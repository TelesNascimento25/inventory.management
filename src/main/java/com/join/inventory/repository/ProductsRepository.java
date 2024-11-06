package com.join.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.join.inventory.model.Category;
import com.join.inventory.model.Products;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {

    interface ProductDetails {
        String getName();
        Double getPrice();
        String getCategoryName();
    }

    @Query("SELECT p.name AS name, p.price AS price, c.name AS categoryName " +
            "FROM Product p JOIN p.category c " +
            "WHERE p.category.id = :categoryId")
    List<ProductDetails> getProductDetailsByCategory(Long categoryId);

    List<Products> findByPriceGreaterThan(Double price);

    List<Products> findByCategory(Category category);
    
    List<Products> findByCategoryId(Long categoryId);
    
    List<Products> findByCategoryName(String categoryName);
    
    boolean existsByName(String name);
}