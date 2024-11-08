package com.join.inventory.controller;

import com.join.inventory.model.dto.CategoryDTO;
import com.join.inventory.model.dto.CreateCategoryRequest;
import com.join.inventory.model.dto.UpdateCategoryRequest;
import com.join.inventory.service.CategoryService;
import com.join.inventory.util.Converter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final Converter converter;

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @Valid CreateCategoryRequest createCategoryRequest) {
        var createdCategory = categoryService.createCategory(createCategoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId,
                                                      @RequestBody @Valid UpdateCategoryRequest updateCategoryRequest) {
        var updatedCategory = categoryService.updateCategory(categoryId, updateCategoryRequest);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort,
            @RequestParam(value = "filterText", required = false) String filterText) {

        try {
            Sort sortOrder = converter.convertToSort(sort);

            Page<CategoryDTO> categoryPage = categoryService.getCategories(page, size, sortOrder, filterText);

            Map<String, Object> response = new HashMap<>();
            response.put("categories", categoryPage.getContent());
            response.put("currentPage", categoryPage.getNumber());
            response.put("totalItems", categoryPage.getTotalElements());
            response.put("totalPages", categoryPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long categoryId) {
        var category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }
}
