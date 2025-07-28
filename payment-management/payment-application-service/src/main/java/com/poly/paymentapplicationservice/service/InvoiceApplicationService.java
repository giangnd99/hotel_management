package com.poly.paymentapplicationservice.service;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentapplicationservice.command.CreateInvoiceCommand;
import com.poly.paymentapplicationservice.command.CreateInvoiceItemCommand;
import com.poly.paymentapplicationservice.dto.InvoiceDto;
import com.poly.paymentapplicationservice.port.input.InvoiceUsecase;
import com.poly.paymentdomain.model.entity.Invoice;
import com.poly.paymentdomain.model.entity.valueobject.BookingId;
import com.poly.paymentdomain.model.entity.valueobject.CustomerId;
import com.poly.paymentdomain.model.entity.valueobject.StaffId;
import com.poly.paymentdomain.model.entity.valueobject.VoucherId;
import com.poly.paymentdomain.output.InvoiceRepository;

import java.util.UUID;

public class InvoiceApplicationService implements InvoiceUsecase {

    private InvoiceRepository invoiceRepository;

    public InvoiceApplicationService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public InvoiceDto createInvoice(CreateInvoiceCommand command) {

        Invoice invoiceCreated = Invoice.builder()
                .id(InvoiceId.generate())
                .bookingId(BookingId.from(command.getBookingId()))
                .customerId(CustomerId.fromValue(command.getCustomerId()))
                .createdBy(StaffId.from(command.getStaffIdCreated()))
                .voucherId(VoucherId.from(command.getVoucherId()))
                .items(command.getInvoiceItemCommandList())
                .payments(command.getPaymentCommandList())
                .build();

        CreateInvoiceItemCommand createInvoiceItemCommand = invoiceRepository.createInvoice();
        return null;
    }
}
