package ru.practicum.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.RequestOutputDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class RequestOutputDtoTest {

    @Autowired
    private JacksonTester<RequestOutputDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {
        RequestOutputDto requestOutputDto = new RequestOutputDto("ewm-main-service", "/events/1", 2L);

        JsonContent<RequestOutputDto> requestOutputDtoSaved = jacksonTester.write(requestOutputDto);

        assertThat(requestOutputDtoSaved).hasJsonPath("$.app");
        assertThat(requestOutputDtoSaved).hasJsonPath("$.uri");
        assertThat(requestOutputDtoSaved).hasJsonPath("$.hits");

        assertThat(requestOutputDtoSaved).extractingJsonPathStringValue("$.app").isEqualTo(requestOutputDto.getApp());
        assertThat(requestOutputDtoSaved).extractingJsonPathStringValue("$.uri").isEqualTo(requestOutputDto.getUri());
        assertThat(requestOutputDtoSaved).extractingJsonPathNumberValue("$.hits").isEqualTo(requestOutputDto.getHits().intValue());
    }
}
