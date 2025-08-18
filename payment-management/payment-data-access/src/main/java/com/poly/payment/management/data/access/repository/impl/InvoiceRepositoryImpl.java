package com.poly.payment.management.data.access.repository.impl;

import com.poly.payment.management.data.access.entity.InvoiceEntity;
import com.poly.payment.management.data.access.mapper.InvoiceMapper;
import com.poly.payment.management.data.access.repository.InvoiceJpaRepository;
import com.poly.payment.management.domain.model.Invoice;
import com.poly.payment.management.domain.port.output.repository.InvoiceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InvoiceRepositoryImpl implements InvoiceRepository {

    private final InvoiceJpaRepository invoiceJpaRepository;

    @Override
    public Invoice save(Invoice object) {
        InvoiceEntity entity = InvoiceMapper.toEntity(object);
        invoiceJpaRepository.save(entity);
        return InvoiceMapper.toDomain(entity);
    }

    @Override
    public Invoice update(Invoice object) {
        InvoiceEntity entity = InvoiceMapper.toEntity(object);
        invoiceJpaRepository.save(entity);
        return InvoiceMapper.toDomain(entity);
    }

    @Override
    public void delete(UUID uuid) {
        invoiceJpaRepository.deleteById(uuid);
    }

    @Override
    public Optional<Invoice> findById(UUID uuid) {
        InvoiceEntity entity = invoiceJpaRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found: " + uuid));
        return Optional.ofNullable(InvoiceMapper.toDomain(entity));
    }

    @Override
    public List<Invoice> findAll() {
        List<InvoiceEntity> entities = invoiceJpaRepository.findAll();
        List<Invoice> invoices = entities.stream().map(InvoiceMapper::toDomain).collect(Collectors.toList());
        return invoices;
    }

    @Override
    public List<Invoice> findAllById(UUID customerId) {
        List<InvoiceEntity> entities = invoiceJpaRepository.findAllByCustomerId(customerId);
        List<Invoice> invoices = entities.stream().map(InvoiceMapper::toDomain).collect(Collectors.toList());
        return invoices;
    }

    @Override
    public Invoice remove(Invoice id) {
        InvoiceEntity entity = InvoiceMapper.toEntity(id);
        entity = invoiceJpaRepository.save(entity);
        return null;
    }
}
