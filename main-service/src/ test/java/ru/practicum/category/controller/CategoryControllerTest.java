package ru.practicum.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"db.name=test"})
public class CategoryControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    private CategoryDto categoryDto;
    private NewCategoryDto newCategoryDto;

    @BeforeEach
    void setup() {
        newCategoryDto = new NewCategoryDto("Meeting new friends");
        categoryDto = new CategoryDto("Meeting new friends", 1L);
    }

    @Test
    void get_shouldReturnStatusOk() throws Exception {
        List<CategoryDto> categoryDtoList = List.of(categoryDto);

        when(categoryService.getAll(any(), any())).thenReturn(categoryDtoList);

        mockMvc.perform(get("/categories")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(categoryDto.getId().intValue())))
                .andExpect(jsonPath("$.[0].name", is(categoryDto.getName())));

        verify(categoryService, times(1)).getAll(any(), any());
    }

    @Test
    void getById_shouldReturnStatusOk() throws Exception {
        when(categoryService.get(any())).thenReturn(categoryDto);

        mockMvc.perform(get("/categories/{categoryId}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoryDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(categoryDto.getName())));

        verify(categoryService, times(1)).get(any());
    }

}