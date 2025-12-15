package com.supermarket.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    
    @NotBlank(message = "Product name is required")
    private String name;
    
    @NotBlank(message = "Category is required")
    private String category;
    
    @NotBlank(message = "Barcode is required")
    private String barcode;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private Double price;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
    
    private String status;
    
    @Min(value = 1, message = "Low stock threshold must be at least 1")
    private Integer lowStockThreshold = 10;
}
