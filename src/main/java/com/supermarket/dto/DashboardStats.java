package com.supermarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    private Long totalProducts;
    private Long lowStockCount;
    private Long outOfStockCount;
    private Double todaysSales;
    private Long todaysTransactions;
    private Double monthlySales;
    private Long monthlyTransactions;
}
