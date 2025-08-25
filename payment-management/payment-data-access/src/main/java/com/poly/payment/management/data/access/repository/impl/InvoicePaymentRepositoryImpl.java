package com.poly.payment.management.data.access.repository.impl;

import com.poly.payment.management.data.access.entity.InvoicePaymentEntity;
import com.poly.payment.management.data.access.mapper.InvoicePaymentMapper;
import com.poly.payment.management.data.access.repository.InvoicePaymentJpaRepository;
import com.poly.payment.management.domain.model.InvoicePayment;
import com.poly.payment.management.domain.port.output.repository.InvoicePaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InvoicePaymentRepositoryImpl implements InvoicePaymentRepository {

    private final InvoicePaymentJpaRepository  invoicePaymentJpaRepository;
    private final InvoicePaymentMapper invoicePaymentMapper;

    @Override
    public InvoicePayment save(InvoicePayment object) {
        InvoicePaymentEntity entity = invoicePaymentMapper.toEntity(object);
        entity = invoicePaymentJpaRepository.save(entity);
        return invoicePaymentMapper.toDomain(entity);
    }

    @Override
    public InvoicePayment update(InvoicePayment object) {
        InvoicePaymentEntity entity = invoicePaymentMapper.toEntity(object);
        entity = invoicePaymentJpaRepository.save(entity);
        return invoicePaymentMapper.toDomain(entity);
    }

    @Override
    public void delete(UUID uuid) {
        InvoicePaymentEntity entity = invoicePaymentJpaRepository.findById(uuid).orElse(null);
        invoicePaymentJpaRepository.delete(entity);
    }

    @Override
    public Optional<InvoicePayment> findById(UUID uuid) {
        InvoicePaymentEntity entity = invoicePaymentJpaRepository.findById(uuid).orElse(null);
        return Optional.ofNullable(invoicePaymentMapper.toDomain(entity));
    }

    @Override
    public List<InvoicePayment> findAll() {
        List<InvoicePaymentEntity> entities = invoicePaymentJpaRepository.findAll();
        List<InvoicePayment> result = entities.stream().map(invoicePaymentMapper::toDomain).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<InvoicePayment> findAllById(UUID uuid) {
        List<InvoicePaymentEntity> entities = invoicePaymentJpaRepository.findAllById(Collections.singleton(uuid));
        return entities.stream().map(invoicePaymentMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public InvoicePayment remove(InvoicePayment id) {
        return null;
    }

    @Override
    public Optional<InvoicePayment> findByPaymentId(UUID paymentId) {
        Optional<InvoicePaymentEntity> entity = invoicePaymentJpaRepository.findByPaymentId(paymentId);
        if (entity.isPresent()) {
            return Optional.of(invoicePaymentMapper.toDomain(entity.get()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<InvoicePayment> findByInvoiceId(UUID invoiceId) {
        Optional<InvoicePaymentEntity> entity = invoicePaymentJpaRepository.findByInvoiceId(invoiceId);
        if (entity.isPresent()) {
            return Optional.of(invoicePaymentMapper.toDomain(entity.get()));
        }
        return Optional.empty();
    }
}
