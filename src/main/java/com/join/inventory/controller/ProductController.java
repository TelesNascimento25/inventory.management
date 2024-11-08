package com.join.inventory.controller;

import com.join.inventory.model.dto.CreateProductRequest;
import com.join.inventory.model.dto.ProductDTO;
import com.join.inventory.model.dto.UpdateProductRequest;
import com.join.inventory.service.ProductService;
import com.join.inventory.util.Converter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final Converter converter;

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody @Valid CreateProductRequest createProductRequest) {
        var createdProduct = productService.createProduct(createProductRequest);
        return ResponseEntity.created(URI.create("/products/" + createdProduct.getId())).body(createdProduct);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductDetails(@PathVariable Long productId) {
        var productDTO = productService.getProductDetails(productId);
        return ResponseEntity.ok(productDTO);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProducts(
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort,
            @RequestParam(value = "filterText", required = false) String filterText) {

        try {
            Sort sortOrder = converter.convertToSort(sort);

            Page<ProductDTO> productPage = Optional.ofNullable(categoryId)
                    .map(id -> productService.getProductsByCategory(id, page, size, sortOrder, filterText))
                    .orElseGet(() -> productService.getAllProducts(page, size, sortOrder, filterText));

            Map<String, Object> response = new HashMap<>();
            response.put("products", productPage.getContent());
            response.put("currentPage", productPage.getNumber());
            response.put("totalItems", productPage.getTotalElements());
            response.put("totalPages", productPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, @RequestBody @Valid UpdateProductRequest updateProductRequest) {
        var updatedProduct = productService.updateProduct(productId, updateProductRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
