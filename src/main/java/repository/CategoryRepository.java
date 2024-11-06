package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    interface CategoryProductCount {
        String getCategoryName();
        Long getProductCount();
    }

    @Query("SELECT c.name AS categoryName, COUNT(p) AS productCount " +
            "FROM Category c LEFT JOIN c.products p " +
            "GROUP BY c.id")
    List<CategoryProductCount> getCategoryProductCount();

    boolean existsByName(String name);
}