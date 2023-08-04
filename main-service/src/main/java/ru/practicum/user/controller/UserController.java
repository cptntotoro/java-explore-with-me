package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final RequestService requestService;
    private final EventService eventService;

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addUserEvent(@PathVariable Long userId,
                                     @RequestBody @Valid NewEventDto event) {

        log.info("Calling POST: /users/{userId}/events with 'userId': {}, 'event': {}", userId, event);
        return eventService.addUserEvent(userId, event);
    }

    @GetMapping("/events")
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {

        log.info("Calling GET: /users/{userId}/events with 'userId': {}, 'from': {}, 'size': {}", userId, from, size);
        return eventService.getUserEvents(userId, from, size);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getUserEventById(@PathVariable Long userId,
                                         @PathVariable Long eventId) {

        log.info("Calling GET: /users/{userId}/events/{eventId} with 'userId': {}, 'eventId': {}", userId, eventId);
        return eventService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateUserEventById(@PathVariable @NotNull Long userId,
                                            @PathVariable @NotNull Long eventId,
                                            @RequestBody @Valid EventUpdateDto eventDto) {

        log.info("Calling PATCH: /users/{userId}/events/{eventId} with 'userId': {}, 'eventId': {}", userId, eventId);
        return eventService.updateUserEventById(userId, eventId, eventDto);
    }

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId) {

        log.info("Calling GET: /users/{userId}/requests with 'userId': {}", userId);
        return requestService.getUserRequests(userId);
    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addUserRequest(@PathVariable Long userId,
                                                  @RequestParam(name = "eventId") Long eventId) {

        log.info("Calling POST: /users/{userId}/requests with 'userId': {}, 'eventId': {}", userId, eventId);
        return requestService.addParticipationRequest(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto updateUserRequest(@PathVariable Long userId,
                                                     @PathVariable Long requestId) {

        log.info("Calling PATCH: /users/{userId}/requests/{requestId}/cancel with 'userId': {}, 'requestId': {}", userId, requestId);
        return requestService.cancelParticipationRequest(userId, requestId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getUserEventsRequests(@PathVariable Long userId,
                                                               @PathVariable Long eventId) {

        log.info("Calling GET: /users/{userId}/events/{eventId}/requests with 'userId': {}, 'eventId': {}", userId, eventId);
        return requestService.getUserEventRequests(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateUserEventRequests(@PathVariable Long userId,
                                                                  @PathVariable Long eventId,
                                                                  @RequestBody EventRequestStatusUpdateRequest requestsUpdate) {

        log.info("Calling PATCH: /users/{userId}/events/{eventId}/requests with 'userId': {}, " +
                "'eventId': {}, 'requestsUpdate': {}", userId, eventId, requestsUpdate);
        return requestService.updateEventRequests(userId, eventId, requestsUpdate);
    }
}