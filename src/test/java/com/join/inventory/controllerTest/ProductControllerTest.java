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
    private MockMvc mock_mvc;

    @MockBean
    private ProductService product_service;

    @Autowired
    private ObjectMapper object_mapper;

    private ProductDTO product_dto;

    @BeforeEach
    void setUp() {
        product_dto = new ProductDTO(1L, "Product Test", "Description of Product Test 1", new BigDecimal("10000.00"), 1L);
    }

    @Test
    void test_create_product() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Product Test", "Description of Product Test 1", new BigDecimal("10000"), 1L);

        when(product_service.createProduct(any(CreateProductRequest.class))).thenReturn(product_dto);

        mock_mvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Product Test\", \"description\": \"Description of Product Test 1\", \"priceInCents\": 10000, \"categoryId\": 1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Product Test"))
                .andExpect(jsonPath("$.priceInCents").value(10000));

        verify(product_service, times(1)).createProduct(any(CreateProductRequest.class));
    }

    @Test
    void test_get_product_details() throws Exception {
        when(product_service.getProductDetails(anyLong())).thenReturn(product_dto);

        mock_mvc.perform(get("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product Test"));

        verify(product_service, times(1)).getProductDetails(1L);
    }

    @Test
    void test_get_all_products() throws Exception {
        when(product_service.getAllProducts()).thenReturn(List.of(product_dto));

        mock_mvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Product Test"));

        verify(product_service, times(1)).getAllProducts();
    }

    @Test
    void test_update_product() throws Exception {
        var update_request = new UpdateProductRequest("Updated Product", "Updated Description", new BigDecimal("15000"), 2L);

        product_dto = new ProductDTO(1L, "Updated Product", "Updated Description", new BigDecimal("15000"), 2L);

        when(product_service.updateProduct(anyLong(), any())).thenReturn(product_dto);

        mock_mvc.perform(put("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(object_mapper.writeValueAsString(update_request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.priceInCents").value(15000));  // Checa se priceInCents é 15000

        verify(product_service, times(1)).updateProduct(anyLong(), any());
    }

    @Test
    void test_delete_product() throws Exception {
        doNothing().when(product_service).deleteProduct(anyLong());

        mock_mvc.perform(delete("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(product_service, times(1)).deleteProduct(1L);
    }
}
