package com.poly.paymentapplicationservice.service;

import com.poly.paymentapplicationservice.command.CreatePaymentCommand;
import com.poly.paymentapplicationservice.dto.PaymentDto;
import com.poly.paymentapplicationservice.mapper.PaymentMapper;
import com.poly.paymentapplicationservice.port.input.PaymentUsecase;
import com.poly.paymentdomain.model.entity.Payment;
import com.poly.paymentdomain.model.entity.valueobject.Money;
import com.poly.paymentdomain.model.entity.valueobject.StaffId;
import com.poly.paymentdomain.output.PaymentRepository;

public class PaymentApplicationService implements PaymentUsecase {

    private PaymentRepository paymentRepository;

    public PaymentApplicationService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentDto makePayment(CreatePaymentCommand createPaymentCommand) {
        Payment payment = Payment.builder()
                .staffId(StaffId.from(createPaymentCommand.getStaffId()))
                .paymentStatus(createPaymentCommand.getPaymentStatus())
                .amount(Money.from(createPaymentCommand.getAmount()))
                .method(createPaymentCommand.getMethod())
                .paidAt(createPaymentCommand.getPaidAt())
                .referenceCode(createPaymentCommand.getReferenceCode())
                .build();
        paymentRepository.createPayment(payment);

        return PaymentMapper.toPaymentDto(payment);
    }
}
