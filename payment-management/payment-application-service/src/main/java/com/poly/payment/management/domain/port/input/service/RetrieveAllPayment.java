package com.poly.payment.management.domain.port.input.service;

import com.poly.payment.management.domain.model.Payment;

import java.util.List;

public interface RetrieveAllPayment {
    List<Payment> retrieveAllPayment();
}
