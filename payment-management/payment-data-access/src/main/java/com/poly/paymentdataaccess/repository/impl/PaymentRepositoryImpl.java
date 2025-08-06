package com.poly.paymentdataaccess.repository.impl;

import com.poly.paymentdataaccess.entity.PaymentEntity;
import com.poly.paymentdataaccess.mapper.PaymentMapper;
import com.poly.paymentdataaccess.repository.PaymentJpaRepository;
import com.poly.paymentdataaccess.share.PaymentStatusEntity;
import com.poly.paymentdataaccess.share.PaymentTransactionTypeEntity;
import com.poly.paymentdomain.model.entity.Payment;
import com.poly.paymentdomain.model.entity.valueobject.PaymentTransactionType;
import com.poly.paymentdomain.output.PaymentRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    public PaymentRepositoryImpl(PaymentJpaRepository paymentJpaRepository) {
        this.paymentJpaRepository = paymentJpaRepository;
    }

    @Override
    @Transactional
    public Payment createPayment(Payment payment) {
        var entity = PaymentMapper.mapToEntity(payment);
        var newEntity = paymentJpaRepository.save(entity);
        return PaymentMapper.mapToDomain(newEntity);
    }

    @Override
    public Payment updatePayment(Payment payment) {
        var entity = PaymentMapper.mapToEntity(payment);
        var newEntity = paymentJpaRepository.save(entity);
        return PaymentMapper.mapToDomain(newEntity);
    }

    @Override
    public void deletePayment(Payment payment) {

    }

    @Override
    public Optional<Payment> findByBookingIdAndType(UUID bookingId, PaymentTransactionType type) {
        PaymentTransactionTypeEntity paymentStatusEntity = PaymentTransactionTypeEntity.DEPOSIT;
        return paymentJpaRepository.findByBookingIdAndPaymentTransactionTypeEntity(bookingId, paymentStatusEntity)
                .map(PaymentMapper::mapToDomain);
    }


    @Override
    public Optional<Payment> findByReferenceCode(String referenceCode) {
        return paymentJpaRepository.findByReferenceCode(referenceCode)
                .map(PaymentMapper::mapToDomain);
    }

    @Override
    public List<Payment> findAllPayments() {
        return List.of();
    }

    @Override
    public List<Payment> findExpiredPendingPayments() {
        return paymentJpaRepository.findExpiredDepositPayments().stream().map(PaymentMapper::mapToDomain).collect(Collectors.toList());
    }
}
