package com.join.inventory.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.join.inventory.exception.DuplicateProductException;
import com.join.inventory.exception.ProductNotFoundException;
import com.join.inventory.model.Product;
import com.join.inventory.model.dto.CreateProductRequest;
import com.join.inventory.model.dto.ProductDTO;
import com.join.inventory.model.dto.UpdateProductRequest;
import com.join.inventory.repository.ProductRepository;

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

    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);

        if (products.isEmpty()) {
            throw new ProductNotFoundException(null); 
        }

        return products.stream()
                .map(ProductDTO::fromProduct)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getAllProducts() {
        var products = productRepository.findAll();

        return products.stream()
                .map(ProductDTO::fromProduct)
                .collect(Collectors.toList());
    }

    public ProductDTO updateProduct(Long productId, UpdateProductRequest updateProductRequest) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        product = Product.builder()
            .id(product.getId())
            .name(StringUtils.defaultIfBlank(updateProductRequest.getName().trim(), product.getName()))
            .description(StringUtils.defaultIfBlank(updateProductRequest.getDescription().trim(), product.getDescription()))
            .price(Optional.ofNullable(updateProductRequest.getPriceInCents())
                .map(priceInCents -> priceInCents.divide(BigDecimal.valueOf(100), RoundingMode.UNNECESSARY))
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
