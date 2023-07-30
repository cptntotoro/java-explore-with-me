package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto get(Long categoryId);

    CategoryDto add(NewCategoryDto body);

    void delete(Long catId);

    CategoryDto update(Long categoryId, NewCategoryDto newCategoryDto);
}
