package com.supermarket.service;

import com.supermarket.dto.ProductDTO;
import com.supermarket.exception.DuplicateBarcodeException;
import com.supermarket.exception.ResourceNotFoundException;
import com.supermarket.model.Product;
import com.supermarket.model.StockMovement;
import com.supermarket.repository.ProductRepository;
import com.supermarket.repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }
    
    public Product getProductByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with barcode: " + barcode));
    }
    
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
    
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    
    public List<String> getAllCategories() {
        return productRepository.findAllCategories();
    }
    
    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts();
    }
    
    public List<Product> getOutOfStockProducts() {
        return productRepository.findOutOfStockProducts();
    }
    
    @Transactional
    public Product createProduct(ProductDTO productDTO) {
        // Check for duplicate barcode
        if (productRepository.existsByBarcode(productDTO.getBarcode())) {
            throw new DuplicateBarcodeException("Product with barcode " + productDTO.getBarcode() + " already exists");
        }
        
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setCategory(productDTO.getCategory());
        product.setBarcode(productDTO.getBarcode());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setLowStockThreshold(productDTO.getLowStockThreshold());
        
        Product savedProduct = productRepository.save(product);
        
        // Log stock movement
        logStockMovement(savedProduct, "INITIAL", savedProduct.getQuantity(), 0, savedProduct.getQuantity(), 
                        "Initial stock entry");
        
        return savedProduct;
    }
    
    @Transactional
    public Product updateProduct(Long id, ProductDTO productDTO) {
        Product product = getProductById(id);
        
        // Check for duplicate barcode (excluding current product)
        if (!product.getBarcode().equals(productDTO.getBarcode()) && 
            productRepository.existsByBarcode(productDTO.getBarcode())) {
            throw new DuplicateBarcodeException("Product with barcode " + productDTO.getBarcode() + " already exists");
        }
        
        Integer previousQuantity = product.getQuantity();
        
        product.setName(productDTO.getName());
        product.setCategory(productDTO.getCategory());
        product.setBarcode(productDTO.getBarcode());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setLowStockThreshold(productDTO.getLowStockThreshold());
        
        Product updatedProduct = productRepository.save(product);
        
        // Log stock movement if quantity changed
        if (!previousQuantity.equals(productDTO.getQuantity())) {
            String movementType = productDTO.getQuantity() > previousQuantity ? "RESTOCK" : "ADJUSTMENT";
            int quantityDiff = Math.abs(productDTO.getQuantity() - previousQuantity);
            logStockMovement(updatedProduct, movementType, quantityDiff, previousQuantity, 
                           productDTO.getQuantity(), "Stock updated via product edit");
        }
        
        return updatedProduct;
    }
    
    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
    
    @Transactional
    public Product adjustStock(Long id, Integer adjustment, String notes) {
        Product product = getProductById(id);
        Integer previousStock = product.getQuantity();
        Integer newStock = previousStock + adjustment;
        
        if (newStock < 0) {
            throw new IllegalArgumentException("Stock adjustment would result in negative quantity");
        }
        
        product.setQuantity(newStock);
        Product updatedProduct = productRepository.save(product);
        
        String movementType = adjustment > 0 ? "RESTOCK" : "ADJUSTMENT";
        logStockMovement(updatedProduct, movementType, Math.abs(adjustment), previousStock, newStock, notes);
        
        return updatedProduct;
    }
    
    public List<StockMovement> getStockMovementHistory(Long productId) {
        return stockMovementRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }
    
    public List<StockMovement> getRecentStockMovements() {
        return stockMovementRepository.findTop50ByOrderByCreatedAtDesc();
    }
    
    private void logStockMovement(Product product, String movementType, Integer quantity, 
                                   Integer previousStock, Integer newStock, String notes) {
        StockMovement movement = new StockMovement();
        movement.setProductId(product.getId());
        movement.setProductName(product.getName());
        movement.setBarcode(product.getBarcode());
        movement.setMovementType(movementType);
        movement.setQuantity(quantity);
        movement.setPreviousStock(previousStock);
        movement.setNewStock(newStock);
        movement.setNotes(notes);
        
        stockMovementRepository.save(movement);
    }
}
