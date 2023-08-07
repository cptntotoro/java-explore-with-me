package ru.practicum.category.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CategoryMapperTest {

    @Test
    void newCategoryDtoToCategory() {
        NewCategoryDto categoryDto = new NewCategoryDto("Meeting new friends");
        Category category = CategoryMapper.INSTANCE.newCategoryDtoToCategory(categoryDto);

        assertNotNull(category);
        assertEquals(category.getName(), categoryDto.getName());
    }

    @Test
    void categoryToCategoryDto() {
        Category category = new Category(1L, "Meeting new friends");
        CategoryDto categoryDto = CategoryMapper.INSTANCE.categoryToCategoryDto(category);

        assertNotNull(categoryDto);
        assertEquals(category.getId(), categoryDto.getId());
        assertEquals(category.getName(), categoryDto.getName());
    }
}
