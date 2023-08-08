package ru.practicum.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentStatusUpdateRequest;
import ru.practicum.comment.model.CommentStatus;
import ru.practicum.comment.service.CommentService;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminCommentController.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"db.name=test"})
public class AdminCommentControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    private static CommentDto commentDto;
    private static CommentStatusUpdateRequest updateRequest;
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @BeforeAll
    static void setup() {
        UserShortDto userShortDto = new UserShortDto(1L, "User");
        CategoryDto categoryDto = new CategoryDto("Meeting new friends", 1L);
        EventShortDto eventShortDto = new EventShortDto("11.06 Watching raccoons together in the Central park", 1L,
                categoryDto, 0L, LocalDateTime.now().minusDays(1), userShortDto, false,
                "Raccoon watching", 0L);

        commentDto = new CommentDto("Do they serve lactose-free latte there?", LocalDateTime.now(),
                userShortDto, eventShortDto);

        updateRequest = new CommentStatusUpdateRequest(List.of(1L), CommentStatus.PUBLISHED);
    }

    @Test
    void getAdminComments_shouldReturnStatusOk() throws Exception {
        when(commentService.getAdminComments(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of(commentDto));

        mockMvc.perform(get("/admin/comments")
                        .param("text", "friend")
                        .param("users", "1")
                        .param("statuses", CommentStatus.PENDING.toString())
                        .param("events", "1")
                        .param("rangeStart", LocalDateTime.now().minusDays(1).format(DTF))
                        .param("rangeEnd", LocalDateTime.now().plusDays(1).format(DTF))
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(5)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].text", is(commentDto.getText()), String.class))
                .andExpect(jsonPath("$.[0].createdOn", is(commentDto.getCreatedOn().format(DTF))))
                .andExpect(jsonPath("$.[0].user", is(commentDto.getUser()), UserShortDto.class))
                .andExpect(jsonPath("$.[0].event", is(notNullValue())));

        verify(commentService, times(1))
                .getAdminComments(any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void moderateAdminComments_shouldReturnStatusOk() throws Exception {
        when(commentService.moderateAdminComments(any())).thenReturn(List.of(commentDto));

        mockMvc.perform(patch("/admin/comments")
                        .content(mapper.writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].text", is(commentDto.getText()), String.class))
                .andExpect(jsonPath("$.[0].createdOn", is(commentDto.getCreatedOn().format(DTF))))
                .andExpect(jsonPath("$.[0].user", is(commentDto.getUser()), UserShortDto.class))
                .andExpect(jsonPath("$.[0].event", is(notNullValue())));

        verify(commentService, times(1)).moderateAdminComments(any());
    }
}