package ru.practicum.comment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.mapper.CommentMapperImpl;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentStatus;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.enums.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {"db.name=test"})
public class CommentServiceTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private CommentMapperImpl commentMapper;

    private CommentDto commentDto;
    private Comment comment;
    private NewCommentDto newCommentDto;
    private User user;
    private Event event;
    private Category category;
    private Location location;
    private UserShortDto userShortDto;
    private CategoryDto categoryDto;
    private EventShortDto eventShortDto;

    @BeforeEach
    void setup() {
        user = new User(1L, "user@mail.com", "User");
        category = new Category(1L, "Meeting new friends");
        location = new Location(1, "Central park", 35.6, 22.3);
        event = new Event(1L, "11.06 Watching raccoons together in the Central park", category, 0L,
                LocalDateTime.now().minusDays(10), "Looking for pals to contemplate raccoon existence in the park",
                LocalDateTime.now().plusDays(20), user, location, false, 0L, LocalDateTime.now().minusDays(8),
                false, EventState.PUBLISHED, "Raccoon watching", 0L, List.of());

        userShortDto = new UserShortDto(1L, "User");
        categoryDto = new CategoryDto("Meeting new friends", 1L);
        eventShortDto = new EventShortDto("11.06 Watching raccoons together in the Central park", 1L,
                categoryDto, 0L, LocalDateTime.now().minusDays(1), userShortDto, false,
                "Raccoon watching", 0L);

        commentDto = new CommentDto("Do they serve lactose-free latte there?", LocalDateTime.now(),
                userShortDto, eventShortDto);

        comment = new Comment(1L, "Do they serve lactose-free latte there?", LocalDateTime.now(),
                user, CommentStatus.PENDING, event);

        newCommentDto = new NewCommentDto("Do they serve lactose-free latte there?");
    }

    @Test
    void addUserComment_shouldReturnCommentDto() {
        Mockito.when(userRepository.findById(any()))
                .thenReturn(Optional.ofNullable(user));

        Mockito.when(eventRepository.findById(any()))
                .thenReturn(Optional.ofNullable(event));

        Mockito.when(commentRepository.save(any()))
                .thenReturn(comment);

        CommentDto commentDtoOutput = commentService.addUserComment(1L, 1L, newCommentDto);

        assertNotNull(commentDtoOutput);
        assertEquals(commentDtoOutput.getText(), commentDto.getText());

        verify(userRepository, atMostOnce()).saveAndFlush(any());
        verify(eventRepository, atMostOnce()).saveAndFlush(any());
    }

    @Test
    void updateUserComment_shouldReturnCommentDto() {
        NewCommentDto newCommentDtoToSet = new NewCommentDto("Do they serve lactose-free latte there?");
        comment.setStatus(CommentStatus.PUBLISHED);

        Mockito.when(commentRepository.findByIdAndUserIdAndEventId(any(), any(), any()))
                .thenReturn(Optional.ofNullable(comment));

        comment.setText("Can I bring a friend?");

        Mockito.when(commentRepository.save(any()))
                .thenReturn(comment);

        CommentDto commentDtoOutput = commentService.updateUserComment(1L, 1L, 1L, newCommentDtoToSet);

        assertNotNull(commentDtoOutput);
        assertEquals(commentDtoOutput.getText(), newCommentDtoToSet.getText());
    }

    @Test
    void getUserEventComment_shouldReturnCommentDto() {
        comment.setStatus(CommentStatus.PUBLISHED);
        Mockito.when(commentRepository.findByIdAndUserIdAndEventId(any(), any(), any()))
                .thenReturn(Optional.ofNullable(comment));

        CommentDto commentDtoOutput = commentService.getUserEventComment(1L, 1L, 1L);

        assertNotNull(commentDtoOutput);
        assertEquals(commentMapper.commentToCommentDto(comment), commentDtoOutput);
    }

    @Test
    void getAllUserComments_shouldReturnCommentDto() {
        comment.setStatus(CommentStatus.PUBLISHED);
        Mockito.when(commentRepository.findAllByUserIdAndStatus(any(), any()))
                .thenReturn(List.of(comment));

        List<CommentDto> commentDtoListOutput = commentService.getAllUserComments(1L);

        assertEquals(1, commentDtoListOutput.size());
        assertEquals(commentMapper.commentToCommentDto(comment), commentDtoListOutput.get(0));
    }

    @Test
    void getAllUserEventComments_shouldReturnCommentDto() {
        comment.setStatus(CommentStatus.PUBLISHED);
        Mockito.when(commentRepository.findAllByUserIdAndEventIdAndStatus(any(), any(), any()))
                .thenReturn(List.of(comment));

        List<CommentDto> commentDtoListOutput = commentService.getAllUserEventComments(1L, 1L);

        assertEquals(1, commentDtoListOutput.size());
        assertEquals(commentMapper.commentToCommentDto(comment), commentDtoListOutput.get(0));
    }

    @Test
    void deleteUserComment_shouldNotThrowObjectNotFoundException() {
        comment.setStatus(CommentStatus.PUBLISHED);
        Mockito.when(commentRepository.existsByIdAndUserIdAndEventIdAndStatus(any(), any(), any(), any()))
                .thenReturn(true);

        doNothing().when(commentRepository).deleteByIdAndUserIdAndEventIdAndStatus(any(), any(), any(), any());

        assertDoesNotThrow(() -> commentService.deleteUserComment(user.getId(), event.getId(), comment.getId()));
    }
}
