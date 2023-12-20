package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentStatusUpdateRequest;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    Comment addUserComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    Comment updateUserComment(Long userId, Long eventId, Long commentId, NewCommentDto newCommentDto);

    void deleteUserComment(Long userId, Long eventId, Long commentId);

    Comment getUserEventComment(Long userId, Long eventId, Long commentId);

    List<Comment> getAllUserComments(Long userId);

    List<Comment> getAllUserEventComments(Long userId, Long eventId);

    List<Comment> getAdminComments(String text, List<Long> users, List<CommentStatus> statuses, List<Long> events, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    List<Comment> moderateAdminComments(CommentStatusUpdateRequest updateRequest);
}
