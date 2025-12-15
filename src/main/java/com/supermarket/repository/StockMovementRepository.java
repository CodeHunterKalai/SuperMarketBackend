package com.supermarket.repository;

import com.supermarket.model.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    
    List<StockMovement> findByProductIdOrderByCreatedAtDesc(Long productId);
    
    List<StockMovement> findByBarcodeOrderByCreatedAtDesc(String barcode);
    
    List<StockMovement> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end);
    
    List<StockMovement> findTop50ByOrderByCreatedAtDesc();
}
