package ru.practicum.event.service;

import ru.practicum.event.dto.EventUpdateDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.enums.EventSort;
import ru.practicum.event.model.enums.EventState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<Event> getAdminEvents(List<Long> users, List<EventState> states, List<Long> categories,
                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    Event updateAdminEvent(Long eventId, EventUpdateDto eventUpdateDto);

    List<Event> getAll(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                       LocalDateTime rangeEnd, Boolean onlyAvailable, Integer from, Integer size,
                       EventSort sort, HttpServletRequest request);

    Event get(Long id, HttpServletRequest request);

    List<Event> getUserEvents(Long userId, Integer from, Integer size);

    Event addUserEvent(Long userId, NewEventDto event);

    Event getUserEventById(Long userId, Long eventId);

    Event updateUserEventById(Long userId, Long eventId, EventUpdateDto eventDto);
}
