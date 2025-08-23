package com.poly.payment.management.domain.service.impl;

import com.poly.payment.management.domain.model.Payment;
import com.poly.payment.management.domain.port.input.service.RetrieveAllPayment;
import com.poly.payment.management.domain.port.output.repository.PaymentRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RetrieveAllPaymentImpl implements RetrieveAllPayment {

    private final PaymentRepository paymentRepository;

    public RetrieveAllPaymentImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<Payment> retrieveAllPayment() {
        return paymentRepository.findAll();
    }
}
