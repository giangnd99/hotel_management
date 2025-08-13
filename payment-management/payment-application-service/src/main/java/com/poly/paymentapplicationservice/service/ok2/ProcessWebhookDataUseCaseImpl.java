package com.poly.paymentapplicationservice.service.ok2;

import com.poly.domain.valueobject.PaymentStatus;
import com.poly.paymentapplicationservice.dto.command.ConfirmPaymentCommand;
import com.poly.paymentapplicationservice.port.input.ok2.ProcessWebhookDataUseCase;
import com.poly.paymentdomain.model.entity.Invoice;
import com.poly.paymentdomain.model.entity.InvoicePayment;
import com.poly.paymentdomain.model.entity.Payment;
import com.poly.paymentdomain.model.entity.value_object.InvoiceStatus;
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

//    @Override
//    public void handleProcessWebhook(ConfirmPaymentCommand command) {
//        Optional<Payment> existedPayment = paymentRepository.findByOrderCode(command.getReferenceCode());
//        Optional<InvoicePayment> existedInvoicePayment = Optional.empty();
//        Optional<Invoice> existedInvoice = Optional.empty();
//        if (existedPayment.isPresent()) {
//            if (command.isStatus()) {
//                existedPayment.get().setStatus(PaymentStatus.PAID);
//            } else {
//                existedPayment.get().setStatus(PaymentStatus.FAILED);
//            }
//
//            existedInvoicePayment = invoicePaymentRepository.findByPaymentId(existedPayment.get().getId().getValue());
//
//            if (existedInvoicePayment.isPresent()) {
//
//                if (existedInvoicePayment.get().getInvoiceId() != null) {
//                    existedInvoice = invoiceRepository.findById(existedInvoicePayment.get().getInvoiceId().getValue());
//
//                    if (existedInvoice.isPresent()) {
//                        existedInvoice.get().setStatus(InvoiceStatus.PAID);
//                    } else {
//                        existedInvoice.get().setStatus(InvoiceStatus.FAILED);
//                    }
//                }
//            }
//            paymentRepository.save(existedPayment.get());
//            invoiceRepository.save(existedInvoice.get());
//            invoicePaymentRepository.save(existedInvoicePayment.get());
//        }
//    }

    @Override
    public void handleProcessWebhook(ConfirmPaymentCommand command) {
        Optional<Payment> paymentOpt = paymentRepository.findByOrderCode(command.getReferenceCode());
        if (paymentOpt.isEmpty()) return;

        Payment payment = paymentOpt.get();
        // Cập nhật trạng thái payment
        payment.setStatus(command.isStatus() ? PaymentStatus.PAID : PaymentStatus.FAILED);

        // Cập nhật InvoicePayment
        Optional<InvoicePayment> invoicePaymentOpt = invoicePaymentRepository.findByPaymentId(payment.getId().getValue());
        invoicePaymentOpt.ifPresent(invoicePayment -> {
            // Cập nhật Invoice
            if (invoicePayment.getInvoiceId() != null) {
                invoiceRepository.findById(invoicePayment.getInvoiceId().getValue())
                        .ifPresentOrElse(invoice -> {
                            invoice.setStatus(command.isStatus() ? InvoiceStatus.PAID : InvoiceStatus.FAILED);
                            invoiceRepository.save(invoice);
                        }, () -> {
                            // Nếu invoice không tồn tại, có thể log hoặc xử lý riêng
                            log.warn("Invoice not found for id {}", invoicePayment.getInvoiceId().getValue());
                        });
            }
            invoicePaymentRepository.save(invoicePayment);
        });

        paymentRepository.save(payment);
    }

}
