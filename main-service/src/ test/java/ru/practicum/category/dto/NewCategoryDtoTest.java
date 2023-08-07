package ru.practicum.category.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class NewCategoryDtoTest {

    @Autowired
    private JacksonTester<NewCategoryDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {
        NewCategoryDto categoryDto = new NewCategoryDto("Meeting new friends.");
        JsonContent<NewCategoryDto> categoryDtoSaved = jacksonTester.write(categoryDto);

        assertThat(categoryDtoSaved).hasJsonPath("$.name");
        assertThat(categoryDtoSaved).extractingJsonPathStringValue("$.name").isEqualTo(categoryDto.getName());
    }

    @Test
    void testDeserialize() throws Exception {
        String jsonValue = "{\"name\":\"Meeting new friends.\"}";
        NewCategoryDto expectedCategoryDto = new NewCategoryDto("Meeting new friends.");

        assertThat(jacksonTester.parse(jsonValue)).usingRecursiveComparison().isEqualTo(expectedCategoryDto);
    }
}