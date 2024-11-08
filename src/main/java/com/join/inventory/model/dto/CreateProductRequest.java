package com.join.inventory.model.dto;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

    @NotBlank
    private String name;

    private String description;

    @PositiveOrZero
    @Digits(integer = 12, fraction = 0)
    private BigDecimal priceInCents;

    @NotNull
    private Long categoryId;
}