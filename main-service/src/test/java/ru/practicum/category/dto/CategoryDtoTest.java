package ru.practicum.category.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CategoryDtoTest {

    @Autowired
    private JacksonTester<CategoryDto> jacksonTester;

    @Test
    void testSerialize() throws Exception {
        CategoryDto categoryDto = new CategoryDto("Meeting new friends.", 1L);
        JsonContent<CategoryDto> categoryDtoSaved = jacksonTester.write(categoryDto);

        assertThat(categoryDtoSaved).hasJsonPath("$.id");
        assertThat(categoryDtoSaved).hasJsonPath("$.name");

        assertThat(categoryDtoSaved).extractingJsonPathNumberValue("$.id").isEqualTo(categoryDto.getId().intValue());
        assertThat(categoryDtoSaved).extractingJsonPathStringValue("$.name").isEqualTo(categoryDto.getName());
    }

    @Test
    void testDeserialize() throws Exception {
        String jsonValue = "{\"name\":\"Meeting new friends.\",\"id\":\"1\"}";
        CategoryDto expectedCategoryDto = new CategoryDto("Meeting new friends.", 1L);

        assertThat(jacksonTester.parse(jsonValue)).usingRecursiveComparison().isEqualTo(expectedCategoryDto);
    }
}
