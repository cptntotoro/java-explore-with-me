package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventUpdateDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.enums.EventState;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public List<EventFullDto> getAdminEvents(@RequestParam(name = "users", required = false) List<Long> users,
                                             @RequestParam(name = "states", required = false) List<EventState> states,
                                             @RequestParam(name = "categories", required = false) List<Long> categories,
                                             @RequestParam(name = "rangeStart", required = false)
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                             @RequestParam(name = "rangeEnd", required = false)
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                             @RequestParam(name = "from", defaultValue = "0") Integer from,
                                             @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("Calling GET: /admin/events with 'users': {}, 'states': {}, 'categories': {}, 'rangeStart': {}, " +
                "'rangeEnd': {}, 'from': {}, 'size': {}", users, states, categories, rangeStart, rangeEnd, from, size);

        return eventMapper.listEventToListEventFullDto(eventService.getAdminEvents(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping(path = "/{eventId}")
    public EventFullDto updateAdminEvent(@PathVariable("eventId") Long eventId,
                                         @RequestBody @Valid EventUpdateDto eventUpdateDto) {
        log.info("Calling PATCH: /admin/events/{eventId} with 'eventId': {}, 'eventAdminPatchDto': {}", eventId, eventUpdateDto);
        return eventMapper.eventToEventFullDto(eventService.updateAdminEvent(eventId, eventUpdateDto));
    }
}
