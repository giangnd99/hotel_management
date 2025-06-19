package com.poly.inventory.application.port.out;

import com.poly.inventory.domain.entity.InventoryTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LoadTransactionPort {
    List<InventoryTransaction> findAll();

    List<InventoryTransaction> findByDateRange(LocalDate from, LocalDate to);

    Optional<InventoryTransaction> findById(Integer id);
}
