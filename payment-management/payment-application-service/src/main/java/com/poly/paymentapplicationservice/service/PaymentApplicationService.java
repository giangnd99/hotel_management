package com.poly.paymentapplicationservice.service;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentapplicationservice.command.ConfirmDepositPaymentCommand;
import com.poly.paymentapplicationservice.command.CreateDepositCommand;
import com.poly.paymentapplicationservice.command.CreateDepositPaymentLinkCommand;
import com.poly.paymentapplicationservice.command.CreatePaymentCommand;
import com.poly.paymentapplicationservice.mapper.InvoiceItemMapper;
import com.poly.paymentapplicationservice.share.CheckoutResponseData;
import com.poly.paymentapplicationservice.port.input.PaymentUsecase;
import com.poly.paymentapplicationservice.port.output.PaymentGateway;
import com.poly.paymentapplicationservice.share.ItemData;
import com.poly.paymentdomain.model.entity.Invoice;
import com.poly.paymentdomain.model.entity.Payment;
import com.poly.paymentdomain.model.entity.valueobject.*;
import com.poly.paymentdomain.model.exception.ExistingDepositException;
import com.poly.paymentdomain.model.exception.PaymentNotFoundException;
import com.poly.paymentdomain.output.InvoiceRepository;
import com.poly.paymentdomain.output.PaymentRepository;
import lombok.extern.log4j.Log4j2;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.poly.paymentdomain.model.entity.valueobject.PaymentTransactionType.*;

@Log4j2
public class PaymentApplicationService implements PaymentUsecase {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentGateway payOS;

    public PaymentApplicationService(PaymentRepository paymentRepository, InvoiceRepository invoiceRepository, PaymentGateway payOS) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
        this.payOS = payOS;
    }

    @Override
    public CheckoutResponseData makeBookingDeposit(CreateDepositCommand command) throws Exception {

        Optional<Payment> existingDeposit = paymentRepository.findByBookingIdAndType(
                command.getBookingId(),
                DEPOSIT
        );

        if (existingDeposit.isPresent()) {
            throw new ExistingDepositException();
        }

        Payment newDeposit = Payment.builder()
                .id(PaymentId.randomPaymentId())
                .bookingId(BookingId.from(command.getBookingId()))
                .amount(Money.from(command.getAmount()))
                .method(command.getMethod())
                .paymentTransactionType(DEPOSIT)
                .referenceCode(PaymentReference.generate())
                .build();

        CreateDepositPaymentLinkCommand comand = CreateDepositPaymentLinkCommand.builder()
                .referenceCode(Long.valueOf(newDeposit.getReferenceCode().getValue()))
                .amount(newDeposit.getAmount().getValue())
                .description("Thanh toán đặt cọc.")
                .items(List.of(
                        ItemData.builder()
                                .name(command.getName())
                                .quantity(command.getQuantity())
                                .price(command.getAmount())
                                .build()
                )).build();

        CheckoutResponseData result = payOS.createDepositPaymentLink(comand);
        paymentRepository.createPayment(newDeposit);

        return result;
    }

    @Override
    public void handleWebhookPayment(ConfirmDepositPaymentCommand command) {
        Payment payment = paymentRepository.findByReferenceCode(command.getReferenceCode())
                .orElseThrow(PaymentNotFoundException::new);

        if (payment.isPaid()) {
            log.info("Webhook đã xử lý giao dịch này rồi: {}", command.getPaymentStatus().getValue());
            return;
        }

        switch (command.getPaymentStatus().getValue()) {
            case "COMPLETED":
                payment.markAsPaid(command.getTransactionDateTime());
                break;
            case "FAILED":
                payment.markAsFailed(command.getTransactionDateTime());
                break;
            case "CANCELLED":
                payment.markAsCancelled(command.getTransactionDateTime());
                break;
            default:
                log.warn("Không hỗ trợ xử lý trạng thái: {}", command.getPaymentStatus().getValue());
                return;
        }

        paymentRepository.updatePayment(payment);
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

    @Override
    public CheckoutResponseData makeServicePuchardPaymentOnline(CreatePaymentCommand command) throws Exception {

        Invoice newInvoice = Invoice.builder()
                .id(InvoiceId.generate())
                .createdBy(StaffId.from(command.getStaffId()))
                .items(InvoiceItemMapper.mapToDomain(command.getItems()))
                .status(InvoiceStatus.PENDING)
                .note(Description.from(command.getNote()))
                .build();

        Payment newPayment = Payment.builder()
                .id(PaymentId.randomPaymentId())
                .invoiceId(newInvoice.getId())
                .bookingId(BookingId.from(command.getBookingId()))
                .amount(newInvoice.getTotalAmount())
                .method(PaymentMethod.PAYOS)
                .createdAt(LocalDateTime.now())
                .paymentTransactionType(command.getPaymentTransactionType())
                .referenceCode(PaymentReference.generate())
                .build();

        CreateDepositPaymentLinkCommand comand = CreateDepositPaymentLinkCommand.builder()
                .referenceCode(Long.valueOf(newPayment.getReferenceCode().getValue()))
                .amount(newPayment.getAmount().getValue())
                .description(command.getTypeService() + newPayment.getReferenceCode().getValue())
                .items(command.getItems())
                .build();

        CheckoutResponseData result = payOS.createDepositPaymentLink(comand);
        invoiceRepository.createInvoice(newInvoice);
        paymentRepository.createPayment(newPayment);

        return result;
    }

    @Override
    public CheckoutResponseData makePaymentCheckoutOnline(CreatePaymentCommand command) throws Exception {

//        Option<Invoice> exitstisInvoice = invoiceRepository.findInvoiceById(command.getInvoiceId())
        return null;
    }
}
