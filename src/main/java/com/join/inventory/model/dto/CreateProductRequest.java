package com.join.inventory.model.dto;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {
    
    @NotBlank
    private String name;

    private String description;

    @PositiveOrZero
    @Digits(integer = 10, fraction = 2)
    private BigDecimal priceInCents;

    @NotNull
    private Long categoryId;
}