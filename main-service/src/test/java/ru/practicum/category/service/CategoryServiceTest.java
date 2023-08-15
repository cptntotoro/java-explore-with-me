package ru.practicum.category.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapperImpl;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.exception.RequestConflictException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {"db.name=test"})
public class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private EventRepository eventRepository;

    @Spy
    private CategoryMapperImpl categoryMapper;

    private Category category;
    private NewCategoryDto newCategoryDto;

    @BeforeEach
    void setup() {
        category = new Category(1L, "Meeting new friends.");
        newCategoryDto = new NewCategoryDto("Meeting new friends.");
    }

    @Test
    void add_shouldReturnCategoryDto() {
        Mockito.when(categoryRepository.save(any()))
                .thenReturn(category);

        CategoryDto categoryDtoOutput = categoryService.add(newCategoryDto);

        assertAll(
                () -> assertEquals(categoryDtoOutput.getId(), category.getId()),
                () -> assertEquals(categoryDtoOutput.getName(), category.getName())
        );

        verify(categoryRepository, atMostOnce()).saveAndFlush(any());
    }

    @Test
    void update_shouldReturnCategoryDto() {
        Long categoryId = category.getId();
        NewCategoryDto updateCategoryDto = new NewCategoryDto("Meeting new people.");

        Mockito.when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.ofNullable(category));

        Category categoryToSave = category;
        categoryToSave.setName(updateCategoryDto.getName());

        Mockito.when(categoryRepository.save(any()))
                .thenReturn(categoryToSave);

        CategoryDto savedCategoryDto = categoryService.update(categoryId, updateCategoryDto);

        assertAll(
                () -> assertEquals(savedCategoryDto.getId(), categoryId),
                () -> assertEquals(savedCategoryDto.getName(), updateCategoryDto.getName())
        );

        verify(categoryRepository, atMostOnce()).saveAndFlush(any());
    }

    @Test
    void update_throwsObjectNotFoundException() {
        Long categoryId = category.getId();
        NewCategoryDto updateCategoryDto = new NewCategoryDto("Meeting new people.");

        Mockito.when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> categoryService.update(categoryId, updateCategoryDto));
    }

    @Test
    void get_shouldReturnCategoryDto() {
        Long categoryId = category.getId();

        Mockito.when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.ofNullable(category));

        CategoryDto receivedCategoryDto = categoryService.get(categoryId);

        assertAll(
                () -> assertEquals(receivedCategoryDto.getId(), categoryId),
                () -> assertEquals(receivedCategoryDto.getName(), category.getName())
        );

        verify(categoryRepository, atMostOnce()).saveAndFlush(any());
    }

    @Test
    void get_throwsObjectNotFoundException() {
        Long categoryId = category.getId();

        Mockito.when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> categoryService.get(categoryId));
    }

    @Test
    void delete_throwsRequestConflictException() {
        Mockito.when(eventRepository.existsByCategoryId(any()))
                .thenReturn(true);

        assertThrows(RequestConflictException.class, () -> categoryService.delete(category.getId()));
        verify(categoryRepository, atMostOnce()).saveAndFlush(any());
    }

    @Test
    void getAll_shouldReturnListOfCategoryDto() {
        Page<Category> expectedList = new PageImpl<>(List.of(category));

        when(categoryRepository.findAll(any(Pageable.class)))
                .thenReturn(expectedList);

        List<CategoryDto> categoryDtoList = categoryService.getAll(0, 10);

        assertAll(
                () -> assertEquals(categoryDtoList.size(), 1),
                () -> assertEquals(categoryDtoList.get(0).getId(), category.getId())
        );

        verify(categoryRepository, atMostOnce()).saveAndFlush(any());
    }

    @Test
    void getAll_shouldReturnEmptyList() {
        when(categoryRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        List<CategoryDto> categoryDtoList = categoryService.getAll(0, 10);

        assertTrue(categoryDtoList.isEmpty());
    }

}