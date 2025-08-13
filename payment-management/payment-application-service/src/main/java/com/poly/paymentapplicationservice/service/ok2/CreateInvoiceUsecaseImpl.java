package com.poly.paymentapplicationservice.service.ok2;

import com.poly.paymentapplicationservice.dto.command.invoice.CreateInvoiceCommand;
import com.poly.paymentapplicationservice.dto.result.InvoiceResult;
import com.poly.paymentapplicationservice.port.input.ok2.CreateInvoiceUsecase;
import com.poly.paymentdomain.output.InvoicePaymentRepository;
import com.poly.paymentdomain.output.InvoiceRepository;
import com.poly.paymentdomain.output.PaymentRepository;

public class CreateInvoiceUsecaseImpl implements CreateInvoiceUsecase {

    private final InvoiceRepository invoiceRepository;

    private final InvoicePaymentRepository invoicePaymentRepository;

    private final PaymentRepository paymentRepository;

    public CreateInvoiceUsecaseImpl(InvoiceRepository invoiceRepository, InvoicePaymentRepository invoicePaymentRepository, PaymentRepository paymentRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoicePaymentRepository = invoicePaymentRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public InvoiceResult createInvoice(CreateInvoiceCommand command) {
        return null;
    }
}
