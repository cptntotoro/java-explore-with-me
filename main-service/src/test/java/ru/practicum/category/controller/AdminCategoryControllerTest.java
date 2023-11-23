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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminCategoryController.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"db.name=test"})
public class AdminCategoryControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    private NewCategoryDto newCategoryDto;
    private CategoryDto categoryDto;

    @BeforeEach
    void setup() {
        newCategoryDto = new CategoryDto("Meeting new friends.", 1L);
        categoryDto = new CategoryDto("Meeting new friends.", 1L);
    }

    @Test
    void add_shouldReturnCategoryDtoAndStatusCreated() throws Exception {
        when(categoryService.add(any())).thenReturn(categoryDto);

        mockMvc.perform(post("/admin/categories")
                        .content(mapper.writeValueAsString(newCategoryDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(categoryDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(categoryDto.getName())));

        verify(categoryService, times(1)).add(any());
    }

    @Test
    void update_shouldReturnCategoryDto() throws Exception {
        when(categoryService.update(any(), any())).thenReturn(categoryDto);

        mockMvc.perform(patch("/admin/categories/{catId}", 1)
                        .content(mapper.writeValueAsString(newCategoryDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoryDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(categoryDto.getName())));

        verify(categoryService, times(1)).update(any(), any());
    }

    @Test
    void delete_shouldReturnStatusNoContent() throws Exception {
        doNothing().when(categoryService).delete(any());

        mockMvc.perform(delete("/admin/categories/{catId}", 1))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).delete(any());
    }

}
