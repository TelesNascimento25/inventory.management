package com.join.inventory.service;

import com.join.inventory.exception.DuplicateProductException;
import com.join.inventory.exception.ProductNotFoundException;
import com.join.inventory.model.Product;
import com.join.inventory.model.dto.CreateProductRequest;
import com.join.inventory.model.dto.ProductDTO;
import com.join.inventory.model.dto.UpdateProductRequest;
import com.join.inventory.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductDTO createProduct(CreateProductRequest createProductRequest) {
        if (productRepository.existsByName(createProductRequest.getName())) {
            throw new DuplicateProductException(createProductRequest.getName());
        }

        var product = Product.builder()
                .name(createProductRequest.getName())
                .description(createProductRequest.getDescription())
                .price(createProductRequest.getPriceInCents().divide(BigDecimal.valueOf(100), RoundingMode.UNNECESSARY))
                .categoryId(createProductRequest.getCategoryId())
                .build();

        product = productRepository.save(product);

        return ProductDTO.fromProduct(product);
    }

    public ProductDTO getProductDetails(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return ProductDTO.fromProduct(product);
    }

    public Page<ProductDTO> getProductsByCategory(Long categoryId, int page, int size, Sort sort, String filterText) {
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage;

        if (filterText != null && !filterText.isEmpty()) {
            productPage = productRepository.findByCategoryIdAndNameContainingIgnoreCaseOrCategoryIdAndDescriptionContainingIgnoreCase(
                    categoryId, filterText, categoryId, filterText, pageable);
        } else {
            productPage = productRepository.findByCategoryId(categoryId, pageable);
        }

        if (productPage.isEmpty()) {
            throw new ProductNotFoundException(null);
        }

        return productPage.map(ProductDTO::fromProduct);
    }

    public Page<ProductDTO> getAllProducts(int page, int size, Sort sort, String filterText) {
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage;

        if (filterText != null && !filterText.isEmpty()) {
            productPage = productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(filterText, filterText, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }
        return productPage.map(ProductDTO::fromProduct);
    }


    private List<Sort.Order> parseSortParameters(String[] sortParams) {
        List<Sort.Order> orders = new ArrayList<>();
        for (String param : sortParams) {
            String[] sortSplit = param.split(",");

            for (int i = 0; i < sortSplit.length; i++) {
                sortSplit[i] = sortSplit[i].trim();
            }

            if (sortSplit.length == 2) {
                orders.add(new Sort.Order(Sort.Direction.fromString(sortSplit[1].toUpperCase()), sortSplit[0]));
            } else if (sortSplit.length == 1) {
                orders.add(new Sort.Order(Sort.Direction.ASC, sortSplit[0]));
            } else {
                throw new IllegalArgumentException("Invalid sort parameter: " + param + ". Expected format: 'property,direction'.");
            }
        }
        return orders;
    }

    public ProductDTO updateProduct(Long productId, UpdateProductRequest updateProductRequest) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        product = Product.builder()
                .id(product.getId())
                .name(StringUtils.defaultIfBlank(updateProductRequest.getName().trim(), product.getName()))
                .description(StringUtils.defaultIfBlank(updateProductRequest.getDescription().trim(), product.getDescription()))
                .price(Optional.ofNullable(updateProductRequest.getPriceInCents())
                        .map(priceInCents -> priceInCents.divide(BigDecimal.valueOf(100), RoundingMode.HALF_EVEN))
                        .orElse(product.getPrice()))
                .categoryId(Optional.ofNullable(updateProductRequest.getCategoryId()).orElse(product.getCategoryId()))
                .build();

        product = productRepository.save(product);

        return ProductDTO.fromProduct(product);
    }

    public void deleteProduct(Long productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        productRepository.delete(product);
    }
}
