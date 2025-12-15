package com.supermarket.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BillResponse {
    private Long id;
    private String billNumber;
    private List<BillItemResponse> items;
    private Double subtotal;
    private Double tax;
    private Double discount;
    private Double total;
    private LocalDateTime createdAt;
    
    @Data
    public static class BillItemResponse {
        private Long productId;
        private String productName;
        private String barcode;
        private Double price;
        private Integer quantity;
        private Double subtotal;
    }
}
