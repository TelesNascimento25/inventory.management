package com.join.inventory.controllerTest;

import com.join.inventory.controller.ProductController;
import com.join.inventory.model.dto.CreateProductRequest;
import com.join.inventory.model.dto.ProductDTO;
import com.join.inventory.model.dto.UpdateProductRequest;
import com.join.inventory.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        productDTO = new ProductDTO(1L, "Produto Teste", "Descrição", new BigDecimal("100.00"), 1L);
    }

    @Test
    void testCreateProduct() {
        CreateProductRequest request = new CreateProductRequest("Produto Teste", "Descrição", new BigDecimal("100.00"), 1L);

        when(productService.createProduct(any())).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productController.createProduct(request);

        verify(productService, times(1)).createProduct(any());
        assert(response.getStatusCodeValue() == 201);
        assert(response.getBody().getId().equals(1L));
    }

    @Test
    void testGetProductDetails() {
        when(productService.getProductDetails(anyLong())).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productController.getProductDetails(1L);

        verify(productService, times(1)).getProductDetails(1L);
        assert(response.getStatusCodeValue() == 200);
        assert(response.getBody().getName().equals("Produto Teste"));
    }

    @Test
    void testGetAllProducts() {
        when(productService.getAllProducts()).thenReturn(List.of(productDTO));

        ResponseEntity<List<ProductDTO>> response = productController.getAllProducts(null);

        verify(productService, times(1)).getAllProducts();
        assert(response.getStatusCodeValue() == 200);
        assert(response.getBody().size() == 1);
    }

    @Test
    void testUpdateProduct() {
        var updateRequest = new UpdateProductRequest("Produto Atualizado", "Descrição Atualizada", new BigDecimal("150.00"), 2L);

        when(productService.updateProduct(anyLong(), any())).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productController.updateProduct(1L, updateRequest);

        verify(productService, times(1)).updateProduct(anyLong(), any());
        assert(response.getStatusCodeValue() == 200);
        assert(response.getBody().getName().equals("Produto Teste"));
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productService).deleteProduct(anyLong());

        ResponseEntity<Void> response = productController.deleteProduct(1L);

        verify(productService, times(1)).deleteProduct(1L);
        assert(response.getStatusCodeValue() == 204);
    }
}
