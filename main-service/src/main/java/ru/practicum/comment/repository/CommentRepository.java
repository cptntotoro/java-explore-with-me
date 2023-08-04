package ru.practicum.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndUserIdAndEventId(Long commentId, Long userId, Long eventId);

    List<Comment> findAllByUserIdAndStatus(Long userId, CommentStatus published);

    List<Comment> findAllByUserIdAndEventIdAndStatus(Long userId, Long eventId, CommentStatus published);

    void deleteByIdAndUserIdAndEventIdAndStatus(Long commentId, Long userId, Long eventId, CommentStatus published);

    List<Comment> findAllByIdInAndStatus(List<Long> commentIds, CommentStatus status);

    boolean existsByIdAndUserIdAndEventIdAndStatus(Long commentId, Long userId, Long eventId, CommentStatus published);
}
