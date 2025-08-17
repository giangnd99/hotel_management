package com.poly.paymentdataaccess.repository.impl;

import com.poly.paymentdataaccess.entity.InvoicePaymentEntity;
import com.poly.paymentdataaccess.mapper.InvoicePaymentMapper;
import com.poly.paymentdataaccess.repository.InvoicePaymentJpaRepository;
import com.poly.paymentdomain.model.InvoicePayment;
import com.poly.paymentdomain.output.InvoicePaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InvoicePaymentRepositoryImpl implements InvoicePaymentRepository {

    private final InvoicePaymentJpaRepository  invoicePaymentJpaRepository;

    @Override
    public InvoicePayment save(InvoicePayment object) {
        InvoicePaymentEntity entity = InvoicePaymentMapper.toEntity(object);
        entity = invoicePaymentJpaRepository.save(entity);
        return InvoicePaymentMapper.toDomain(entity);
    }

    @Override
    public InvoicePayment update(InvoicePayment object) {
        InvoicePaymentEntity entity = InvoicePaymentMapper.toEntity(object);
        entity = invoicePaymentJpaRepository.save(entity);
        return InvoicePaymentMapper.toDomain(entity);
    }

    @Override
    public void delete(UUID uuid) {
        InvoicePaymentEntity entity = invoicePaymentJpaRepository.findById(uuid).orElse(null);
        invoicePaymentJpaRepository.delete(entity);
    }

    @Override
    public Optional<InvoicePayment> findById(UUID uuid) {
        InvoicePaymentEntity entity = invoicePaymentJpaRepository.findById(uuid).orElse(null);
        return Optional.ofNullable(InvoicePaymentMapper.toDomain(entity));
    }

    @Override
    public List<InvoicePayment> findAll() {
        List<InvoicePaymentEntity> entities = invoicePaymentJpaRepository.findAll();
        List<InvoicePayment> result = entities.stream().map(InvoicePaymentMapper::toDomain).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<InvoicePayment> findAllById(UUID uuid) {
        List<InvoicePaymentEntity> entities = invoicePaymentJpaRepository.findAllById(Collections.singleton(uuid));
        return List.of(InvoicePaymentMapper.toDomain(entities.get(0)));
    }

    @Override
    public InvoicePayment remove(InvoicePayment id) {
        return null;
    }

    @Override
    public Optional<InvoicePayment> findByPaymentId(UUID paymentId) {
        Optional<InvoicePaymentEntity> entity = invoicePaymentJpaRepository.findByPaymentId(paymentId);
        if (entity.isPresent()) {
            return Optional.of(InvoicePaymentMapper.toDomain(entity.get()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<InvoicePayment> findByInvoiceId(UUID invoiceId) {
        Optional<InvoicePaymentEntity> entity = invoicePaymentJpaRepository.findByInvoiceId(invoiceId);
        if (entity.isPresent()) {
            return Optional.of(InvoicePaymentMapper.toDomain(entity.get()));
        }
        return Optional.empty();
    }
}
