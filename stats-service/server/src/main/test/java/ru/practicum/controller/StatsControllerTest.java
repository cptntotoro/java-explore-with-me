package ru.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.RequestDto;
import ru.practicum.RequestOutputDto;
import ru.practicum.mapper.RequestMapperImpl;
import ru.practicum.service.StatsServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatsController.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"db.name=test"})
public class StatsControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Spy
    private RequestMapperImpl requestMapper;

    @MockBean
    private StatsServiceImpl statsService;

    @Autowired
    private MockMvc mockMvc;

    private RequestDto requestDto;
    private RequestOutputDto requestOutputDto;

    @BeforeEach
    void setup() {
        requestDto = new RequestDto(1, "ewm-main-service", "/events/1", "192.163.0.1", LocalDateTime.now());
        requestOutputDto = new RequestOutputDto("ewm-main-service", "/events/1", 1L);
    }

    @Test
    void addRequest() throws Exception {
        mockMvc.perform(post("/hit")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(statsService, times(1)).addRequest(any());
    }

    @Test
    void getStats() throws Exception {
        String startDT = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endDT = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        List<String> uris = List.of(requestDto.getUri());
        Boolean unique = false;

        when(statsService.getRequestsWithViews(any(), any(), any(), any())).thenReturn(List.of(requestOutputDto));

        mockMvc.perform(get("/stats")
                        .param("start", startDT)
                        .param("end", endDT)
                        .param("uris", String.join(",", uris))
                        .param("unique", unique.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].app", is(requestOutputDto.getApp())))
                .andExpect(jsonPath("$.[0].uri", is(requestOutputDto.getUri())))
                .andExpect(jsonPath("$.[0].hits", is(requestOutputDto.getHits().intValue())));
    }

}
