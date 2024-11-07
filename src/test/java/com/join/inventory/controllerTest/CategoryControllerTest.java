package com.join.inventory.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.join.inventory.model.dto.CategoryDTO;
import com.join.inventory.model.dto.CreateCategoryRequest;
import com.join.inventory.model.dto.UpdateCategoryRequest;
import com.join.inventory.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateCategory() throws Exception {
        var createCategoryRequest = new CreateCategoryRequest("Category Test", "Description Test");

        CategoryDTO categoryDTO = new CategoryDTO(1L, "Category Test", "Description Test");

        when(categoryService.createCategory(any())).thenReturn(categoryDTO);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCategoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Category Test"));

        verify(categoryService, times(1)).createCategory(any());
    }

    @Test
    void testUpdateCategory() throws Exception {
        var updateCategoryRequest = new UpdateCategoryRequest("Updated Category", "Description Test");

        CategoryDTO categoryDTO = new CategoryDTO(1L, "Updated Category", "Description Test");

        when(categoryService.updateCategory(anyLong(), any())).thenReturn(categoryDTO);

        mockMvc.perform(put("/categories/{categoryId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCategoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Category"));

        verify(categoryService, times(1)).updateCategory(anyLong(), any());
    }

    @Test
    void testDeleteCategory() throws Exception {
        doNothing().when(categoryService).deleteCategory(anyLong());

        mockMvc.perform(delete("/categories/{categoryId}", 1L))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteCategory(anyLong());
    }

    @Test
    void testGetAllCategories() throws Exception {
        List<CategoryDTO> categoryList = List.of(
                new CategoryDTO(1L, "Category Test 1", "Description Test 1"),
                new CategoryDTO(2L, "Category Test 2", "Description Test 2")
        );

        when(categoryService.getAllCategories()).thenReturn(categoryList);

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].name").value("Category Test 2"));

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void testGetCategoryById() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO(1L, "Category Test", "Description Test");

        when(categoryService.getCategoryById(anyLong())).thenReturn(categoryDTO);

        mockMvc.perform(get("/categories/{categoryId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Category Test"));

        verify(categoryService, times(1)).getCategoryById(anyLong());
    }
}
