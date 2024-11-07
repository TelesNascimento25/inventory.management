package com.join.inventory.model.dto;


import com.join.inventory.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CategoryDTO {
    
    private Long id;
    private String name;
    private String description;

    public static CategoryDTO fromCategory(Category category) {
        return CategoryDTO.builder()
            .id(category.getId())
            .name(category.getName())
            .description(category.getDescription())
        .build();
    }
}
