package com.supermarket.controller;

import com.supermarket.dto.DashboardStats;
import com.supermarket.dto.SalesReport;
import com.supermarket.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    
    private final ReportService reportService;

    @GetMapping("/api/cron/keep-alive")
      public ResponseEntity<String> keepAlive() {
       return ResponseEntity.ok("OK");
     }
    
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStats> getDashboardStats() {
        return ResponseEntity.ok(reportService.getDashboardStats());
    }
    
    @GetMapping("/sales/daily")
    public ResponseEntity<SalesReport> getDailyReport() {
        return ResponseEntity.ok(reportService.getDailyReport());
    }
    
    @GetMapping("/sales/monthly")
    public ResponseEntity<SalesReport> getMonthlyReport() {
        return ResponseEntity.ok(reportService.getMonthlyReport());
    }
    
    @GetMapping("/sales/custom")
    public ResponseEntity<SalesReport> getCustomReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportService.getSalesReport(startDate, endDate));
    }
}
