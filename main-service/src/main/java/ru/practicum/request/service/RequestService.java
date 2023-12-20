package ru.practicum.request.service;

import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.model.Event;
import ru.practicum.request.model.ParticipationRequest;

import javax.validation.Valid;
import java.util.List;

public interface RequestService {
    ParticipationRequest addParticipationRequest(Long userId, Long eventId);

    ParticipationRequest cancelParticipationRequest(Long userId, Long requestId);

    List<ParticipationRequest> getUserRequests(Long userId);

    List<ParticipationRequest> getUserEventRequests(Long userId, Long eventId);

    List<ParticipationRequest> getEventRequests(Long eventId, @Valid EventRequestStatusUpdateRequest requestsUpdate);

    Event checkParticipationRequest(Long userId, Long eventId);

    void updateEventRequests(Event event, @Valid EventRequestStatusUpdateRequest requestsUpdate, List<ParticipationRequest> participationRequests);
}
