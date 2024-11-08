package com.join.inventory.serviceTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.join.inventory.exception.DuplicateProductException;
import com.join.inventory.exception.ProductNotFoundException;
import com.join.inventory.model.Product;
import com.join.inventory.model.dto.CreateProductRequest;
import com.join.inventory.model.dto.ProductDTO;
import com.join.inventory.model.dto.UpdateProductRequest;
import com.join.inventory.repository.ProductRepository;
import com.join.inventory.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

class ProductServiceTest {

    @Mock
    private ProductRepository product_repository;

    @InjectMocks
    private ProductService product_service;

    private Product product;
    private CreateProductRequest create_product_request;
    private UpdateProductRequest update_product_request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = Product.builder()
                .id(1L)
                .name("Product 1")
                .description("Description")
                .price(BigDecimal.valueOf(10))
                .categoryId(1L)
                .build();
        create_product_request = new CreateProductRequest("Product 1", "Description", BigDecimal.valueOf(100), 1L);
        update_product_request = new UpdateProductRequest("Updated Product", "Updated Description", BigDecimal.valueOf(150), 2L);
    }

    @Test
    void test_create_product_is_success() {
        when(product_repository.existsByName(create_product_request.getName())).thenReturn(false);
        when(product_repository.save(any(Product.class))).thenReturn(product);

        ProductDTO created_product_dto = product_service.createProduct(create_product_request);

        assertNotNull(created_product_dto);
        assertEquals("Product 1", created_product_dto.getName());
        assertEquals("Description", created_product_dto.getDescription());
        assertEquals(BigDecimal.valueOf(1000), created_product_dto.getPriceInCents());
        assertEquals(1L, created_product_dto.getCategoryId());
        verify(product_repository, times(1)).existsByName("Product 1");
        verify(product_repository, times(1)).save(any(Product.class));
    }

    @Test
    void test_create_product_throws_duplicate_product_exception() {
        when(product_repository.existsByName(create_product_request.getName())).thenReturn(true);

        assertThrows(DuplicateProductException.class, () -> product_service.createProduct(create_product_request));
        verify(product_repository, times(1)).existsByName("Product 1");
    }

    @Test
    void test_get_product_details_is_success() {
        when(product_repository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO product_dto = product_service.getProductDetails(1L);

        assertNotNull(product_dto);
        assertEquals("Product 1", product_dto.getName());
        assertEquals("Description", product_dto.getDescription());
        assertEquals(BigDecimal.valueOf(1000), product_dto.getPriceInCents());
        verify(product_repository, times(1)).findById(1L);
    }

    @Test
    void test_get_product_details_throws_product_not_found_exception() {
        when(product_repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> product_service.getProductDetails(1L));
        verify(product_repository, times(1)).findById(1L);
    }

    @Test
    void test_get_products_by_category_is_success() {
        List<Product> products = List.of(product);
        when(product_repository.findByCategoryId(1L)).thenReturn(products);

        List<ProductDTO> product_dtos = product_service.getProductsByCategory(1L);

        assertNotNull(product_dtos);
        assertEquals(1, product_dtos.size());
        assertEquals("Product 1", product_dtos.get(0).getName());
        verify(product_repository, times(1)).findByCategoryId(1L);
    }

    @Test
    void test_get_products_by_category_throws_product_not_found_exception() {
        when(product_repository.findByCategoryId(1L)).thenReturn(List.of());

        assertThrows(ProductNotFoundException.class, () -> product_service.getProductsByCategory(1L));
        verify(product_repository, times(1)).findByCategoryId(1L);
    }

    @Test
    void test_get_all_products_is_success() {
        List<Product> products = List.of(product);
        when(product_repository.findAll()).thenReturn(products);

        List<ProductDTO> product_dtos = product_service.getAllProducts();

        assertNotNull(product_dtos);
        assertEquals(1, product_dtos.size());
        assertEquals("Product 1", product_dtos.get(0).getName());
        verify(product_repository, times(1)).findAll();
    }

    @Test
    void test_update_product_is_success() {
        when(product_repository.findById(1L)).thenReturn(Optional.of(product));
        when(product_repository.save(any(Product.class))).thenReturn(product);

        ProductDTO updated_product_dto = product_service.updateProduct(1L, update_product_request);

        assertNotNull(updated_product_dto);
        assertEquals("Product 1", updated_product_dto.getName());
        assertEquals("Description", updated_product_dto.getDescription());

        assertEquals(1, updated_product_dto.getPriceInCents().compareTo(BigDecimal.valueOf(150)));
        verify(product_repository, times(1)).findById(1L);
        verify(product_repository, times(1)).save(any(Product.class));
    }

    @Test
    void test_update_product_with_blank_fields() {
        when(product_repository.findById(1L)).thenReturn(Optional.of(product));
        when(product_repository.save(any(Product.class))).thenReturn(product);

        UpdateProductRequest blank_update_request = new UpdateProductRequest("", "", null, null);
        ProductDTO updated_product_dto = product_service.updateProduct(1L, blank_update_request);

        assertNotNull(updated_product_dto);
        assertEquals("Product 1", updated_product_dto.getName());
        assertEquals("Description", updated_product_dto.getDescription());
        assertEquals(BigDecimal.valueOf(1000), updated_product_dto.getPriceInCents());
    }

    @Test
    void test_delete_product_is_success() {
        when(product_repository.findById(1L)).thenReturn(Optional.of(product));

        product_service.deleteProduct(1L);

        verify(product_repository, times(1)).findById(1L);
        verify(product_repository, times(1)).delete(product);
    }

    @Test
    void test_delete_product_throws_product_not_found_exception() {
        when(product_repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> product_service.deleteProduct(1L));
        verify(product_repository, times(1)).findById(1L);
    }
}
