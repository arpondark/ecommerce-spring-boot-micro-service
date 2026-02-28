package site.shazan.ecommerce.product.repository;

import org.antlr.v4.runtime.misc.MultiMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.shazan.ecommerce.product.models.Product;


import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrue();

    @Query("SELECT p FROM products p WHERE p.active = true AND " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Product> searchProducts(@Param("keyword")String keyword);

    Optional<Product> findByIdAndActiveTrue(Long id);
}
