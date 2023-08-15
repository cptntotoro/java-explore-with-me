package ru.practicum.comment.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class NewCommentDtoTest {

    @Autowired
    private JacksonTester<NewCommentDto> jacksonTester;

    @Test
    void testDeserialize() throws Exception {
        String jsonValue = "{\"text\": \"Do they serve lactose-free latte there?\"}";
        NewCommentDto expectedCommentDto = new NewCommentDto("Do they serve lactose-free latte there?");

        assertThat(jacksonTester.parse(jsonValue)).usingRecursiveComparison().isEqualTo(expectedCommentDto);
    }
}
