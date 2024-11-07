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
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;
    private Category category;
    private CreateCategoryRequest createCategoryRequest;
    private UpdateCategoryRequest updateCategoryRequest;

        @BeforeEach
        void setUp() {
            category = Category.builder()
                    .id(1L)
                    .name("Category Test")
                    .description("Test Description")
                    .build();
            createCategoryRequest = new CreateCategoryRequest("Category Test", "Test Description");
            updateCategoryRequest = new UpdateCategoryRequest("Updated Category", "Updated Description");
        }

    @Test
    void testCreateCategoryIsSuccess() {
        when(categoryRepository.existsByName("Category Test")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("Category Test", "Test Description");

        CategoryDTO createdCategoryDTO = categoryService.createCategory(createCategoryRequest);

        assertNotNull(createdCategoryDTO);
        assertEquals("Category Test", createdCategoryDTO.getName());
        assertEquals("Test Description", createdCategoryDTO.getDescription());

        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);

        verify(categoryRepository).existsByName("Category Test");
        verify(categoryRepository).save(categoryCaptor.capture());

        Category capturedCategory = categoryCaptor.getValue();
        assertEquals("Category Test", capturedCategory.getName());
        assertEquals("Test Description", capturedCategory.getDescription());

        verify(categoryRepository, times(1)).save(capturedCategory);
    }

    @Test
        void testCreateCategoryThrowsDuplicateCategoryException() {
            when(categoryRepository.existsByName(anyString())).thenReturn(true);

            DuplicateCategoryException exception = assertThrows(DuplicateCategoryException.class, () -> {
                categoryService.createCategory(createCategoryRequest);
            });

            assertEquals("DUPLICATE_CATEGORY", exception.getMessage());
            verify(categoryRepository, times(1)).existsByName("Category Test");
            verify(categoryRepository, times(0)).save(any());
        }

    @Test
    void testUpdateCategoryIsSuccess() {
        category.setId(1L);
        UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest("Updated Category Test", "Updated Description");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDTO updatedCategoryDTO = categoryService.updateCategory(1L, updateCategoryRequest);

        assertNotNull(updatedCategoryDTO);
        assertEquals("Category Test", updatedCategoryDTO.getName());
        assertEquals("Test Description", updatedCategoryDTO.getDescription());

        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);

        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(categoryCaptor.capture());

        Category capturedCategory = categoryCaptor.getValue();
        assertEquals("Updated Category Test", capturedCategory.getName());
        assertEquals("Updated Description", capturedCategory.getDescription());

        verify(categoryRepository, times(1)).save(capturedCategory);
    }

    @Test
        void testUpdateCategoryCategoryNotFound() {
            when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

            CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
                categoryService.updateCategory(1L, updateCategoryRequest);
            });

            assertEquals("CATEGORY_NOT_FOUND", exception.getMessage());
            verify(categoryRepository, times(1)).findById(1L);
            verify(categoryRepository, times(0)).save(any());
        }

        @Test
        void testDeleteCategoryIsSuccess() {
            when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
            doNothing().when(categoryRepository).delete(any(Category.class));

            categoryService.deleteCategory(1L);

            verify(categoryRepository, times(1)).findById(1L);
            verify(categoryRepository, times(1)).delete(category);
        }

        @Test
        void testDeleteCategoryCategoryNotFound() {
            when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

            CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
                categoryService.deleteCategory(1L);
            });

            assertEquals("CATEGORY_NOT_FOUND", exception.getMessage());
            verify(categoryRepository, times(1)).findById(1L);
            verify(categoryRepository, times(0)).delete(any());
        }

        @Test
        void testGetAllCategoriesIsSuccess() {
            when(categoryRepository.findAll()).thenReturn(List.of(category));

            List<CategoryDTO> categories = categoryService.getAllCategories();

            assertNotNull(categories);
            assertEquals(1, categories.size());
            assertEquals("Category Test", categories.get(0).getName());
            verify(categoryRepository, times(1)).findAll();
        }

        @Test
        void testGetCategoryByIdIsSuccess() {
            when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

            CategoryDTO categoryDTO = categoryService.getCategoryById(1L);

            assertNotNull(categoryDTO);
            assertEquals("Category Test", categoryDTO.getName());
            verify(categoryRepository, times(1)).findById(1L);
        }

        @Test
        void testGetCategoryByIdCategoryNotFound() {
            when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

            CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
                categoryService.getCategoryById(1L);
            });

            assertEquals("CATEGORY_NOT_FOUND", exception.getMessage());
            verify(categoryRepository, times(1)).findById(1L);
        }
    }
