package com.supermarket.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "bill_number", unique = true, nullable = false)
    private String billNumber;
    
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillItem> items = new ArrayList<>();
    
    @Column(nullable = false)
    private Double subtotal;
    
    @Column(nullable = false)
    private Double tax;
    
    @Column(nullable = false)
    private Double discount;
    
    @Column(nullable = false)
    private Double total;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (billNumber == null) {
            billNumber = "BILL-" + System.currentTimeMillis();
        }
    }
    
    public void addItem(BillItem item) {
        items.add(item);
        item.setBill(this);
    }
    
    public void calculateTotals(double taxRate, double discountAmount) {
        this.subtotal = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        this.discount = discountAmount;
        double subtotalAfterDiscount = this.subtotal - this.discount;
        this.tax = subtotalAfterDiscount * (taxRate / 100);
        this.total = subtotalAfterDiscount + this.tax;
    }
}
