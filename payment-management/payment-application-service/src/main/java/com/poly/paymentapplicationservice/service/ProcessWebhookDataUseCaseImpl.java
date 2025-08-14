package com.poly.paymentapplicationservice.service;

import com.poly.domain.valueobject.PaymentStatus;
import com.poly.paymentapplicationservice.dto.command.ConfirmPaymentCommand;
import com.poly.paymentapplicationservice.port.input.ProcessWebhookDataUseCase;
import com.poly.paymentdomain.model.Invoice;
import com.poly.paymentdomain.model.InvoicePayment;
import com.poly.paymentdomain.model.Payment;
import com.poly.paymentdomain.model.value_object.InvoiceStatus;
import com.poly.paymentdomain.output.InvoicePaymentRepository;
import com.poly.paymentdomain.output.InvoiceRepository;
import com.poly.paymentdomain.output.PaymentRepository;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;

@Log4j2
public class ProcessWebhookDataUseCaseImpl implements ProcessWebhookDataUseCase {

    private final PaymentRepository paymentRepository;

    private final InvoiceRepository invoiceRepository;

    private final InvoicePaymentRepository invoicePaymentRepository;

    public ProcessWebhookDataUseCaseImpl(PaymentRepository paymentRepository, InvoiceRepository invoiceRepository, InvoicePaymentRepository invoicePaymentRepository) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoicePaymentRepository = invoicePaymentRepository;
    }

    @Override
    public void handleProcessWebhook(ConfirmPaymentCommand command) {
        Optional<Payment> paymentOpt = paymentRepository.findByOrderCode(command.getReferenceCode());

        if (paymentOpt.isEmpty()) return;
        if (paymentOpt.get().getStatus().equals(PaymentStatus.PAID)) return ;

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
        //publish(message);
    }
}
