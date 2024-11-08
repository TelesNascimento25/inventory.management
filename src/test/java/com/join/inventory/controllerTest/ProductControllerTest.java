package com.join.inventory.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.join.inventory.controller.ProductController;
import com.join.inventory.model.dto.CreateProductRequest;
import com.join.inventory.model.dto.ProductDTO;
import com.join.inventory.model.dto.UpdateProductRequest;
import com.join.inventory.service.ProductService;
import com.join.inventory.util.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

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

    private ObjectMapper objectMapper;
    @Autowired
    private Converter converter;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(new ProductController(productService, converter)).build();
    }

    @Test
    public void test_Create_Product() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Laptop", "High performance laptop", new BigDecimal(1), 1000L);
        ProductDTO mockProductDTO = new ProductDTO(1L, "Laptop", "High performance laptop", new BigDecimal(1), 1000L);

        when(productService.createProduct(any(CreateProductRequest.class))).thenReturn(mockProductDTO);

        mockMvc.perform(post("/products")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.description").value("High performance laptop"));
    }

    @Test
    public void test_Get_Product_Details() throws Exception {
        ProductDTO mockProductDTO = new ProductDTO(1L, "Laptop", "High performance laptop", new BigDecimal(1), 1000L);

        when(productService.getProductDetails(1L)).thenReturn(mockProductDTO);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.description").value("High performance laptop"));
    }

    @Test
    public void test_Update_Product() throws Exception {
        UpdateProductRequest request = new UpdateProductRequest("Updated Laptop", "Updated description", new BigDecimal(1), 900L);
        ProductDTO mockProductDTO = new ProductDTO(1L, "Updated Laptop", "Updated description", new BigDecimal(1), 900L);

        when(productService.updateProduct(eq(1L), any(UpdateProductRequest.class))).thenReturn(mockProductDTO);

        mockMvc.perform(put("/products/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Laptop"))
                .andExpect(jsonPath("$.description").value("Updated description"));
    }

    @Test
    public void test_Delete_Product() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());
    }
}
