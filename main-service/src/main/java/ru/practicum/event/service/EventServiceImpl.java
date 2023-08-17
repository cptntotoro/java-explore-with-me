package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.RequestDto;
import ru.practicum.RequestOutputDto;
import ru.practicum.StatsClient;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.EventUpdateDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.enums.EventSort;
import ru.practicum.event.model.enums.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.EventSpecRepository;
import ru.practicum.exception.IncorrectRequestException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.exception.RequestConflictException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;
import static ru.practicum.event.repository.EventSpecRepository.*;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventRepository eventRepository;
    private final EventSpecRepository eventSpecRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;
    private final StatsClient statsClient = new StatsClient("http://ewm-stats-server:9090");

    @Override
    public List<EventFullDto> getAdminEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {

        if ((rangeStart != null && rangeEnd != null) && (rangeStart.isAfter(rangeEnd) || rangeStart.isEqual(rangeEnd))) {
            throw new IncorrectRequestException("Start time must not after or equal to end time.");
        }

        Pageable pageable = PageRequest.of(from / size, size, Sort.unsorted());

        if (users != null || states != null || categories != null || rangeStart != null || rangeEnd != null) {

            Page<Event> events = eventSpecRepository.findAll(where(hasUsers(users))
                    .and(hasStates(states))
                    .and(hasCategories(categories))
                    .and(hasRangeStart(rangeStart))
                    .and(hasRangeEnd(rangeEnd)),
                    pageable);

            return events.stream()
                    .map(eventMapper::eventToEventFullDto)
                    .collect(Collectors.toList());
        } else {
            return eventRepository.findAll(pageable).stream()
                    .map(eventMapper::eventToEventFullDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public EventFullDto updateAdminEvent(Long eventId, EventUpdateDto eventUpdateDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Event with id = " + eventId + " is not found.");
        });

        updateEvent(event, eventUpdateDto);

        if (eventUpdateDto.getStateAction() != null) {
            switch (eventUpdateDto.getStateAction()) {
                case REJECT_EVENT:
                    if (event.getState().equals(EventState.PUBLISHED)) {
                        throw new RequestConflictException("Event with id = " + eventId + " is published and can't be cancelled.");
                    }
                    event.setState(EventState.CANCELED);
                    break;
                case PUBLISH_EVENT:
                    if (!event.getState().equals(EventState.PENDING)) {
                        throw new RequestConflictException("Event with id = " + eventId + " is not pending and can't be published.");
                    }
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
            }
        }

        event = eventRepository.save(event);
        return eventMapper.eventToEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getAll(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd, Boolean onlyAvailable, Integer from, Integer size,
                                      EventSort sort, HttpServletRequest request) {

        if ((rangeStart != null && rangeEnd != null) && (rangeStart.isAfter(rangeEnd) || rangeStart.isEqual(rangeEnd))) {
            throw new IncorrectRequestException("Start time must not after or equal to end time.");
        }

        Pageable pageable = sort.equals(EventSort.VIEWS)
                ? PageRequest.of(from / size, size, Sort.by("views"))
                : PageRequest.of(from / size, size, Sort.by("eventDate"));

        Page<Event> eventsPage = eventSpecRepository.findAll(where(hasText(text))
                .and(hasCategories(categories))
                .and(hasPaid(paid))
                .and(hasRangeStart(rangeStart))
                .and(hasRangeEnd(rangeEnd))
                .and(hasAvailable(onlyAvailable)), pageable);

        updateViews(eventsPage.toList(), request);

        return eventsPage.stream()
                .filter(event -> event.getPublishedOn() != null)
                .map(eventMapper::eventToShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto get(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndStateIs(eventId, EventState.PUBLISHED).orElseThrow(() -> {
            throw new ObjectNotFoundException("Event with id = " + eventId + " was not found.");
        });

        updateViews(Collections.singletonList(event), request);

        return eventMapper.eventToEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").ascending());

        return eventRepository.findAllByInitiatorId(userId, pageable).stream()
                .map(eventMapper::eventToShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto addUserEvent(Long userId, NewEventDto eventDto) {
        Event event = eventMapper.newEventDtoToEvent(eventDto);

        updateEvent(event, userId, eventDto);

        event = eventRepository.save(event);
        return eventMapper.eventToEventFullDto(event);
    }

    @Override
    public EventFullDto getUserEventById(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Event with id = " + eventId + " and user id = " + userId + " is not found.");
        });

        event.setViews(event.getViews() + 1);

        event = eventRepository.save(event);
        return eventMapper.eventToEventFullDto(event);
    }

    @Override
    public EventFullDto updateUserEventById(Long userId, Long eventId, EventUpdateDto eventDto) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Event with id = " + eventId + " and user id = " + userId + " is not found.");
        });

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new RequestConflictException("Event must not be published.");
        }

        updateEvent(event, eventDto);

        if (eventDto.getStateAction() != null) {
            switch (eventDto.getStateAction()) {
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
            }
        }

        event = eventRepository.save(event);

        return eventMapper.eventToEventFullDto(event);
    }

    private void updateViews(List<Event> events, HttpServletRequest request) {
        RequestDto requestDto = new RequestDto();
        requestDto.setIp(request.getRemoteAddr());
        requestDto.setUri(request.getRequestURI());
        requestDto.setTimestamp(LocalDateTime.now());
        requestDto.setApp("main-service");

        ResponseEntity<List<RequestOutputDto>> listResponseEntity = statsClient.getStatsByIp(LocalDateTime.now().minusHours(1).format(DTF),
                LocalDateTime.now().format(DTF),
                Collections.singletonList(requestDto.getUri()),
                true,
                request.getRemoteAddr());

        statsClient.addRequest(requestDto);

        if (listResponseEntity.getStatusCode() == HttpStatus.OK &&
                Optional.ofNullable(listResponseEntity.getBody())
                        .map(List::isEmpty).orElse(false)) {
            events.forEach(event -> {
                event.setViews(event.getViews() + 1);
            });
            eventRepository.saveAll(events);
        }
    }

    private void updateEvent(Event event, Long userId, NewEventDto eventDto) {
        User initiator = userRepository.findById(userId).orElseThrow(() -> {
            throw new ObjectNotFoundException("User with id = " + userId + " doesn't exist.");
        });

        event.setInitiator(initiator);

        if (eventDto.getPaid() == null) {
            event.setPaid(false);
        }

        if (eventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }

        event.setCreatedOn(LocalDateTime.now());

        Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(() -> {
            throw new ObjectNotFoundException("Category with id = " + eventDto.getCategory() + " doesn't exist.");
        });
        event.setCategory(category);

        event.setState(EventState.PENDING);
    }

    private void updateEvent(Event event, EventUpdateDto eventUpdateDto) {
        if (eventUpdateDto.getAnnotation() != null) {
            event.setAnnotation(eventUpdateDto.getAnnotation());
        }

        if (eventUpdateDto.getTitle() != null) {
            event.setTitle(eventUpdateDto.getTitle());
        }

        if (eventUpdateDto.getDescription() != null) {
            event.setDescription(eventUpdateDto.getDescription());
        }

        if (eventUpdateDto.getCategory() != null) {
            Category category = categoryRepository.findById(eventUpdateDto.getCategory())
                    .orElseThrow(() -> new ObjectNotFoundException("Category with id = " + eventUpdateDto.getCategory() + " is not found"));
            event.setCategory(category);
        }

        if (eventUpdateDto.getLocation() != null) {
            event.setLocation(eventUpdateDto.getLocation());
        }

        if (eventUpdateDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUpdateDto.getParticipantLimit());
        }

        if (eventUpdateDto.getEventDate() != null) {
            event.setEventDate(eventUpdateDto.getEventDate());
        }

        if (eventUpdateDto.getRequestModeration() != null) {
            event.setRequestModeration(eventUpdateDto.getRequestModeration());
        }

        if (eventUpdateDto.getPaid() != null) {
            event.setPaid(eventUpdateDto.getPaid());
        }
    }
}