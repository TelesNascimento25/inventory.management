package com.join.inventory.serviceTest;

import com.join.inventory.exception.CategoryNotFoundException;
import com.join.inventory.exception.DuplicateCategoryException;
import com.join.inventory.model.Category;
import com.join.inventory.model.dto.CategoryDTO;
import com.join.inventory.model.dto.CreateCategoryRequest;
import com.join.inventory.model.dto.UpdateCategoryRequest;
import com.join.inventory.repository.CategoryRepository;
import com.join.inventory.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository category_repository;

    @InjectMocks
    private CategoryService category_service;
    private Category category;
    private CreateCategoryRequest create_category_request;
    private UpdateCategoryRequest update_category_request;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1L)
                .name("Category Test")
                .description("Test Description")
                .build();
        create_category_request = new CreateCategoryRequest("Category Test", "Test Description");
        update_category_request = new UpdateCategoryRequest("Updated Category", "Updated Description");
    }

    @Test
    void test_create_category_is_success() {
        when(category_repository.existsByName("Category Test")).thenReturn(false);
        when(category_repository.save(any(Category.class))).thenReturn(category);

        CreateCategoryRequest create_category_request = new CreateCategoryRequest("Category Test", "Test Description");

        CategoryDTO created_category_dto = category_service.createCategory(create_category_request);

        assertNotNull(created_category_dto);
        assertEquals("Category Test", created_category_dto.getName());
        assertEquals("Test Description", created_category_dto.getDescription());

        ArgumentCaptor<Category> category_captor = ArgumentCaptor.forClass(Category.class);

        verify(category_repository).existsByName("Category Test");
        verify(category_repository).save(category_captor.capture());

        Category captured_category = category_captor.getValue();
        assertEquals("Category Test", captured_category.getName());
        assertEquals("Test Description", captured_category.getDescription());

        verify(category_repository, times(1)).save(captured_category);
    }

    @Test
    void test_create_category_throws_duplicate_category_exception() {
        when(category_repository.existsByName(anyString())).thenReturn(true);

        DuplicateCategoryException exception = assertThrows(DuplicateCategoryException.class, () -> {
            category_service.createCategory(create_category_request);
        });

        assertEquals("DUPLICATE_CATEGORY", exception.getMessage());
        verify(category_repository, times(1)).existsByName("Category Test");
        verify(category_repository, times(0)).save(any());
    }

    @Test
    void test_update_category_is_success() {
        category.setId(1L);
        UpdateCategoryRequest update_category_request = new UpdateCategoryRequest("Updated Category Test", "Updated Description");

        when(category_repository.findById(1L)).thenReturn(Optional.of(category));
        when(category_repository.save(any(Category.class))).thenReturn(category);

        CategoryDTO updated_category_dto = category_service.updateCategory(1L, update_category_request);

        assertNotNull(updated_category_dto);
        assertEquals("Category Test", updated_category_dto.getName());
        assertEquals("Test Description", updated_category_dto.getDescription());

        ArgumentCaptor<Category> category_captor = ArgumentCaptor.forClass(Category.class);

        verify(category_repository).findById(1L);
        verify(category_repository).save(category_captor.capture());

        Category captured_category = category_captor.getValue();
        assertEquals("Updated Category Test", captured_category.getName());
        assertEquals("Updated Description", captured_category.getDescription());

        verify(category_repository, times(1)).save(captured_category);
    }

    @Test
    void test_update_category_category_not_found() {
        when(category_repository.findById(1L)).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            category_service.updateCategory(1L, update_category_request);
        });

        assertEquals("CATEGORY_NOT_FOUND", exception.getMessage());
        verify(category_repository, times(1)).findById(1L);
        verify(category_repository, times(0)).save(any());
    }

    @Test
    void test_delete_category_is_success() {
        when(category_repository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(category_repository).delete(any(Category.class));

        category_service.deleteCategory(1L);

        verify(category_repository, times(1)).findById(1L);
        verify(category_repository, times(1)).delete(category);
    }

    @Test
    void test_delete_category_category_not_found() {
        when(category_repository.findById(1L)).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            category_service.deleteCategory(1L);
        });

        assertEquals("CATEGORY_NOT_FOUND", exception.getMessage());
        verify(category_repository, times(1)).findById(1L);
        verify(category_repository, times(0)).delete(any());
    }

    @Test
    void test_get_all_categories_is_success() {
        when(category_repository.findAll()).thenReturn(List.of(category));

        List<CategoryDTO> categories = category_service.getAllCategories();

        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals("Category Test", categories.get(0).getName());
        verify(category_repository, times(1)).findAll();
    }

    @Test
    void test_get_category_by_id_is_success() {
        when(category_repository.findById(1L)).thenReturn(Optional.of(category));

        CategoryDTO category_dto = category_service.getCategoryById(1L);

        assertNotNull(category_dto);
        assertEquals("Category Test", category_dto.getName());
        verify(category_repository, times(1)).findById(1L);
    }

    @Test
    void test_get_category_by_id_category_not_found() {
        when(category_repository.findById(1L)).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            category_service.getCategoryById(1L);
        });

        assertEquals("CATEGORY_NOT_FOUND", exception.getMessage());
        verify(category_repository, times(1)).findById(1L);
    }
}
