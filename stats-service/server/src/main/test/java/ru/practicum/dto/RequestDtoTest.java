package ru.practicum.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.RequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JsonTest
public class RequestDtoTest {

    @Autowired
    private JacksonTester<RequestDto> jacksonTester;

    private RequestDto requestDto;

    @BeforeEach
    void setup() {
        requestDto = new RequestDto(1, "ewm-main-service", "/events/1", "192.163.0.1", LocalDateTime.now());
    }

    @Test
    void serialize() throws Exception {
        JsonContent<RequestDto> requestDtoSaved = jacksonTester.write(requestDto);

        assertThat(requestDtoSaved).hasJsonPath("$.id");
        assertThat(requestDtoSaved).hasJsonPath("$.app");
        assertThat(requestDtoSaved).hasJsonPath("$.uri");
        assertThat(requestDtoSaved).hasJsonPath("$.ip");
        assertThat(requestDtoSaved).hasJsonPath("$.timestamp");

        assertThat(requestDtoSaved).extractingJsonPathNumberValue("$.id").isEqualTo(requestDto.getId());
        assertThat(requestDtoSaved).extractingJsonPathStringValue("$.app").isEqualTo(requestDto.getApp());
        assertThat(requestDtoSaved).extractingJsonPathStringValue("$.uri").isEqualTo(requestDto.getUri());
        assertThat(requestDtoSaved).extractingJsonPathStringValue("$.ip").isEqualTo(requestDto.getIp());

        assertThat(requestDtoSaved).hasJsonPathValue("$.timestamp");
    }

    @Test
    void deserialize() throws Exception {
        String json = "{\"app\":\"main-service\"," +
                    "\"uri\":\"/events/1\"," +
                    "\"ip\":\"192.163.0.1\"}";

        RequestDto requestDto = jacksonTester.parseObject(json);

        assertNotNull(requestDto);

        assertThat(requestDto.getApp()).isEqualTo(requestDto.getApp());
        assertThat(requestDto.getUri()).isEqualTo(requestDto.getUri());
        assertThat(requestDto.getIp()).isEqualTo(requestDto.getIp());
    }
}
