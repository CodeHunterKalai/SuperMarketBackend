package com.supermarket.service;

import com.supermarket.dto.BillRequest;
import com.supermarket.dto.BillResponse;
import com.supermarket.exception.InsufficientStockException;
import com.supermarket.exception.ResourceNotFoundException;
import com.supermarket.model.Bill;
import com.supermarket.model.BillItem;
import com.supermarket.model.Product;
import com.supermarket.model.StockMovement;
import com.supermarket.repository.BillRepository;
import com.supermarket.repository.ProductRepository;
import com.supermarket.repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillingService {
    
    private final BillRepository billRepository;
    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;
    
    @Transactional
    public BillResponse createBill(BillRequest request) {
        Bill bill = new Bill();
        
        // Process each item
        for (BillRequest.BillItemDTO itemDTO : request.getItems()) {
            // Fetch product by barcode
            Product product = productRepository.findByBarcode(itemDTO.getBarcode())
                    .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with barcode: " + itemDTO.getBarcode()));
            
            // Check stock availability
            if (product.getQuantity() < itemDTO.getQuantity()) {
                throw new InsufficientStockException(
                    "Insufficient stock for product: " + product.getName() + 
                    ". Available: " + product.getQuantity() + ", Requested: " + itemDTO.getQuantity());
            }
            
            // Create bill item
            BillItem billItem = new BillItem();
            billItem.setProductId(product.getId());
            billItem.setProductName(product.getName());
            billItem.setBarcode(product.getBarcode());
            billItem.setPrice(product.getPrice());
            billItem.setQuantity(itemDTO.getQuantity());
            
            bill.addItem(billItem);
            
            // Deduct stock
            Integer previousStock = product.getQuantity();
            product.setQuantity(previousStock - itemDTO.getQuantity());
            productRepository.save(product);
            
            // Log stock movement
            logStockMovement(product, itemDTO.getQuantity(), previousStock, 
                           product.getQuantity(), bill.getBillNumber());
        }
        
        // Calculate totals
        bill.calculateTotals(request.getTaxRate(), request.getDiscount());
        
        // Save bill
        Bill savedBill = billRepository.save(bill);
        
        return convertToResponse(savedBill);
    }
    
    public BillResponse getBillById(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
        return convertToResponse(bill);
    }
    
    public BillResponse getBillByNumber(String billNumber) {
        Bill bill = billRepository.findByBillNumber(billNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with number: " + billNumber));
        return convertToResponse(bill);
    }
    
    public List<BillResponse> getAllBills() {
        return billRepository.findRecentBills().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<BillResponse> getBillsByDateRange(LocalDateTime start, LocalDateTime end) {
        return billRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(start, end).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    private BillResponse convertToResponse(Bill bill) {
        BillResponse response = new BillResponse();
        response.setId(bill.getId());
        response.setBillNumber(bill.getBillNumber());
        response.setSubtotal(bill.getSubtotal());
        response.setTax(bill.getTax());
        response.setDiscount(bill.getDiscount());
        response.setTotal(bill.getTotal());
        response.setCreatedAt(bill.getCreatedAt());
        
        List<BillResponse.BillItemResponse> items = new ArrayList<>();
        for (BillItem item : bill.getItems()) {
            BillResponse.BillItemResponse itemResponse = new BillResponse.BillItemResponse();
            itemResponse.setProductId(item.getProductId());
            itemResponse.setProductName(item.getProductName());
            itemResponse.setBarcode(item.getBarcode());
            itemResponse.setPrice(item.getPrice());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setSubtotal(item.getSubtotal());
            items.add(itemResponse);
        }
        
        response.setItems(items);
        return response;
    }
    
    private void logStockMovement(Product product, Integer quantity, Integer previousStock, 
                                   Integer newStock, String billNumber) {
        StockMovement movement = new StockMovement();
        movement.setProductId(product.getId());
        movement.setProductName(product.getName());
        movement.setBarcode(product.getBarcode());
        movement.setMovementType("SALE");
        movement.setQuantity(quantity);
        movement.setPreviousStock(previousStock);
        movement.setNewStock(newStock);
        movement.setBillNumber(billNumber);
        movement.setNotes("Stock deducted via billing");
        
        stockMovementRepository.save(movement);
    }
}
