package com.join.inventory.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.join.inventory.exception.CategoryNotFoundException;
import com.join.inventory.exception.DuplicateCategoryException;
import com.join.inventory.model.Category;
import com.join.inventory.model.dto.CreateCategoryRequest;
import com.join.inventory.model.dto.CategoryDTO;
import com.join.inventory.model.dto.UpdateCategoryRequest;
import com.join.inventory.repository.CategoryRepository;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryDTO createCategory(CreateCategoryRequest createCategoryRequest) {
        if (categoryRepository.existsByName(createCategoryRequest.getName())) {
            throw new DuplicateCategoryException(createCategoryRequest.getName());
        }

        var category = Category.builder()
            .name(createCategoryRequest.getName())
            .description(createCategoryRequest.getDescription())
        .build();

        categoryRepository.save(category);

        return CategoryDTO.fromCategory(category);
    }

    public CategoryDTO updateCategory(Long categoryId, UpdateCategoryRequest updateCategoryRequest) {
        var category = findCategoryById(categoryId);
        category = Category.builder()
            .id(category.getId())
            .name(StringUtils.defaultIfBlank(updateCategoryRequest.getName().trim(), category.getName()))
            .description(StringUtils.defaultIfBlank(updateCategoryRequest.getDescription().trim(), category.getDescription()))
        .build();

        category = categoryRepository.save(category);

        return CategoryDTO.fromCategory(category);
    }

    public void deleteCategory(Long categoryId) {
        Category category = findCategoryById(categoryId);
        categoryRepository.delete(category);
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryDTO::fromCategory)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long categoryId) {
        var category = findCategoryById(categoryId);
        return CategoryDTO.fromCategory(category);
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }
}