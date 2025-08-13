package com.poly.paymentdataaccess.repository.impl;

import com.poly.paymentdataaccess.entity.PaymentEntity;
import com.poly.paymentdataaccess.mapper.PaymentMapper;
import com.poly.paymentdataaccess.repository.PaymentJpaRepository;
import com.poly.paymentdomain.model.entity.Payment;
import com.poly.paymentdomain.output.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Optional<Payment> findActiveDepositByBookingId(UUID id) {
        Optional<PaymentEntity> entity = paymentJpaRepository.findByBookingId(id);
        if(entity.isPresent()){
            return Optional.ofNullable(PaymentMapper.toDomain(entity.get()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Payment> findByReferenceCode(long referenceCode) {
        PaymentEntity entity = paymentJpaRepository.findByReferenceCode(referenceCode);
        return Optional.ofNullable(PaymentMapper.toDomain(entity));
    }

    @Override
    public List<Payment> findExpiredPendingPayments() {
        return List.of();
    }

    @Override
    public Payment save(Payment object) {
        PaymentEntity entity = PaymentMapper.toEntity(object);
        entity = paymentJpaRepository.save(entity);
        return PaymentMapper.toDomain(entity);
    }

    @Override
    public Payment update(Payment object) {
        PaymentEntity entity = PaymentMapper.toEntity(object);
        entity = paymentJpaRepository.save(entity);
        return PaymentMapper.toDomain(entity);
    }

    @Override
    public void delete(UUID uuid) {
        Optional<PaymentEntity> entity = paymentJpaRepository.findById(uuid);
        paymentJpaRepository.delete(entity.get());
    }

    @Override
    public Optional<Payment> findById(UUID uuid) {
        PaymentEntity entity = paymentJpaRepository.findById(uuid).orElse(null);
        return Optional.ofNullable(PaymentMapper.toDomain(entity));
    }

    @Override
    public List<Payment> findAll() {
        List<PaymentEntity> entities = paymentJpaRepository.findAll();
        List<Payment> payments = entities.stream().map(PaymentMapper::toDomain).collect(Collectors.toList());
        return payments;
    }

    @Override
    public List<Payment> findAllById(UUID uuid) {
        List<PaymentEntity> entities = paymentJpaRepository.findAllById(Collections.singleton(uuid));
        List<Payment> payments = entities.stream().map(PaymentMapper::toDomain).collect(Collectors.toList());
        return payments;
    }

    @Override
    public Payment remove(Payment id) {
        return null;
    }
}
