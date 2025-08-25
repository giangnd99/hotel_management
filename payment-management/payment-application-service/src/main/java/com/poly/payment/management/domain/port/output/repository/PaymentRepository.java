package com.poly.payment.management.domain.port.output.repository;


import com.poly.payment.management.domain.model.Payment;
import com.poly.payment.management.domain.value_object.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends RepositoryGeneric<Payment, UUID> {
    Optional<Payment> findByReferenceId(UUID referenceId);

    Optional<Payment> findByReferenceIdAndStatus(UUID referenceId, PaymentStatus status);

    Optional<Payment> findByOrderCode(long orderCode);

    List<Payment> findAllByStatusAndCreatedAtBefore(PaymentStatus status, LocalDateTime beforeTime);
}