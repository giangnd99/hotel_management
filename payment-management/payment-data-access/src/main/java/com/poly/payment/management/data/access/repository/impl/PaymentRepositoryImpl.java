package com.poly.payment.management.data.access.repository.impl;

import com.poly.payment.management.data.access.entity.PaymentEntity;
import com.poly.payment.management.data.access.mapper.PaymentMapper;
import com.poly.payment.management.data.access.repository.PaymentJpaRepository;
import com.poly.payment.management.domain.value_object.PaymentStatus;
import com.poly.payment.management.domain.model.Payment;
import com.poly.payment.management.domain.port.output.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;


    @Override
    public Optional<Payment> findByReferenceId(UUID referenceId) {
        Optional<PaymentEntity> paymentEntity = paymentJpaRepository.findByReferenceId(referenceId);
        if (paymentEntity.isPresent()) {
            PaymentEntity paymentEntity1 = paymentEntity.get();
            Payment payment = PaymentMapper.toDomain(paymentEntity1);
            return Optional.of(payment);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Payment> findByOrderCode(long orderCode) {
        Optional<PaymentEntity> paymentEntity = paymentJpaRepository.findByOrderCode(orderCode);
        if (paymentEntity.isPresent()) {
            PaymentEntity paymentEntity1 = paymentEntity.get();
            Payment payment = PaymentMapper.toDomain(paymentEntity1);
            return Optional.of(payment);
        }
        return Optional.empty();
    }

    @Override
    public List<Payment> findAllByStatusAndCreatedAtBefore(com.poly.domain.valueobject.PaymentStatus status, LocalDateTime beforeTime) {
        List<PaymentEntity> paymentEntities = paymentJpaRepository.findAllByStatusAndCreatedAtBefore(PaymentStatus.valueOf(status.name()), beforeTime);
        List<Payment> payments = paymentEntities.stream().map(PaymentMapper::toDomain).collect(Collectors.toList());
        return payments;
    }

    @Override
    public Payment save(Payment object) {
        PaymentEntity entity = PaymentMapper.toEntity(object);
        entity = paymentJpaRepository.save(entity);
        Payment payment = PaymentMapper.toDomain(entity);
        return payment;
    }

    @Override
    public Payment update(Payment object) {
        PaymentEntity entity = PaymentMapper.toEntity(object);
        entity = paymentJpaRepository.save(entity);
        Payment payment = PaymentMapper.toDomain(entity);
        return payment;
    }

    @Override
    public void delete(UUID uuid) {
        PaymentEntity entity = paymentJpaRepository.findById(uuid).orElse(null);
        paymentJpaRepository.delete(entity);
    }

    @Override
    public Optional<Payment> findById(UUID uuid) {
        Optional<PaymentEntity> paymentEntity = paymentJpaRepository.findById(uuid);
        if (paymentEntity.isPresent()) {
            PaymentEntity paymentEntity1 = paymentEntity.get();
            Payment payment = PaymentMapper.toDomain(paymentEntity1);
            return Optional.of(payment);
        }
        return Optional.empty();
    }

    @Override
    public List<Payment> findAll() {
        List<PaymentEntity> paymentEntity = paymentJpaRepository.findAll();
        List<Payment> payments = paymentEntity.stream().map(PaymentMapper::toDomain).collect(Collectors.toList());
        return payments;
    }

    @Override
    public List<Payment> findAllById(UUID uuid) {
        List<PaymentEntity> paymentEntities = paymentJpaRepository.findAllById(Collections.singleton(uuid));
        List<Payment> payments = paymentEntities.stream().map(PaymentMapper::toDomain).collect(Collectors.toList());
        return payments;
    }

    @Override
    public Payment remove(Payment id) {
        return null;
    }
}
