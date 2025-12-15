package com.supermarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesReport {
    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalRevenue;
    private Long transactionCount;
    private Double averageTransactionValue;
    private Double totalTax;
    private Double totalDiscount;
}
