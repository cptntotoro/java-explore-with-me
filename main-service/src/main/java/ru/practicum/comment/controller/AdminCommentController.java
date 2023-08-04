package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentStatusUpdateRequest;
import ru.practicum.comment.model.CommentStatus;
import ru.practicum.comment.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
@Slf4j
public class AdminCommentController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getAdminComments(@RequestParam(name = "text", required = false) String text,
                                             @RequestParam(name = "users", required = false) List<Long> users,
                                             @RequestParam(name = "statuses", required = false) List<CommentStatus> statuses,
                                             @RequestParam(name = "events", required = false) List<Long> events,
                                             @RequestParam(name = "rangeStart", required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                             @RequestParam(name = "rangeEnd", required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                             @RequestParam(name = "from", defaultValue = "0") Integer from,
                                             @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("Calling GET: /admin/comments with 'test': {}, 'users': {}, 'statuses': {}, 'events': {}, 'rangeStart': {}, " +
                "'rangeEnd': {}, 'from': {}, 'size': {}", text, users, statuses, events, rangeStart, rangeEnd, from, size);
        return commentService.getAdminComments(text, users, statuses, events, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping
    public List<CommentDto> moderateAdminComments(@RequestBody CommentStatusUpdateRequest updateRequest) {

        log.info("Calling PATCH: /admin/comments with 'updateRequest': {}", updateRequest);
        return commentService.moderateAdminComments(updateRequest);
    }
}