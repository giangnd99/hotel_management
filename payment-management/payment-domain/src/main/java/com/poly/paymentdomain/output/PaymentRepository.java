package com.poly.paymentdomain.output;

import com.poly.paymentdomain.model.entity.Payment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends RepositoryGeneric<Payment, UUID> {
    Optional<Payment> findActiveDepositByBookingId(UUID id);
    Optional<Payment> findByReferenceCode(String referenceCode);
    List<Payment> findExpiredPendingPayments();
}