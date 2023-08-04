package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createUserComment(@PathVariable @Positive Long userId,
                                        @PathVariable @Positive Long eventId,
                                        @RequestBody @Valid NewCommentDto newCommentDto) {

        log.info("Calling POST: /users/{userId}/events/{eventId}/comments with 'userId': {}, 'eventId': {}," +
                " 'newCommentDto': {}", userId, eventId, newCommentDto);
        return commentService.addUserComment(userId, eventId, newCommentDto);
    }

    @GetMapping ("/events/{eventId}/comments/{commentId}")
    public CommentDto getUserComment(@PathVariable @Positive Long userId,
                                     @PathVariable @Positive Long eventId,
                                     @PathVariable @Positive Long commentId) {

        log.info("Calling GET: /users/{userId}/events/{eventId}/comments/{commentId} with 'userId': {}, 'eventId': {}," +
                " 'commentId': {}", userId, eventId, commentId);
        return commentService.getUserEventComment(userId, eventId, commentId);
    }

    @GetMapping ("/events/{eventId}/comments")
    public List<CommentDto> getUserEventComments(@PathVariable @Positive Long userId,
                                                 @PathVariable @Positive Long eventId) {

        log.info("Calling GET: /users/{userId}/events/{eventId}/comments with 'userId': {}, 'eventId': {},", userId, eventId);
        return commentService.getAllUserEventComments(userId, eventId);
    }

    @GetMapping ("/comments")
    public List<CommentDto> getUserComments(@PathVariable @Positive Long userId) {

        log.info("Calling POST: /users/{userId}/events/{eventId}/comments with 'userId': {}", userId);
        return commentService.getAllUserComments(userId);
    }

    @PatchMapping("/events/{eventId}/comments/{commentId}")
    public CommentDto updateUserComment(@PathVariable @Positive Long userId,
                                        @PathVariable @Positive Long eventId,
                                        @PathVariable @Positive Long commentId,
                                        @RequestBody @Valid NewCommentDto newCommentDto) {

        log.info("Calling PATCH: /users/{userId}/events/{eventId}/comments/{commentId} with 'userId': {}, 'eventId': {}," +
                " , 'commentId': {}, 'newCommentDto': {}", userId, eventId, commentId, newCommentDto);
        return commentService.updateUserComment(userId, eventId, commentId, newCommentDto);
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
