package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.enums.EventState;
import ru.practicum.event.model.enums.EventStatus;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.exception.RequestConflictException;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.request.model.ParticipationStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public ParticipationRequest addParticipationRequest(Long userId, Long eventId) {
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new RequestConflictException("Participation request with userId = " + userId
                    + " eventId = " + eventId + " already exists.");
        }

        User requester = userRepository.findById(userId).orElseThrow(() -> {
            throw new ObjectNotFoundException("User with id = " + userId + " was not found.");
        });

        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Event with id = " + eventId + " doesn't exist.");
                });

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new RequestConflictException("Users are not allowed to register for unpublished events.");
        }

        if (Objects.equals(userId, event.getInitiator().getId())) {
            throw new RequestConflictException("Event organizers are not allowed to request participation in their own events.");
        }

        if ((event.getParticipantLimit() != 0L) && (event.getConfirmedRequests() >= event.getParticipantLimit())) {
            throw new RequestConflictException("Participant limit reached.");
        }

        ParticipationRequest requestToSave = new ParticipationRequest(requester, event,
                !event.getRequestModeration() || event.getParticipantLimit() == 0L ?
                        ParticipationStatus.CONFIRMED : ParticipationStatus.PENDING, LocalDateTime.now());

        ParticipationRequest participationRequest = requestRepository.save(requestToSave);

        if (participationRequest.getStatus() == ParticipationStatus.CONFIRMED) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        return participationRequest;
    }

    @Override
    public ParticipationRequest cancelParticipationRequest(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Participation request with id = " + requestId + " doesn't exist.");
        });

        if (request.getStatus() == ParticipationStatus.CONFIRMED) {
            throw new RequestConflictException("Participation request with id = " + requestId + " is already confirmed.");
        }

        request.setStatus(ParticipationStatus.CANCELED);

        Long eventId = request.getEvent().getId();
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Event with id = " + eventId + " doesn't exist.");
        });

        event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        eventRepository.save(event);

        return requestRepository.save(request);
    }

    @Override
    public List<ParticipationRequest> getUserRequests(Long userId) {
        List<ParticipationRequest> requests = requestRepository.findAllByRequesterId(userId);

        if (!requests.isEmpty()) {
            return requests;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<ParticipationRequest> getUserEventRequests(Long userId, Long eventId) {
        List<ParticipationRequest> requests = requestRepository.findAllByEventIdAndEventInitiatorId(eventId, userId);

        if (!requests.isEmpty()) {
            return requests;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<ParticipationRequest> getEventRequests(Long eventId, @Valid EventRequestStatusUpdateRequest requestsUpdate) {
        List<ParticipationRequest> participationRequests = requestRepository.findAllByIdInAndAndEventId(requestsUpdate.getRequestIds(), eventId);

        if (participationRequests.size() != requestsUpdate.getRequestIds().size()) {
            throw new ObjectNotFoundException("Incorrect request id(s) in the request body.");
        }

        return participationRequests;
    }

    @Override
    public Event checkParticipationRequest(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Event with id = " + eventId + " and user id = " + userId + " doesn't exist.");
        });

        if (!event.getInitiator().getId().equals(userId)) {
            throw new RequestConflictException("Access denied. User with id = " + userId + " is not an event initiator.");
        }

        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new RequestConflictException("Failed to accept request. Reached max participant limit for event id = " + eventId + ".");
        }

        return event;
    }

    @Override
    public void updateEventRequests(Event event, @Valid EventRequestStatusUpdateRequest requestsUpdate, List<ParticipationRequest> participationRequests) {
        for (ParticipationRequest request : participationRequests) {
            if (!request.getStatus().equals(ParticipationStatus.PENDING)) {
                throw new RequestConflictException("Only requests with status 'Pending' can be accepted or rejected.");
            }
        }

        if (requestsUpdate.getStatus() == EventStatus.REJECTED) {
            participationRequests.forEach(participationRequest -> {
                participationRequest.setStatus(ParticipationStatus.REJECTED);
                requestRepository.save(participationRequest);
            });
            return;
        }

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            participationRequests.forEach(participationRequest -> {
                participationRequest.setStatus(ParticipationStatus.CONFIRMED);
                requestRepository.save(participationRequest);
            });
            return;
        }

        participationRequests.forEach(participationRequest -> {
            if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                participationRequest.setStatus(ParticipationStatus.CONFIRMED);
                requestRepository.save(participationRequest);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            } else {
                participationRequest.setStatus(ParticipationStatus.REJECTED);
                requestRepository.save(participationRequest);
            }
        });

        if (participationRequests.stream().anyMatch(participationRequest -> participationRequest.getStatus() == ParticipationStatus.CONFIRMED)) {
            eventRepository.save(event);
        }
    }
}