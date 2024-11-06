package model.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateCategoryResponse {
    
    private Long id;
    private String name;
    private String description;
    private int totalProducts;
    
    public CreateCategoryResponse(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public CreateCategoryResponse(Long id, String name, String description, int totalProducts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.totalProducts = totalProducts;
    }
}
