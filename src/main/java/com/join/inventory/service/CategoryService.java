package com.join.inventory.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.join.inventory.exception.CategoryNotFoundException;
import com.join.inventory.exception.DuplicateCategoryException;
import com.join.inventory.model.Category;
import com.join.inventory.model.dto.CreateCategoryRequest;
import com.join.inventory.model.dto.CreateCategoryResponse;
import com.join.inventory.model.dto.UpdateCategoryRequest;
import com.join.inventory.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CreateCategoryResponse createCategory(CreateCategoryRequest createCategoryRequest) {
        if (categoryRepository.existsByName(createCategoryRequest.getName())) {
            throw new DuplicateCategoryException(createCategoryRequest.getName());
        }

        var category = Category.builder()
            .name(createCategoryRequest.getName())
            .description(createCategoryRequest.getDescription())
        .build();

        categoryRepository.save(category);

        return CreateCategoryResponse.fromCategory(category);
    }

    public CreateCategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest updateCategoryRequest) {
        var category = findCategoryById(categoryId);
        category = Category.builder()
            .id(category.getId())
            .name(StringUtils.defaultIfBlank(updateCategoryRequest.getName().trim(), category.getName()))
            .description(StringUtils.defaultIfBlank(updateCategoryRequest.getDescription().trim(), category.getDescription()))
        .build();

        category = categoryRepository.save(category);

        return CreateCategoryResponse.fromCategory(category);
    }

    public void deleteCategory(Long categoryId) {
        Category category = findCategoryById(categoryId);
        categoryRepository.delete(category);
    }

    public List<CreateCategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CreateCategoryResponse::fromCategory)
                .collect(Collectors.toList());
    }

    public CreateCategoryResponse getCategoryById(Long categoryId) {
        Category category = findCategoryById(categoryId);
        return CreateCategoryResponse.fromCategory(category);
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }
}