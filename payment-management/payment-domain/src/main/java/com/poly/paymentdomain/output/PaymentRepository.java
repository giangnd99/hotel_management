package com.poly.paymentdomain.output;

import com.poly.paymentdomain.model.entity.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends RepositoryGeneric<Payment, UUID> {
    Optional<Payment> findByReferenceId(UUID referenceId);
    Optional<Payment> findByOrderCode(long orderCode);
}