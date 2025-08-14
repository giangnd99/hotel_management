package com.poly.paymentdomain.output;

import com.poly.domain.valueobject.PaymentStatus;
import com.poly.paymentdomain.model.Payment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends RepositoryGeneric<Payment, UUID> {
    Optional<Payment> findByReferenceId(UUID referenceId);
    Optional<Payment> findByOrderCode(long orderCode);
    List<Payment> findAllByStatusAndCreatedAtBefore(PaymentStatus status, LocalDateTime beforeTime);
}