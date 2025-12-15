package com.supermarket.service;

import com.supermarket.dto.DashboardStats;
import com.supermarket.dto.SalesReport;
import com.supermarket.model.Bill;
import com.supermarket.repository.BillRepository;
import com.supermarket.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    
    private final BillRepository billRepository;
    private final ProductRepository productRepository;
    
    public DashboardStats getDashboardStats() {
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        
        LocalDateTime monthStart = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
        LocalDateTime monthEnd = LocalDateTime.now();
        
        DashboardStats stats = new DashboardStats();
        
        // Product stats
        stats.setTotalProducts(productRepository.count());
        stats.setLowStockCount((long) productRepository.findLowStockProducts().size());
        stats.setOutOfStockCount((long) productRepository.findOutOfStockProducts().size());
        
        // Today's sales
        Double todaysSales = billRepository.getTotalRevenueBetween(todayStart, todayEnd);
        stats.setTodaysSales(todaysSales != null ? todaysSales : 0.0);
        
        Long todaysTransactions = billRepository.getTransactionCountBetween(todayStart, todayEnd);
        stats.setTodaysTransactions(todaysTransactions != null ? todaysTransactions : 0L);
        
        // Monthly sales
        Double monthlySales = billRepository.getTotalRevenueBetween(monthStart, monthEnd);
        stats.setMonthlySales(monthlySales != null ? monthlySales : 0.0);
        
        Long monthlyTransactions = billRepository.getTransactionCountBetween(monthStart, monthEnd);
        stats.setMonthlyTransactions(monthlyTransactions != null ? monthlyTransactions : 0L);
        
        return stats;
    }
    
    public SalesReport getSalesReport(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.MAX);
        
        List<Bill> bills = billRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(start, end);
        
        SalesReport report = new SalesReport();
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        
        if (bills.isEmpty()) {
            report.setTotalRevenue(0.0);
            report.setTransactionCount(0L);
            report.setAverageTransactionValue(0.0);
            report.setTotalTax(0.0);
            report.setTotalDiscount(0.0);
            return report;
        }
        
        double totalRevenue = bills.stream().mapToDouble(Bill::getTotal).sum();
        long transactionCount = bills.size();
        double totalTax = bills.stream().mapToDouble(Bill::getTax).sum();
        double totalDiscount = bills.stream().mapToDouble(Bill::getDiscount).sum();
        double averageTransactionValue = totalRevenue / transactionCount;
        
        report.setTotalRevenue(totalRevenue);
        report.setTransactionCount(transactionCount);
        report.setAverageTransactionValue(averageTransactionValue);
        report.setTotalTax(totalTax);
        report.setTotalDiscount(totalDiscount);
        
        return report;
    }
    
    public SalesReport getDailyReport() {
        return getSalesReport(LocalDate.now(), LocalDate.now());
    }
    
    public SalesReport getMonthlyReport() {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now();
        return getSalesReport(startOfMonth, endOfMonth);
    }
}
