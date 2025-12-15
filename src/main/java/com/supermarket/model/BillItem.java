package com.supermarket.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "bill_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    @JsonIgnore
    private Bill bill;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_name", nullable = false)
    private String productName;
    
    @Column(nullable = false)
    private String barcode;
    
    @Column(nullable = false)
    private Double price;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private Double subtotal;
    
    @PrePersist
    @PreUpdate
    protected void calculateSubtotal() {
        this.subtotal = this.price * this.quantity;
    }
}
