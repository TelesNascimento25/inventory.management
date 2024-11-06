package model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductResponse {
    
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long categoryId;
}
