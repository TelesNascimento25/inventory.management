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
    private MockMvc mock_mvc;

    @MockBean
    private CategoryService category_service;

    @Autowired
    private ObjectMapper object_mapper;

    @Test
    void test_create_category() throws Exception {
        var create_category_request = new CreateCategoryRequest("Category Test", "Description Test");

        CategoryDTO category_dto = new CategoryDTO(1L, "Category Test", "Description Test");

        when(category_service.createCategory(any())).thenReturn(category_dto);

        mock_mvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(object_mapper.writeValueAsString(create_category_request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Category Test"));

        verify(category_service, times(1)).createCategory(any());
    }

    @Test
    void test_update_category() throws Exception {
        var update_category_request = new UpdateCategoryRequest("Updated Category", "Description Test");

        CategoryDTO category_dto = new CategoryDTO(1L, "Updated Category", "Description Test");

        when(category_service.updateCategory(anyLong(), any())).thenReturn(category_dto);

        mock_mvc.perform(put("/categories/{categoryId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(object_mapper.writeValueAsString(update_category_request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Category"));

        verify(category_service, times(1)).updateCategory(anyLong(), any());
    }

    @Test
    void test_delete_category() throws Exception {
        doNothing().when(category_service).deleteCategory(anyLong());

        mock_mvc.perform(delete("/categories/{categoryId}", 1L))
                .andExpect(status().isNoContent());

        verify(category_service, times(1)).deleteCategory(anyLong());
    }

    @Test
    void test_get_all_categories() throws Exception {
        List<CategoryDTO> category_list = List.of(
                new CategoryDTO(1L, "Category Test 1", "Description Test 1"),
                new CategoryDTO(2L, "Category Test 2", "Description Test 2")
        );

        when(category_service.getAllCategories()).thenReturn(category_list);

        mock_mvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].name").value("Category Test 2"));

        verify(category_service, times(1)).getAllCategories();
    }

    @Test
    void test_get_category_by_id() throws Exception {
        CategoryDTO category_dto = new CategoryDTO(1L, "Category Test", "Description Test");

        when(category_service.getCategoryById(anyLong())).thenReturn(category_dto);

        mock_mvc.perform(get("/categories/{categoryId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Category Test"));

        verify(category_service, times(1)).getCategoryById(anyLong());
    }
}
