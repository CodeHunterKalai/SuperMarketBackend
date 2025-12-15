package com.supermarket.repository;

import com.supermarket.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Optional<Product> findByBarcode(String barcode);
    
    boolean existsByBarcode(String barcode);
    
    List<Product> findByNameContainingIgnoreCase(String name);
    
    List<Product> findByCategory(String category);
    
    @Query("SELECT p FROM Product p WHERE p.quantity <= p.lowStockThreshold AND p.quantity > 0")
    List<Product> findLowStockProducts();
    
    @Query("SELECT p FROM Product p WHERE p.status = 'Out of Stock'")
    List<Product> findOutOfStockProducts();
    
    @Query("SELECT DISTINCT p.category FROM Product p ORDER BY p.category")
    List<String> findAllCategories();
}
