package com.poly.inventory.dataaccess.jpa;

import com.poly.inventory.dataaccess.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, Integer> {
    List<TransactionEntity> findByTransactionDateBetween(LocalDateTime from, LocalDateTime to);
}
