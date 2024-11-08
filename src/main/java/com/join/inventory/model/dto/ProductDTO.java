package com.join.inventory.model.dto;


import com.join.inventory.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal priceInCents;
    private Long categoryId;

    public static ProductDTO fromProduct(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .priceInCents(product.getPrice().multiply(BigDecimal.valueOf(100)))
                .categoryId(product.getCategoryId())
                .build();
    }
}
