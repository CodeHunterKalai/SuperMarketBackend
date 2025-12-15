package com.supermarket.controller;

import com.supermarket.dto.ProductDTO;
import com.supermarket.model.Product;
import com.supermarket.model.StockMovement;
import com.supermarket.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
    
    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<Product> getProductByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok(productService.getProductByBarcode(barcode));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        return ResponseEntity.ok(productService.getAllCategories());
    }
    
    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts() {
        return ResponseEntity.ok(productService.getLowStockProducts());
    }
    
    @GetMapping("/out-of-stock")
    public ResponseEntity<List<Product>> getOutOfStockProducts() {
        return ResponseEntity.ok(productService.getOutOfStockProducts());
    }
    
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        Product product = productService.createProduct(productDTO);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, 
                                                 @Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/adjust-stock")
    public ResponseEntity<Product> adjustStock(@PathVariable Long id, 
                                               @RequestBody Map<String, Object> request) {
        Integer adjustment = (Integer) request.get("adjustment");
        String notes = (String) request.getOrDefault("notes", "Manual stock adjustment");
        return ResponseEntity.ok(productService.adjustStock(id, adjustment, notes));
    }
    
    @GetMapping("/{id}/stock-movements")
    public ResponseEntity<List<StockMovement>> getStockMovementHistory(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getStockMovementHistory(id));
    }
    
    @GetMapping("/stock-movements/recent")
    public ResponseEntity<List<StockMovement>> getRecentStockMovements() {
        return ResponseEntity.ok(productService.getRecentStockMovements());
    }
}
