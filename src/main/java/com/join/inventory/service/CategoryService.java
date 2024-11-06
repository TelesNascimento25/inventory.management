package com.join.inventory.service;

import java.util.List;
import java.util.stream.Collectors;

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

        Category category = new Category();
        category.setName(createCategoryRequest.getName());
        category.setDescription(createCategoryRequest.getDescription());
        categoryRepository.save(category);

        return new CreateCategoryResponse(category.getId(), category.getName(), category.getDescription());
    }

    public CreateCategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest updateCategoryRequest) {
        Category category = findCategoryById(categoryId);
        category.setName(updateCategoryRequest.getName());
        category.setDescription(updateCategoryRequest.getDescription());
        categoryRepository.save(category);

        return new CreateCategoryResponse(category.getId(), category.getName(), category.getDescription());
    }

    public void deleteCategory(Long categoryId) {
        Category category = findCategoryById(categoryId);
        categoryRepository.delete(category);
    }

    public List<CreateCategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CreateCategoryResponse(category.getId(), category.getName(), category.getDescription()))
                .collect(Collectors.toList());
    }

    public CreateCategoryResponse getCategoryById(Long categoryId) {
        Category category = findCategoryById(categoryId);
        return new CreateCategoryResponse(category.getId(), category.getName(), category.getDescription());
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }
}