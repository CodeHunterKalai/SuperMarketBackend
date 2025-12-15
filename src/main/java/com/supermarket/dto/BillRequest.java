package com.supermarket.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class BillRequest {
    
    @NotEmpty(message = "Bill must contain at least one item")
    private List<BillItemDTO> items;
    
    @NotNull(message = "Tax rate is required")
    @DecimalMin(value = "0.0", message = "Tax rate cannot be negative")
    @DecimalMax(value = "100.0", message = "Tax rate cannot exceed 100%")
    private Double taxRate;
    
    @NotNull(message = "Discount is required")
    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    private Double discount;
    
    @Data
    public static class BillItemDTO {
        @NotBlank(message = "Barcode is required")
        private String barcode;
        
        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;
    }
}
