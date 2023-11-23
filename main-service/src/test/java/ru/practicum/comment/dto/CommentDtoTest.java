package ru.practicum.comment.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentDtoTest {

    @Autowired
    private JacksonTester<CommentDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {
        UserShortDto userShortDto = new UserShortDto(1L, "User");
        CategoryDto categoryDto = new CategoryDto("Meeting new friends", 1L);
        EventShortDto eventShortDto = new EventShortDto("11.06 Watching raccoons together in the Central park", 1L,
                categoryDto, 0L, LocalDateTime.now().minusDays(1), userShortDto, false,
                "Raccoon watching", 0L);

        CommentDto commentDto = new CommentDto("Do they serve lactose-free latte there?", LocalDateTime.now(),
                userShortDto, eventShortDto);

        JsonContent<CommentDto> commentDtoSaved = jacksonTester.write(commentDto);

        assertThat(commentDtoSaved).hasJsonPath("$.text");
        assertThat(commentDtoSaved).hasJsonPath("$.createdOn");
        assertThat(commentDtoSaved).hasJsonPath("$.user");
        assertThat(commentDtoSaved).hasJsonPath("$.event");

        assertThat(commentDtoSaved).extractingJsonPathStringValue("$.text").isEqualTo(commentDto.getText());
        assertThat(commentDtoSaved).hasJsonPathValue("$.createdOn");
        assertThat(commentDtoSaved).hasJsonPathValue("$.user");
        assertThat(commentDtoSaved).hasJsonPathValue("$.event");
    }

}
