package com.poly.paymentapplicationservice.service;

import com.poly.paymentapplicationservice.command.ConfirmDepositPaymentCommand;
import com.poly.paymentapplicationservice.command.CreateDepositCommand;
import com.poly.paymentapplicationservice.command.CreateDepositPaymentLinkConmand;
import com.poly.paymentapplicationservice.share.CheckoutResponseData;
import com.poly.paymentapplicationservice.port.input.PaymentUsecase;
import com.poly.paymentapplicationservice.port.output.PaymentGateway;
import com.poly.paymentapplicationservice.share.ItemData;
import com.poly.paymentdomain.model.entity.Payment;
import com.poly.paymentdomain.model.entity.valueobject.*;
import com.poly.paymentdomain.model.exception.ExistingDepositException;
import com.poly.paymentdomain.model.exception.PaymentNotFoundException;
import com.poly.paymentdomain.output.PaymentRepository;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Log4j2
public class PaymentApplicationService implements PaymentUsecase {

    private PaymentRepository paymentRepository;
    private final PaymentGateway payOS;

    public PaymentApplicationService(PaymentRepository paymentRepository, PaymentGateway payOS) {
        this.paymentRepository = paymentRepository;
        this.payOS = payOS;
    }

    @Override
    public CheckoutResponseData makeBookingDeposit(CreateDepositCommand command) throws Exception {

        Optional<Payment> existingDeposit = paymentRepository.findByBookingIdAndType(
                command.getBookingId(),
                PaymentTransactionType.DEPOSIT
        );

        if (existingDeposit.isPresent()) {
            throw new ExistingDepositException();
        }
        long referenceCode = System.currentTimeMillis() * 1000 + ThreadLocalRandom.current().nextInt(1000);

        Payment newDeposit = Payment.builder()
                .id(PaymentId.randomPaymentId())
                .bookingId(BookingId.from(command.getBookingId()))
                .amount(Money.from(command.getAmount()))
                .method(command.getMethod())
                .paymentTransactionType(PaymentTransactionType.DEPOSIT)
                .referenceCode(PaymentReference.from(String.valueOf(referenceCode)))
                .build();


        CreateDepositPaymentLinkConmand comand = CreateDepositPaymentLinkConmand.builder()
                .referenceCode(Long.valueOf(newDeposit.getReferenceCode().getValue()))
                .amount(newDeposit.getAmount().getValue())
                .description("Thanh toán đặt cọc.")
                .items(List.of( // <- Chuyển thành List<ItemData>
                        ItemData.builder()
                                .name(command.getName())
                                .quantity(command.getQuantity())
                                .price(command.getAmount().intValue())
                                .build()
                )).build();

        CheckoutResponseData result = payOS.createDepositPaymentLink(comand);
        paymentRepository.createPayment(newDeposit);

        return result;
    }

    @Override
    public void handleWebhookPayment(ConfirmDepositPaymentCommand command) {
        Optional<Payment>paymentOpt = paymentRepository.findByReferenceCode(command.getReferenceCode());
        if (paymentOpt.get().getPaymentStatus().equals(PaymentStatus.Status.COMPLETED) || paymentOpt.get().getPaymentStatus().equals(PaymentStatus.Status.CANCELLED)) {
            log.info("Webhook đã xử lý giao dịch này rồi: {}", command.getPaymentStatus().getValue());
            return;
        }
        if (paymentOpt.isEmpty()) {
            throw new PaymentNotFoundException();
        }

        Payment payment = paymentOpt.get();

        if (command.getPaymentStatus().getValue().equals("COMPLETED")) {
            payment.markAsPaid(command.getTransactionDateTime());
        }

        if (command.getPaymentStatus().getValue().equals("CANCELED")) {
            payment.markAsCancelled(command.getTransactionDateTime());
        }

        if (payment.getPaymentStatus().getValue().equals("EXPIRED") || payment.getPaymentStatus().getValue().equals("FAILED")) {
            payment.markAsFailed(command.getTransactionDateTime());
        }

        paymentRepository.updatePayment(payment); // cập nhật lại trạng thái
    }


    @Override
    public void cancelExpiredPayments() throws Exception {
        List<Payment> pendingPayments = paymentRepository.findExpiredPendingPayments();
        for (Payment payment : pendingPayments) {
            if (payment.isExpired()) {
                payOS.cancelPaymentLink(Long.valueOf(payment.getReferenceCode().getValue()), "Expired");
                payment.markAsExpired();
                paymentRepository.updatePayment(payment);
            }
        }
    }


////    CheckoutResponseData MakeOnlinePayment(CreatePaymentCommand command);

}
