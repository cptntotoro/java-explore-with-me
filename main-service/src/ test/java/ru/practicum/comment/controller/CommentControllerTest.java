package ru.practicum.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import ru.practicum.comment.dto.NewCommentDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"db.name=test"})
public class CommentControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    private CommentDto commentDto;
    private NewCommentDto newCommentDto;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @BeforeEach
    void setup() {
        UserShortDto userShortDto = new UserShortDto(1L, "User");
        CategoryDto categoryDto = new CategoryDto("Meeting new friends", 1L);
        EventShortDto eventShortDto = new EventShortDto("11.06 Watching raccoons together in the Central park", 1L,
                categoryDto, 0L, LocalDateTime.now().minusDays(1), userShortDto, false,
                "Raccoon watching", 0L);

        commentDto = new CommentDto("Do they serve lactose-free latte there?", LocalDateTime.now(),
                userShortDto, eventShortDto);

        newCommentDto = new NewCommentDto("Do they serve lactose-free latte there?");
    }

    @Test
    void createUserComment_shouldReturnStatusCreated() throws Exception {
        when(commentService.addUserComment(any(), any(), any())).thenReturn(commentDto);

        mockMvc.perform(post("/users/{userId}/events/{eventId}/comments", 1, 1)
                        .content(mapper.writeValueAsString(newCommentDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text", is(commentDto.getText()), String.class))
                .andExpect(jsonPath("$.createdOn", is(commentDto.getCreatedOn().format(DTF))))
                .andExpect(jsonPath("$.user", is(commentDto.getUser()), UserShortDto.class))
                .andExpect(jsonPath("$.event", is(notNullValue())));

        verify(commentService, times(1)).addUserComment(any(), any(), any());
    }

    @Test
    void getUserComment_shouldReturnStatusOk() throws Exception {
        when(commentService.getUserEventComment(any(), any(), any())).thenReturn(commentDto);

        mockMvc.perform(get("/users/{userId}/events/{eventId}/comments/{commentId}", 1, 1, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(commentDto.getText()), String.class))
                .andExpect(jsonPath("$.createdOn", is(commentDto.getCreatedOn().format(DTF))))
                .andExpect(jsonPath("$.user", is(commentDto.getUser()), UserShortDto.class))
                .andExpect(jsonPath("$.event", is(notNullValue())));

        verify(commentService, times(1)).getUserEventComment(any(), any(), any());
    }

    @Test
    void getUserEventComments_shouldReturnStatusOk() throws Exception {
        when(commentService.getAllUserEventComments(any(), any())).thenReturn(List.of(commentDto));

        mockMvc.perform(get("/users/{userId}/events/{eventId}/comments", 1, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].text", is(commentDto.getText()), String.class))
                .andExpect(jsonPath("$.[0].createdOn", is(commentDto.getCreatedOn().format(DTF))))
                .andExpect(jsonPath("$.[0].user", is(commentDto.getUser()), UserShortDto.class))
                .andExpect(jsonPath("$.[0].event", is(notNullValue())));

        verify(commentService, times(1)).getAllUserEventComments(any(), any());
    }

    @Test
    void getUserComments_shouldReturnStatusOk() throws Exception {
        when(commentService.getAllUserComments(any())).thenReturn(List.of(commentDto));

        mockMvc.perform(get("/users/{userId}/comments", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].text", is(commentDto.getText()), String.class))
                .andExpect(jsonPath("$.[0].createdOn", is(commentDto.getCreatedOn().format(DTF))))
                .andExpect(jsonPath("$.[0].user", is(commentDto.getUser()), UserShortDto.class))
                .andExpect(jsonPath("$.[0].event", is(notNullValue())));

        verify(commentService, times(1)).getAllUserComments(any());
    }

    @Test
    void updateUserComment_shouldReturnStatusOk() throws Exception {
        when(commentService.updateUserComment(any(), any(), any(), any())).thenReturn(commentDto);

        mockMvc.perform(patch("/users/{userId}/events/{eventId}/comments/{commentId}", 1, 1, 1)
                        .content(mapper.writeValueAsString(newCommentDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(commentDto.getText()), String.class))
                .andExpect(jsonPath("$.createdOn", is(commentDto.getCreatedOn().format(DTF))))
                .andExpect(jsonPath("$.user", is(commentDto.getUser()), UserShortDto.class))
                .andExpect(jsonPath("$.event", is(notNullValue())));

        verify(commentService, times(1)).updateUserComment(any(), any(), any(), any());
    }

    @Test
    void deleteUserComment_shouldReturnStatusNoContent() throws Exception {
        doNothing().when(commentService).deleteUserComment(any(), any(), any());

        mockMvc.perform(delete("/users/{userId}/events/{eventId}/comments/{commentId}", 1, 1, 1))
                .andExpect(status().isNoContent());

        verify(commentService, times(1)).deleteUserComment(any(), any(), any());
    }
}
