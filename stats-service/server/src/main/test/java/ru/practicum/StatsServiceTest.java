package ru.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.mapper.RequestMapperImpl;
import ru.practicum.model.App;
import ru.practicum.model.Request;
import ru.practicum.repository.AppRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.service.StatsServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {"db.name=test"})
public class StatsServiceTest {

    @InjectMocks
    private StatsServiceImpl statsService;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private AppRepository appRepository;

    @Spy
    private RequestMapperImpl requestMapper;

    RequestDto requestDto;

    RequestOutputDto requestOutputDto;
    Request request;
    App app;

    @BeforeEach
    void setup() {
        request = new Request(1, new App("main-service"), "/events/1", "192.163.0.1",  LocalDateTime.now());
        requestDto = new RequestDto(1, "main-service", "/events/1", "192.163.0.1", LocalDateTime.now());
        requestOutputDto = new RequestOutputDto("main-service", "/events/1", 1L);
        app = new App(requestDto.getApp());
    }

    @Test
    void addRequest() {
        Mockito.when(appRepository.findByName(requestDto.getApp()))
                .thenReturn(Optional.ofNullable(app));

        Mockito.when(requestRepository.save(any()))
                .thenReturn(request);

        statsService.addRequest(requestDto);

        verify(requestRepository, atMostOnce()).saveAndFlush(any());
    }

    @Test
    void getRequestsWithViews_WhenUnique() {
        Mockito.when(requestRepository.getUniqueIpRequestsWithUri(any(), any(), any()))
                .thenReturn(List.of(requestOutputDto));

        List<RequestOutputDto> requestOutputDtoSaved = statsService.getRequestsWithViews(LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1), List.of("/events/1"), true);

        assertAll(
                () -> assertEquals(requestOutputDtoSaved.size(), 1),
                () -> assertEquals(requestOutputDtoSaved.get(0).getUri(), request.getUri()),
                () -> assertEquals(requestOutputDtoSaved.get(0).getApp(), app.getName()),
                () -> assertEquals(requestOutputDtoSaved.get(0).getHits(), 1L)
        );

        verify(requestRepository, atMostOnce()).saveAndFlush(any());
    }

    @Test
    void getRequestsWithViews_WhenNotUnique() {
        Mockito.when(requestRepository.getAllRequestsWithUri(any(), any(), any()))
                .thenReturn(List.of(requestOutputDto));

        List<RequestOutputDto> requestOutputDtoSaved = statsService.getRequestsWithViews(LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1), List.of("/events/1"), false);

        assertAll(
                () -> assertEquals(requestOutputDtoSaved.size(), 1),
                () -> assertEquals(requestOutputDtoSaved.get(0).getUri(), request.getUri()),
                () -> assertEquals(requestOutputDtoSaved.get(0).getApp(), app.getName()),
                () -> assertEquals(requestOutputDtoSaved.get(0).getHits(), 1L)
        );

        verify(requestRepository, atMostOnce()).saveAndFlush(any());
    }
}