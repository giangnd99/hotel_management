package com.poly.paymentdomain.output;

import com.poly.paymentdomain.model.entity.Payment;
import com.poly.paymentdomain.model.entity.valueobject.PaymentTransactionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    Payment createPayment(Payment payment);
    Payment updatePayment(Payment payment);
    void deletePayment(Payment payment);
    Optional<Payment> findByBookingIdAndType(UUID bookingId, PaymentTransactionType type);
    Optional<Payment> findByReferenceCode(String referenceCode);
//    Optional<Payment> findBy
    List<Payment> findAllPayments();
    List<Payment> findExpiredPendingPayments();

}
