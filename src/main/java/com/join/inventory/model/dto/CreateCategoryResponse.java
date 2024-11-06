package com.join.inventory.model.dto;


import com.join.inventory.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class CreateCategoryResponse {
    
    private Long id;
    private String name;
    private String description;
    private int totalProducts;

    public static CreateCategoryResponse fromCategory(Category category) {
        return CreateCategoryResponse.builder()
            .id(category.getId())
            .name(category.getName())
            .description(category.getDescription())
            .totalProducts(category.getProducts().size())
        .build();
    }
}
