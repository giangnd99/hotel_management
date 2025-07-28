package com.poly.paymentdomain.output;

import com.poly.paymentdomain.model.entity.Payment;

import java.util.List;

public interface PaymentRepository {
    Payment createPayment(Payment payment);
    Payment updatePayment(Payment payment);
    void deletePayment(Payment payment);
    Payment findPayment(Payment payment);
    List<Payment> findAllPayments();
}
