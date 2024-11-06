package com.join.inventory.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.join.inventory.model.dto.CreateCategoryRequest;
import com.join.inventory.model.dto.CreateCategoryResponse;
import com.join.inventory.model.dto.UpdateCategoryRequest;
import com.join.inventory.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CreateCategoryResponse> createCategory(@RequestBody @Valid CreateCategoryRequest createCategoryRequest) {
        CreateCategoryResponse createdCategory = categoryService.createCategory(createCategoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CreateCategoryResponse> updateCategory(@PathVariable Long categoryId,
                                                                 @RequestBody @Valid UpdateCategoryRequest updateCategoryRequest) {
        CreateCategoryResponse updatedCategory = categoryService.updateCategory(categoryId, updateCategoryRequest);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CreateCategoryResponse>> getAllCategories() {
        List<CreateCategoryResponse> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CreateCategoryResponse> getCategoryById(@PathVariable Long categoryId) {
        CreateCategoryResponse category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }
}
