package service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import exception.CategoryNotFoundException;
import exception.DuplicateProductException;
import exception.ProductNotFoundException;
import model.Category;
import model.Products;
import model.dto.CreateProductRequest;
import model.dto.CreateProductResponse;
import model.dto.UpdateProductRequest;
import repository.CategoryRepository;
import repository.ProductsRepository;

@Service
public class ProductService {

    private final ProductsRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductsRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public CreateProductResponse createProduct(CreateProductRequest createProductRequest) {
        if (productRepository.existsByName(createProductRequest.getName())) {
            throw new DuplicateProductException(createProductRequest.getName());
        }

        Category category = categoryRepository.findById(createProductRequest.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(createProductRequest.getCategoryId()));

        Products product = new Products();
        product.setName(createProductRequest.getName());
        product.setDescription(createProductRequest.getDescription());
        product.setPrice(createProductRequest.getPrice());
        product.setCategory(category);

        productRepository.save(product);

        return new CreateProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getCategory().getId());
    }

    public CreateProductResponse getProductDetails(Long productId) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return new CreateProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getCategory().getId());
    }

    public List<CreateProductResponse> getProductsByCategory(String category) {
        List<Products> products = productRepository.findByCategoryName(category);

        if (products.isEmpty()) {
            throw new ProductNotFoundException(null); 
        }

        return products.stream().map(product -> new CreateProductResponse(product.getId(), product.getName(),
                product.getDescription(), product.getPrice(), product.getCategory().getId())).collect(Collectors.toList());
    }

    public List<CreateProductResponse> getAllProducts() {
        List<Products> products = productRepository.findAll();

        return products.stream().map(product -> new CreateProductResponse(product.getId(), product.getName(),
                product.getDescription(), product.getPrice(), product.getCategory().getId())).collect(Collectors.toList());
    }

    public CreateProductResponse updateProduct(Long productId, UpdateProductRequest updateProductRequest) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        product.setName(updateProductRequest.getName());
        product.setDescription(updateProductRequest.getDescription());
        product.setPrice(updateProductRequest.getPrice());

        Category category = categoryRepository.findById(updateProductRequest.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(updateProductRequest.getCategoryId()));

        product.setCategory(category);

        productRepository.save(product);

        return new CreateProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getCategory().getId());
    }

    public boolean deleteProduct(Long productId) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        productRepository.delete(product);
        return true;
    }
}