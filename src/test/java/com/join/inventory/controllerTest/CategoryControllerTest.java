package com.join.inventory.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.join.inventory.controller.CategoryController;
import com.join.inventory.model.dto.CategoryDTO;
import com.join.inventory.model.dto.CreateCategoryRequest;
import com.join.inventory.model.dto.UpdateCategoryRequest;
import com.join.inventory.service.CategoryService;
import com.join.inventory.util.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

    private ObjectMapper objectMapper;
    @Autowired
    private Converter converter;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(new CategoryController(categoryService, converter)).build();
    }

    @Test
    public void test_Create_Category() throws Exception {
        CreateCategoryRequest request = new CreateCategoryRequest("Electronics", "All electronic items");
        CategoryDTO mockCategoryDTO = new CategoryDTO(1L, "Electronics", "All electronic items");

        when(categoryService.createCategory(any(CreateCategoryRequest.class))).thenReturn(mockCategoryDTO);

        mockMvc.perform(post("/categories")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Electronics"))
                .andExpect(jsonPath("$.description").value("All electronic items"));
    }

    @Test
    public void test_Update_Category() throws Exception {
        UpdateCategoryRequest request = new UpdateCategoryRequest("Updated Name", "Updated Description");
        CategoryDTO mockCategoryDTO = new CategoryDTO(1L, "Updated Name", "Updated Description");

        when(categoryService.updateCategory(eq(1L), any(UpdateCategoryRequest.class))).thenReturn(mockCategoryDTO);

        mockMvc.perform(put("/categories/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    public void test_Delete_Category() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_Get_Category_By_Id() throws Exception {
        CategoryDTO mockCategoryDTO = new CategoryDTO(1L, "Electronics", "All electronic items");

        when(categoryService.getCategoryById(1L)).thenReturn(mockCategoryDTO);

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"))
                .andExpect(jsonPath("$.description").value("All electronic items"));
    }
}
