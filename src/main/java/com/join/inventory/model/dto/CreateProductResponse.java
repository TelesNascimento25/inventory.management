package com.join.inventory.model.dto;


import com.join.inventory.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductResponse {
    
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long categoryId;

    public static CreateProductResponse fromProduct(Product product) {
        return CreateProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .categoryId(product.getCategory().getId())
        .build();
    }
}
