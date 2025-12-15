package com.supermarket.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockMovement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_name", nullable = false)
    private String productName;
    
    @Column(nullable = false)
    private String barcode;
    
    @Column(name = "movement_type", nullable = false)
    private String movementType; // "SALE", "RESTOCK", "ADJUSTMENT"
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(name = "previous_stock")
    private Integer previousStock;
    
    @Column(name = "new_stock")
    private Integer newStock;
    
    @Column(name = "bill_number")
    private String billNumber;
    
    private String notes;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
