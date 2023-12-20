package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.exception.RequestConflictException;
import ru.practicum.exception.SQLConstraintViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Category add(NewCategoryDto newCategoryDto) {

        Category category = categoryMapper.newCategoryDtoToCategory(newCategoryDto);

        try {
            category = categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new SQLConstraintViolationException("Category with name = " + newCategoryDto.getName() + " already exists.");
        }

        return category;
    }

    @Override
    public Category update(Long catId, NewCategoryDto categoryDto) {

        Category category = categoryRepository.findById(catId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Category with id = " + catId + " doesn't exist.");
        });

        category.setName(categoryDto.getName());

        try {
            category = categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new SQLConstraintViolationException("Category with name = " + categoryDto.getName() + " already exists.");
        }

        return category;
    }

    @Override
    public Category get(Long catId) {

        return categoryRepository.findById(catId).orElseThrow(() -> {
            throw new ObjectNotFoundException("Category with id = " + catId + " doesn't exist.");
        });
    }

    @Override
    public void delete(Long catId) {

        if (eventRepository.existsByCategoryId(catId)) {
            throw new RequestConflictException("Failed to delete category. It must not be assigned to any event.");
        }

        try {
            categoryRepository.deleteById(catId);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("Category with id = " + catId + " doesn't exist.");
        }
    }

    @Override
    public List<Category> getAll(Integer from, Integer size) {

        Sort sort = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(from / size, size, sort);

        List<Category> categories = categoryRepository.findAll(pageable).stream()
                .collect(Collectors.toList());

        return (!categories.isEmpty()) ? categories : new ArrayList<>();
    }
}