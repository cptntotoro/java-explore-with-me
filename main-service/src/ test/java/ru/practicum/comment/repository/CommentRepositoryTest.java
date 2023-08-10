package ru.practicum.comment.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentStatus;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.enums.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    private Comment comment;
    private User user;
    private Event event;
    private Category category;
    private Location location;

    @Before
    public void beforeEach() {
        user = new User(null, "user@mail.com", "User");
        userRepository.save(user);

        category = new Category(null, "Meeting new friends");
        categoryRepository.save(category);

        location = new Location(null, "Central park", 35.6, 22.3);
        locationRepository.save(location);

        event = new Event(null, "11.06 Watching raccoons together in the Central park", category, 0L,
                LocalDateTime.now().minusDays(10), "Looking for pals to contemplate raccoon existence in the park",
                LocalDateTime.now().plusDays(20), user, location, false, 0L, LocalDateTime.now().minusDays(8),
                false, EventState.PUBLISHED, "Raccoon watching", 0L, List.of());
        eventRepository.save(event);

        comment = new Comment(null, "Do they serve lactose-free latte there?", LocalDateTime.now(),
                user, CommentStatus.PENDING, event);
        commentRepository.save(comment);
    }

    @Test
    public void findByIdAndUserIdAndEventId() {
        Optional<Comment> commentOptional = commentRepository.findByIdAndUserIdAndEventId(comment.getId(), user.getId(), event.getId());

        assertNotNull(commentOptional);

        Comment commentSaved = commentOptional.get();

        assertNotNull(commentSaved);
        assertNotNull(commentSaved.getId());
        assertEquals(commentSaved.getUser().getId(), user.getId());
        assertEquals(commentSaved.getEvent().getId(), event.getId());
    }

    @Test
    public void findAllByUserIdAndStatus() {
        List<Comment> commentList = commentRepository.findAllByUserIdAndStatus(user.getId(), CommentStatus.PENDING);

        assertEquals(commentList.size(), 1);

        assertEquals(comment.getId(), commentList.get(0).getId());
        assertEquals(user.getId(), commentList.get(0).getUser().getId());
        assertEquals(CommentStatus.PENDING, commentList.get(0).getStatus());
    }

    @Test
    public void findAllByUserIdAndEventIdAndStatus() {
        List<Comment> commentList = commentRepository.findAllByUserIdAndEventIdAndStatus(user.getId(), event.getId(), CommentStatus.PENDING);

        assertEquals(1, commentList.size());

        assertEquals(comment.getId(), commentList.get(0).getId());
        assertEquals(user.getId(), commentList.get(0).getUser().getId());
        assertEquals(event.getId(), commentList.get(0).getEvent().getId());
        assertEquals(CommentStatus.PENDING, commentList.get(0).getStatus());
    }

    @Test
    public void deleteByIdAndUserIdAndEventIdAndStatus() {
        List<Comment> commentList = commentRepository.findAllByUserIdAndEventIdAndStatus(user.getId(), event.getId(), CommentStatus.PENDING);

        assertEquals(1, commentList.size());

        commentRepository.deleteByIdAndUserIdAndEventIdAndStatus(comment.getId(), user.getId(), event.getId(), CommentStatus.PENDING);

        commentList = commentRepository.findAllByUserIdAndEventIdAndStatus(user.getId(), event.getId(), CommentStatus.PENDING);
        assertEquals(0, commentList.size());
    }

    @Test
    public void findAllByIdInAndStatus() {
        List<Comment> commentList = commentRepository.findAllByIdInAndStatus(List.of(comment.getId()), CommentStatus.PENDING);

        assertEquals(1, commentList.size());

        assertEquals(comment.getId(), commentList.get(0).getId());
        assertEquals(CommentStatus.PENDING, commentList.get(0).getStatus());
    }

    @Test
    public void existsByIdAndUserIdAndEventIdAndStatus() {
        Optional<Comment> commentOptional = commentRepository.findByIdAndUserIdAndEventId(comment.getId(), user.getId(), event.getId());

        assertNotNull(commentOptional);
        assertEquals(CommentStatus.PENDING, commentOptional.get().getStatus());

        Boolean commentExists = commentRepository.existsByIdAndUserIdAndEventIdAndStatus(comment.getId(), user.getId(), event.getId(), CommentStatus.PENDING);

        assertTrue(commentExists);
    }

}
