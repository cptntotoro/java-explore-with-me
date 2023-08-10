package ru.practicum.comment.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.category.model.Category;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentStatus;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.enums.EventState;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommentMapperTest {

    @Test
    void newCommentDtoToComment() {
        NewCommentDto newCommentDto = new NewCommentDto("Do they serve lactose-free latte there?");
        Comment comment = CommentMapper.INSTANCE.newCommentDtoToComment(newCommentDto);

        assertNotNull(comment);
        assertEquals(newCommentDto.getText(), comment.getText());
    }

    @Test
    void commentToCommentDto() {
        User user = new User(1L, "user@mail.com", "User");
        Category category = new Category(1L, "Meeting new friends");
        Location location = new Location(1, "Central park", 35.6, 22.3);
        Event event = new Event(1L, "11.06 Watching raccoons together in the Central park", category, 0L,
                LocalDateTime.now().minusDays(10), "Looking for pals to contemplate raccoon existence in the park",
                LocalDateTime.now().plusDays(20), user, location, false, 0L, LocalDateTime.now().minusDays(8),
                false, EventState.PUBLISHED, "Raccoon watching", 0L, List.of());

        Comment comment = new Comment(null, "Do they serve lactose-free latte there?", LocalDateTime.now(),
                user, CommentStatus.PENDING, event);

        CommentDto commentDto = CommentMapper.INSTANCE.commentToCommentDto(comment);

        assertNotNull(commentDto);
        assertEquals(comment.getText(), commentDto.getText());
        assertEquals(comment.getCreatedOn(), commentDto.getCreatedOn());
        assertEquals(comment.getUser().getId(), commentDto.getUser().getId());
        assertEquals(comment.getEvent().getId(), commentDto.getEvent().getId());
    }

}
