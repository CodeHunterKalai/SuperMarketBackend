package com.supermarket.controller;

import com.supermarket.dto.BillRequest;
import com.supermarket.dto.BillResponse;
import com.supermarket.service.BillingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillingController {
    
    private final BillingService billingService;
    
    @PostMapping
    public ResponseEntity<BillResponse> createBill(@Valid @RequestBody BillRequest request) {
        BillResponse response = billingService.createBill(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<BillResponse>> getAllBills() {
        return ResponseEntity.ok(billingService.getAllBills());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BillResponse> getBillById(@PathVariable Long id) {
        return ResponseEntity.ok(billingService.getBillById(id));
    }
    
    @GetMapping("/number/{billNumber}")
    public ResponseEntity<BillResponse> getBillByNumber(@PathVariable String billNumber) {
        return ResponseEntity.ok(billingService.getBillByNumber(billNumber));
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<BillResponse>> getBillsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(billingService.getBillsByDateRange(start, end));
    }
}
