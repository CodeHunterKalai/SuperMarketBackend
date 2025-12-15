package com.supermarket.repository;

import com.supermarket.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    
    Optional<Bill> findByBillNumber(String billNumber);
    
    List<Bill> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT SUM(b.total) FROM Bill b WHERE b.createdAt BETWEEN :start AND :end")
    Double getTotalRevenueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT COUNT(b) FROM Bill b WHERE b.createdAt BETWEEN :start AND :end")
    Long getTransactionCountBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT b FROM Bill b ORDER BY b.createdAt DESC")
    List<Bill> findRecentBills();
}
