package com.supermarket.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Product name is required")
    @Column(nullable = false)
    private String name;
    
    @NotBlank(message = "Category is required")
    @Column(nullable = false)
    private String category;
    
    @NotBlank(message = "Barcode is required")
    @Column(nullable = false, unique = true)
    private String barcode;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Column(nullable = false)
    private Double price;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private String status; // "In Stock" or "Out of Stock"
    
    @Column(name = "low_stock_threshold")
    private Integer lowStockThreshold = 10;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        updateStatus();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        updateStatus();
    }
    
    private void updateStatus() {
        this.status = (this.quantity != null && this.quantity > 0) ? "In Stock" : "Out of Stock";
    }
}
