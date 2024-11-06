package com.join.inventory.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.join.inventory.exception.CategoryNotFoundException;
import com.join.inventory.exception.DuplicateProductException;
import com.join.inventory.exception.ProductNotFoundException;
import com.join.inventory.model.Product;
import com.join.inventory.model.dto.CreateProductRequest;
import com.join.inventory.model.dto.CreateProductResponse;
import com.join.inventory.model.dto.UpdateProductRequest;
import com.join.inventory.repository.CategoryRepository;
import com.join.inventory.repository.ProductsRepository;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductsRepository productRepository;
    private final CategoryRepository categoryRepository;

    public CreateProductResponse createProduct(CreateProductRequest createProductRequest) {
        if (productRepository.existsByName(createProductRequest.getName())) {
            throw new DuplicateProductException(createProductRequest.getName());
        }

        var category = categoryRepository.findById(createProductRequest.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(createProductRequest.getCategoryId()));

        var product = Product.builder()
            .name(createProductRequest.getName())
            .description(createProductRequest.getDescription())
            .price(createProductRequest.getPriceInCents().divide(BigDecimal.valueOf(100), RoundingMode.UNNECESSARY).doubleValue())
            .category(category)
        .build();

        product = productRepository.save(product);

        return CreateProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .categoryId(product.getCategory().getId())
        .build();
    }

    public CreateProductResponse getProductDetails(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return CreateProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .categoryId(product.getCategory().getId())
        .build();
    }

    public List<CreateProductResponse> getProductsByCategory(String category) {
        List<Product> products = productRepository.findByCategoryName(category);

        if (products.isEmpty()) {
            throw new ProductNotFoundException(null); 
        }

        return products.stream()
                .map(CreateProductResponse::fromProduct)
                .collect(Collectors.toList());
    }

    public List<CreateProductResponse> getAllProducts() {
        var products = productRepository.findAll();

        return products.stream()
                .map(CreateProductResponse::fromProduct)
                .collect(Collectors.toList());
    }

    public CreateProductResponse updateProduct(Long productId, UpdateProductRequest updateProductRequest) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        var category = categoryRepository.findById(updateProductRequest.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(updateProductRequest.getCategoryId()));

        product = Product.builder()
            .id(product.getId())
            .name(StringUtils.defaultIfBlank(updateProductRequest.getName().trim(), product.getName()))
            .description(StringUtils.defaultIfBlank(updateProductRequest.getDescription().trim(), product.getDescription()))
            .price(Optional.ofNullable(updateProductRequest.getPrice()).orElse(product.getPrice()))
            .category(Optional.ofNullable(category).orElse(product.getCategory()))
        .build();

        productRepository.save(product);

        return CreateProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .categoryId(product.getCategory().getId())
        .build();
    }

    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        productRepository.delete(product);
    }
}
