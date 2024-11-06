package com.join.inventory.model.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {
    
    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Double price;

    @NotNull
    private Long categoryId;
}
