package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> get(@RequestParam(defaultValue = "0") Integer from,
                                 @RequestParam(defaultValue = "10") Integer size) {
        log.info("Calling GET: /categories with 'from': {}, 'size': {}", from, size);
        return categoryMapper.listCategoryToListCategoryDto(categoryService.getAll(from, size));
    }

    @GetMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto get(@PathVariable Long categoryId)  {
        log.info("Calling GET: /categories/{categoryId} with 'categoryId': {}", categoryId);
        return categoryMapper.categoryToCategoryDto(categoryService.get(categoryId));
    }
}