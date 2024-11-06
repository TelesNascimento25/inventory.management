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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.join.inventory.model.dto.CreateProductRequest;
import com.join.inventory.model.dto.CreateProductResponse;
import com.join.inventory.model.dto.UpdateProductRequest;
import com.join.inventory.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<CreateProductResponse> createProduct(@RequestBody @Valid CreateProductRequest createProductRequest) {
        CreateProductResponse createdProduct = productService.createProduct(createProductRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<CreateProductResponse> getProductDetails(@PathVariable Long productId) {
        CreateProductResponse productDTO = productService.getProductDetails(productId);
        return ResponseEntity.ok(productDTO);
    }

    @GetMapping
    public ResponseEntity<List<CreateProductResponse>> getAllProducts(@RequestParam(value = "category", required = false) String category) {
        List<CreateProductResponse> products;

        if (category != null) {
            products = productService.getProductsByCategory(category);
        } else {
            products = productService.getAllProducts();
        }

        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<CreateProductResponse> updateProduct(@PathVariable Long productId, @RequestBody @Valid UpdateProductRequest updateProductRequest) {
        CreateProductResponse updatedProduct = productService.updateProduct(productId, updateProductRequest);
        return updatedProduct != null ? ResponseEntity.ok(updatedProduct) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }
}
