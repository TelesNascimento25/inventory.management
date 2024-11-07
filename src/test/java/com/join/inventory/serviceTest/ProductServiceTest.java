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
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private CreateProductRequest createProductRequest;
    private UpdateProductRequest updateProductRequest;

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
        createProductRequest = new CreateProductRequest("Product 1", "Description", BigDecimal.valueOf(100), 1L);
        updateProductRequest = new UpdateProductRequest("Updated Product", "Updated Description", BigDecimal.valueOf(150), 2L);
    }

    @Test
    void testCreateProductIsSuccess() {
        when(productRepository.existsByName(createProductRequest.getName())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO createdProductDTO = productService.createProduct(createProductRequest);

        assertNotNull(createdProductDTO);
        assertEquals("Product 1", createdProductDTO.getName());
        assertEquals("Description", createdProductDTO.getDescription());
        assertEquals(BigDecimal.valueOf(1000), createdProductDTO.getPriceInCents());
        assertEquals(1L, createdProductDTO.getCategoryId());
        verify(productRepository, times(1)).existsByName("Product 1");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testCreateProductThrowsDuplicateProductException() {
        when(productRepository.existsByName(createProductRequest.getName())).thenReturn(true);

        assertThrows(DuplicateProductException.class, () -> productService.createProduct(createProductRequest));
        verify(productRepository, times(1)).existsByName("Product 1");
    }

    @Test
    void testGetProductDetailsIsSuccess() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO productDTO = productService.getProductDetails(1L);

        assertNotNull(productDTO);
        assertEquals("Product 1", productDTO.getName());
        assertEquals("Description", productDTO.getDescription());
        assertEquals(BigDecimal.valueOf(1000), productDTO.getPriceInCents());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductDetailsThrowsProductNotFoundException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductDetails(1L));
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductsByCategoryIsSuccess() {
        List<Product> products = List.of(product);
        when(productRepository.findByCategoryId(1L)).thenReturn(products);

        List<ProductDTO> productDTOs = productService.getProductsByCategory(1L);

        assertNotNull(productDTOs);
        assertEquals(1, productDTOs.size());
        assertEquals("Product 1", productDTOs.getFirst().getName());
        verify(productRepository, times(1)).findByCategoryId(1L);
    }

    @Test
    void testGetProductsByCategoryThrowsProductNotFoundException() {
        when(productRepository.findByCategoryId(1L)).thenReturn(List.of());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductsByCategory(1L));
        verify(productRepository, times(1)).findByCategoryId(1L);
    }

    @Test
    void testGetAllProductsIsSuccess() {
        List<Product> products = List.of(product);
        when(productRepository.findAll()).thenReturn(products);

        List<ProductDTO> productDTOs = productService.getAllProducts();

        assertNotNull(productDTOs);
        assertEquals(1, productDTOs.size());
        assertEquals("Product 1", productDTOs.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testUpdateProductIsSuccess() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO updatedProductDTO = productService.updateProduct(1L, updateProductRequest);

        assertNotNull(updatedProductDTO);
        assertEquals("Product 1", updatedProductDTO.getName());
        assertEquals("Description", updatedProductDTO.getDescription());

        assertEquals(1, updatedProductDTO.getPriceInCents().compareTo(BigDecimal.valueOf(150)));
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }


    @Test
    void testUpdateProductWithBlankFields() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        UpdateProductRequest blankUpdateRequest = new UpdateProductRequest("", "", null, null);
        ProductDTO updatedProductDTO = productService.updateProduct(1L, blankUpdateRequest);

        assertNotNull(updatedProductDTO);
        assertEquals("Product 1", updatedProductDTO.getName());
        assertEquals("Description", updatedProductDTO.getDescription());
        assertEquals(BigDecimal.valueOf(1000), updatedProductDTO.getPriceInCents());
    }

    @Test
    void testDeleteProductIsSuccess() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void testDeleteProductThrowsProductNotFoundException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
        verify(productRepository, times(1)).findById(1L);
    }
}
