package ru.practicum.service;

import ru.practicum.RequestDto;
import ru.practicum.RequestOutputDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void addRequest(RequestDto requestDto);

    List<RequestOutputDto> getRequestsWithViews(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

    List<RequestOutputDto> getRequestsWithViewsByIp(LocalDateTime startDT, LocalDateTime endDT, List<String> uris, Boolean unique, String ip);
}
