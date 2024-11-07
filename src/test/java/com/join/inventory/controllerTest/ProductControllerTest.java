package com.join.inventory.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.join.inventory.model.dto.CreateProductRequest;
import com.join.inventory.model.dto.ProductDTO;
import com.join.inventory.model.dto.UpdateProductRequest;
import com.join.inventory.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        productDTO = new ProductDTO(1L, "Product Test", "Description of Product Test 1", new BigDecimal("10000.00"), 1L);
    }

    @Test
    void testCreateProduct() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Product Test", "Description of Product Test 1", new BigDecimal("10000"), 1L);

        when(productService.createProduct(any(CreateProductRequest.class))).thenReturn(productDTO);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Product Test\", \"description\": \"Description of Product Test 1\", \"priceInCents\": 10000, \"categoryId\": 1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Product Test"))
                .andExpect(jsonPath("$.priceInCents").value(10000));

        verify(productService, times(1)).createProduct(any(CreateProductRequest.class));
    }

    @Test
    void testGetProductDetails() throws Exception {
        when(productService.getProductDetails(anyLong())).thenReturn(productDTO);

        mockMvc.perform(get("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product Test"));

        verify(productService, times(1)).getProductDetails(1L);
    }

    @Test
    void testGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(productDTO));

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Product Test"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testUpdateProduct() throws Exception {
        var updateRequest = new UpdateProductRequest("Updated Product", "Updated Description", new BigDecimal("15000"), 2L);

        productDTO = new ProductDTO(1L, "Updated Product", "Updated Description", new BigDecimal("15000"), 2L);

        when(productService.updateProduct(anyLong(), any())).thenReturn(productDTO);

        mockMvc.perform(put("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.priceInCents").value(15000));  // Checa se priceInCents é 15000

        verify(productService, times(1)).updateProduct(anyLong(), any());
    }

    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(anyLong());

        mockMvc.perform(delete("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1L);
    }
}
