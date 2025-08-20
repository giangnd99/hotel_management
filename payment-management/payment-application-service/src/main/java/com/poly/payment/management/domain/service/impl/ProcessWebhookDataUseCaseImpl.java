package com.poly.payment.management.domain.service.impl;


import com.poly.payment.management.domain.dto.request.ConfirmPaymentCommand;
import com.poly.payment.management.domain.message.BookingPaymentResponse;
import com.poly.payment.management.domain.port.input.service.ProcessWebhookDataUseCase;
import com.poly.payment.management.domain.model.Invoice;
import com.poly.payment.management.domain.model.InvoicePayment;
import com.poly.payment.management.domain.model.Payment;
import com.poly.payment.management.domain.port.output.publisher.BookingPaymentReplyPublisher;
import com.poly.payment.management.domain.port.output.repository.InvoicePaymentRepository;
import com.poly.payment.management.domain.port.output.repository.InvoiceRepository;
import com.poly.payment.management.domain.port.output.repository.PaymentRepository;
import com.poly.payment.management.domain.value_object.PaymentStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Component
public class ProcessWebhookDataUseCaseImpl implements ProcessWebhookDataUseCase {

    private final PaymentRepository paymentRepository;

    private final InvoiceRepository invoiceRepository;

    private final InvoicePaymentRepository invoicePaymentRepository;

    private final BookingPaymentReplyPublisher bookingPaymentReplyPublisher;

    public ProcessWebhookDataUseCaseImpl(PaymentRepository paymentRepository, InvoiceRepository invoiceRepository, InvoicePaymentRepository invoicePaymentRepository, BookingPaymentReplyPublisher bookingPaymentReplyPublisher) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoicePaymentRepository = invoicePaymentRepository;
        this.bookingPaymentReplyPublisher = bookingPaymentReplyPublisher;
    }

    @Override
    public void handleProcessWebhook(ConfirmPaymentCommand command) {
        Optional<Payment> paymentOpt = paymentRepository.findByOrderCode(command.getReferenceCode());

        if (paymentOpt.isEmpty()) return;
        if (paymentOpt.get().getStatus().equals(PaymentStatus.PAID)) return;

        Payment payment = paymentOpt.get();

        if (command.isStatus()) {
            payment.markAsPaid(command.getTransactionDateTime());
        } else {
            payment.markAsFailed(command.getTransactionDateTime());
        }

        Optional<InvoicePayment> invoicePaymentOpt = invoicePaymentRepository.findByPaymentId(payment.getId().getValue());

        if (invoicePaymentOpt.isPresent()) {
            InvoicePayment invoicePayment = invoicePaymentOpt.get();

            if (invoicePayment.getInvoiceId() != null && invoicePayment.getInvoiceId().getValue() != null) {
                Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoicePayment.getInvoiceId().getValue());

                if (invoiceOpt.isPresent()) {
                    Invoice invoice = invoiceOpt.get();
                    // Cập nhật trạng thái invoice
                    if (command.isStatus()) {
                        invoice.markAsPaid(command.getTransactionDateTime());
                    } else {
                        invoice.markAsFailed(command.getTransactionDateTime());
                    }
                    invoiceRepository.save(invoice);
                }
            } else {
                log.info("Payment {} không có liên kết Invoice, bỏ qua cập nhật Invoice.", payment.getId().getValue());
            }

            invoicePaymentRepository.save(invoicePayment);

        }

        paymentRepository.save(payment);

        if (payment.getDescription().getValue().contains("Deposit for booking")) {
            BookingPaymentResponse message = createBookingPaymentResponse(payment);
            log.info("Payment {}: {}", payment.getId().getValue(), message);
            bookingPaymentReplyPublisher.publishBookingPaymentReply(message);
            log.info("Payment {}: published", payment.getId().getValue());
        }


        //publish(message);
    }

    private BookingPaymentResponse createBookingPaymentResponse(Payment payment) {
        return BookingPaymentResponse.builder()
                .paymentId(payment.getId().getValue().toString())
                .price(payment.getAmount().getValue())
                .bookingId(payment.getReferenceId().toString())
                .id(UUID.randomUUID().toString())
                .createdAt(Instant.ofEpochSecond(payment.getCreatedAt().getSecond()))
                .failureMessages(List.of(payment.getStatus().name()))
                .paymentStatus(exchangePaymentStatus(payment.getStatus()))
                .build();
    }

    private com.poly.payment.management.domain.message.PaymentStatus exchangePaymentStatus(PaymentStatus paymentStatus) {
        return paymentStatus == PaymentStatus.PAID ?
                com.poly.payment.management.domain.message.PaymentStatus.COMPLETED :
                com.poly.payment.management.domain.message.PaymentStatus.FAILED;
    }
}
