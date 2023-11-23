package ru.practicum.comment.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.comment.model.CommentStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentStatusUpdateRequestTest {

    @Autowired
    private JacksonTester<CommentStatusUpdateRequest> jacksonTester;

    @Test
    void testDeserialize() throws Exception {
        String jsonValue = "{\"commentIds\": [1, 2],\"status\": \"PUBLISHED\"}";
        CommentStatusUpdateRequest expectedUpdateRequest = new CommentStatusUpdateRequest(List.of(1L, 2L), CommentStatus.PUBLISHED);

        assertThat(jacksonTester.parse(jsonValue)).usingRecursiveComparison().isEqualTo(expectedUpdateRequest);
    }
}
