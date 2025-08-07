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
import com.poly.paymentdomain.model.exception.invoice.InvoiceNotFoundException;
import com.poly.paymentdomain.output.InvoiceRepository;
import com.poly.paymentdomain.output.PaymentRepository;
import lombok.extern.log4j.Log4j2;

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
                .description("TTDC- " + newDeposit.getReferenceCode().getValue())
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
            log.info("Webhook đã xử lý giao dịch này rồi: PaymentId %s ".formatted(payment.getPaymentStatus().getValue()));
            return;
        }

        String status = command.getPaymentStatus().getValue();
        LocalDateTime dateTime = command.getTransactionDateTime();

        // Xử lý theo transaction type
        switch (payment.getPaymentTransactionType().getValue()) {
            case "DEPOSIT" -> handleDeposit(payment, status, dateTime);
            case "INVOICE_PAYMENT", "OTHER" -> handleInvoicePayment(payment, status, dateTime);
            default -> {
                log.warn("Không hỗ trợ transaction type: {}", payment.getPaymentTransactionType());
                return;
            }
        }

        paymentRepository.updatePayment(payment);
    }

//-------------------
// Xử lý riêng biệt

    private void handleDeposit(Payment payment, String status, LocalDateTime dateTime) {
        switch (status) {
            case "COMPLETED" -> payment.markAsPaid(dateTime);
            case "FAILED" -> payment.markAsFailed(dateTime);
            case "CANCELLED" -> payment.markAsCancelled(dateTime);
            default -> log.warn("Không hỗ trợ trạng thái: {}", status);
        }
    }

    private void handleInvoicePayment(Payment payment, String status, LocalDateTime dateTime) {
        Invoice invoice = invoiceRepository.findInvoiceById(payment.getInvoiceId().getValue())
                .orElseThrow(InvoiceNotFoundException::new);

        switch (status) {
            case "COMPLETED" -> {
                payment.markAsPaid(dateTime);
                invoice.markAsPaid(dateTime);
            }
            case "FAILED" -> payment.markAsFailed(dateTime);
            case "CANCELLED" -> {
                payment.markAsCancelled(dateTime);
                invoice.markAsCancel(dateTime);
            }
            default -> {
                log.warn("Không hỗ trợ trạng thái: {}", status);
                return;
            }
        }

        invoiceRepository.createInvoice(invoice);
    }

    @Override
    public void cancelExpiredPayments() throws Exception {
        List<Payment> pendingPayments = paymentRepository.findExpiredPendingPayments();
        if (!pendingPayments.isEmpty()) {
            for (Payment payment : pendingPayments) {
                if (payment.isExpired()) {
                    try {
                        payOS.cancelPaymentLink(Long.valueOf(payment.getReferenceCode().getValue()), "Expired");
                    } finally {
                        payment.markAsExpired();
                    }
                    paymentRepository.updatePayment(payment);
                }
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
                .description("TTDV-" + newPayment.getReferenceCode().getValue())
                .items(command.getItems())
                .build();

        CheckoutResponseData result = payOS.createDepositPaymentLink(comand);
        invoiceRepository.createInvoice(newInvoice);
        paymentRepository.createPayment(newPayment);

        return result;
    }

    @Override
    public CheckoutResponseData makePaymentCheckoutOnline(CreatePaymentCommand command) throws Exception {

       Optional<Invoice> existedInvoice = invoiceRepository.findInvoiceById(command.getInvoiceId());

       if (existedInvoice.isEmpty()) {
           throw new InvoiceNotFoundException();
       }
       
        Payment newPayment = Payment.builder()
                .id(PaymentId.randomPaymentId())
                .invoiceId(existedInvoice.get().getId())
                .bookingId(existedInvoice.get().getBookingId())
                .amount(existedInvoice.get().getTotalAmount())
                .method(command.getMethod())
                .paymentTransactionType(INVOICE_PAYMENT)
                .referenceCode(PaymentReference.generate())
                .build();

        CreateDepositPaymentLinkCommand comand = CreateDepositPaymentLinkCommand.builder()
                .referenceCode(Long.valueOf(newPayment.getReferenceCode().getValue()))
                .amount(newPayment.getAmount().getValue())
                .description("TTHD- " + newPayment.getReferenceCode().getValue())
                .items(InvoiceItemMapper.mapToEntity(existedInvoice.get().getItems())).build();

        CheckoutResponseData result = payOS.createDepositPaymentLink(comand);
        paymentRepository.createPayment(newPayment);

        return result;
    }
}
