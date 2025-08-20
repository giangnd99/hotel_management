package com.poly.payment.management.data.access.repository.impl;

import com.poly.payment.management.data.access.entity.PaymentEntity;
import com.poly.payment.management.data.access.mapper.PaymentMapper;
import com.poly.payment.management.data.access.repository.PaymentJpaRepository;
import com.poly.payment.management.domain.value_object.PaymentStatus;
import com.poly.payment.management.domain.model.Payment;
import com.poly.payment.management.domain.port.output.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Component( value = "paymentRepository")
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;
    private final PaymentMapper paymentMapper;


    @Override
    public Optional<Payment> findByReferenceId(UUID referenceId) {
        Optional<PaymentEntity> paymentEntity = paymentJpaRepository.findByReferenceId(referenceId);
        if (paymentEntity.isPresent()) {
            PaymentEntity paymentEntity1 = paymentEntity.get();
            Payment payment = paymentMapper.toDomain(paymentEntity1);
            return Optional.of(payment);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Payment> findByOrderCode(long orderCode) {
        Optional<PaymentEntity> paymentEntity = paymentJpaRepository.findByOrderCode(orderCode);
        if (paymentEntity.isPresent()) {
            PaymentEntity paymentEntity1 = paymentEntity.get();
            Payment payment = paymentMapper.toDomain(paymentEntity1);
            return Optional.of(payment);
        }
        return Optional.empty();
    }

    @Override
    public List<Payment> findAllByStatusAndCreatedAtBefore(PaymentStatus status, LocalDateTime beforeTime) {
        List<PaymentEntity> paymentEntities = paymentJpaRepository.findAllByStatusAndCreatedAtBefore(status, beforeTime);
        return paymentEntities.stream().map(paymentMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Payment save(Payment object) {
        PaymentEntity entity = paymentMapper.toEntity(object);
        entity = paymentJpaRepository.save(entity);
        return paymentMapper.toDomain(entity);
    }

    @Override
    public Payment update(Payment object) {
        PaymentEntity entity = paymentMapper.toEntity(object);
        entity = paymentJpaRepository.save(entity);
        return paymentMapper.toDomain(entity);
    }

    @Override
    public void delete(UUID uuid) {
        PaymentEntity entity = paymentJpaRepository.findById(uuid).orElse(null);
        assert entity != null;
        paymentJpaRepository.delete(entity);
    }

    @Override
    public Optional<Payment> findById(UUID uuid) {
        Optional<PaymentEntity> paymentEntity = paymentJpaRepository.findById(uuid);
        if (paymentEntity.isPresent()) {
            PaymentEntity paymentEntity1 = paymentEntity.get();
            Payment payment = paymentMapper.toDomain(paymentEntity1);
            return Optional.of(payment);
        }
        return Optional.empty();
    }

    @Override
    public List<Payment> findAll() {
        List<PaymentEntity> paymentEntity = paymentJpaRepository.findAll();
        return paymentEntity.stream().map(paymentMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Payment> findAllById(UUID uuid) {
        List<PaymentEntity> paymentEntities = paymentJpaRepository.findAllById(Collections.singleton(uuid));
        return paymentEntities.stream().map(paymentMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Payment remove(Payment id) {
        return null;
    }
}
