package com.poly.inventory.dataaccess.adapter;

import com.poly.inventory.application.port.out.LoadTransactionPort;
import com.poly.inventory.application.port.out.SaveTransactionPort;
import com.poly.inventory.dataaccess.entity.TransactionEntity;
import com.poly.inventory.dataaccess.jpa.TransactionJpaRepository;
import com.poly.inventory.dataaccess.mapper.TransactionEntityMapper;
import com.poly.inventory.domain.entity.InventoryTransaction;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TransactionJpaAdapter implements LoadTransactionPort, SaveTransactionPort {

    private final TransactionJpaRepository transactionRepository;

    public TransactionJpaAdapter(TransactionJpaRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<InventoryTransaction> findAll() {
        return transactionRepository.findAll().stream()
                .map(TransactionEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<InventoryTransaction> findByDateRange(LocalDate from, LocalDate to) {
        return transactionRepository.findByTransactionDateBetween(from.atStartOfDay(), to.atTime(23, 59, 59))
                .stream()
                .map(TransactionEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryTransaction save(InventoryTransaction transaction) {
        TransactionEntity entity = TransactionEntityMapper.toEntity(transaction);
        TransactionEntity saved = transactionRepository.save(entity);
        return TransactionEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<InventoryTransaction> findById(Integer id) {
        return transactionRepository.findById(id)
                .map(TransactionEntityMapper::toDomain);
    }
}
