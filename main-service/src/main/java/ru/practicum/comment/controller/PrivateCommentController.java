package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
@Slf4j
public class PrivateCommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @PostMapping("/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createUserComment(@PathVariable @Positive Long userId,
                                        @PathVariable @Positive Long eventId,
                                        @RequestBody @Valid NewCommentDto newCommentDto) {

        log.info("Calling POST: /users/{userId}/events/{eventId}/comments with 'userId': {}, 'eventId': {}," +
                " 'newCommentDto': {}", userId, eventId, newCommentDto);
        return commentMapper.commentToCommentDto(commentService.addUserComment(userId, eventId, newCommentDto));
    }

    @GetMapping("/events/{eventId}/comments/{commentId}")
    public CommentDto getUserComment(@PathVariable @Positive Long userId,
                                     @PathVariable @Positive Long eventId,
                                     @PathVariable @Positive Long commentId) {

        log.info("Calling GET: /users/{userId}/events/{eventId}/comments/{commentId} with 'userId': {}, 'eventId': {}," +
                " 'commentId': {}", userId, eventId, commentId);
        return commentMapper.commentToCommentDto(commentService.getUserEventComment(userId, eventId, commentId));
    }

    @GetMapping ("/events/{eventId}/comments")
    public List<CommentDto> getUserEventComments(@PathVariable @Positive Long userId,
                                                 @PathVariable @Positive Long eventId) {

        log.info("Calling GET: /users/{userId}/events/{eventId}/comments with 'userId': {}, 'eventId': {},", userId, eventId);
        return commentMapper.listCommentToListCommentDto(commentService.getAllUserEventComments(userId, eventId));
    }

    @GetMapping ("/comments")
    public List<CommentDto> getUserComments(@PathVariable @Positive Long userId) {
        log.info("Calling GET: /comments with 'userId': {}", userId);
        return commentMapper.listCommentToListCommentDto(commentService.getAllUserComments(userId));
    }

    @PatchMapping("/events/{eventId}/comments/{commentId}")
    public CommentDto updateUserComment(@PathVariable @Positive Long userId,
                                        @PathVariable @Positive Long eventId,
                                        @PathVariable @Positive Long commentId,
                                        @RequestBody @Valid NewCommentDto newCommentDto) {

        log.info("Calling PATCH: /users/{userId}/events/{eventId}/comments/{commentId} with 'userId': {}, 'eventId': {}," +
                " , 'commentId': {}, 'newCommentDto': {}", userId, eventId, commentId, newCommentDto);
        return commentMapper.commentToCommentDto(commentService.updateUserComment(userId, eventId, commentId, newCommentDto));
    }

    @DeleteMapping("/events/{eventId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserComment(@PathVariable @Positive Long userId,
                                  @PathVariable @Positive Long eventId,
                                  @PathVariable @Positive Long commentId) {

        log.info("Calling DELETE: /users/{userId}/events/{eventId}/comments/{commentId} with 'userId': {}, 'eventId': {}," +
                " , 'commentId': {}", userId, eventId, commentId);
        commentService.deleteUserComment(userId, eventId, commentId);
    }
}
