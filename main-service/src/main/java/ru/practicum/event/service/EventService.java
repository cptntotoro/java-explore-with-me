package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.EventUpdateDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.enums.EventSort;
import ru.practicum.event.model.enums.EventState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventFullDto> getAdminEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto updateAdminEvent(Long eventId, EventUpdateDto eventUpdateDto);

    List<EventShortDto> getAll(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                               LocalDateTime rangeEnd, Boolean onlyAvailable, Integer from, Integer size,
                               EventSort sort, HttpServletRequest request);

    EventFullDto get(Long id, HttpServletRequest request);

    List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size);

    EventFullDto addUserEvent(Long userId, NewEventDto event);

    EventFullDto getUserEventById(Long userId, Long eventId);

    EventFullDto updateUserEventById(Long userId, Long eventId, EventUpdateDto eventDto);
}
