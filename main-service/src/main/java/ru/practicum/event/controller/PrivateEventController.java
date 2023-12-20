package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.EventUpdateDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.request.model.ParticipationStatus;
import ru.practicum.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {

    private final EventService eventService;
    private final RequestService requestService;
    private final RequestMapper requestMapper;
    private final EventMapper eventMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addUserEvent(@PathVariable Long userId,
                                     @RequestBody @Valid NewEventDto event) {
        log.info("Calling POST: /users/{userId}/events with 'userId': {}, 'event': {}", userId, event);
        return eventMapper.eventToEventFullDto(eventService.addUserEvent(userId, event));
    }

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {
        log.info("Calling GET: /users/{userId}/events with 'userId': {}, 'from': {}, 'size': {}", userId, from, size);
        return eventMapper.listEventToListEventShortDto(eventService.getUserEvents(userId, from, size));
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEventById(@PathVariable Long userId,
                                         @PathVariable Long eventId) {
        log.info("Calling GET: /users/{userId}/events/{eventId} with 'userId': {}, 'eventId': {}", userId, eventId);
        return eventMapper.eventToEventFullDto(eventService.getUserEventById(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateUserEventById(@PathVariable @NotNull Long userId,
                                            @PathVariable @NotNull Long eventId,
                                            @RequestBody @Valid EventUpdateDto eventDto) {
        log.info("Calling PATCH: /users/{userId}/events/{eventId} with 'userId': {}, 'eventId': {}", userId, eventId);
        return eventMapper.eventToEventFullDto(eventService.updateUserEventById(userId, eventId, eventDto));
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getUserEventsRequests(@PathVariable Long userId,
                                                               @PathVariable Long eventId) {
        log.info("Calling GET: /users/{userId}/events/{eventId}/requests with 'userId': {}, 'eventId': {}", userId, eventId);
        return requestMapper.listRequestToListRequestDto(requestService.getUserEventRequests(userId, eventId));
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResultDto updateUserEventRequests(@PathVariable Long userId,
                                                                     @PathVariable Long eventId,
                                                                     @RequestBody EventRequestStatusUpdateRequest requestsUpdate) {
        log.info("Calling PATCH: /users/{userId}/events/{eventId}/requests with 'userId': {}, " +
                "'eventId': {}, 'requestsUpdate': {}", userId, eventId, requestsUpdate);

        Event event = requestService.checkParticipationRequest(userId, eventId);

        List<ParticipationRequest> participationRequests = requestService.getEventRequests(eventId, requestsUpdate);

        requestService.updateEventRequests(event, requestsUpdate, participationRequests);

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        participationRequests.forEach(participationRequest -> {
            if (participationRequest.getStatus() == ParticipationStatus.REJECTED) {
                rejectedRequests.add(requestMapper.requestToRequestDto(participationRequest));
            } else {
                confirmedRequests.add(requestMapper.requestToRequestDto(participationRequest));
            }
        });

        return new EventRequestStatusUpdateResultDto(confirmedRequests, rejectedRequests);
    }

}
