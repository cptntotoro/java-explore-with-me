package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentStatusUpdateRequest;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.model.CommentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    CommentDto addUserComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentDto updateUserComment(Long userId, Long eventId, Long commentId, NewCommentDto newCommentDto);

    void deleteUserComment(Long userId, Long eventId, Long commentId);

    CommentDto getUserEventComment(Long userId, Long eventId, Long commentId);

    List<CommentDto> getAllUserComments(Long userId);

    List<CommentDto> getAllUserEventComments(Long userId, Long eventId);

    List<CommentDto> getAdminComments(String text, List<Long> users, List<CommentStatus> statuses, List<Long> events, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    List<CommentDto> moderateAdminComments(CommentStatusUpdateRequest updateRequest);
}
