package com.poly.paymentapplicationservice.service;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentapplicationservice.command.CreateInvoiceCommand;
import com.poly.paymentapplicationservice.command.RetrieveInvoiceCommand;
import com.poly.paymentapplicationservice.dto.InvoiceDto;
import com.poly.paymentapplicationservice.dto.PageResult;
import com.poly.paymentapplicationservice.mapper.InvoiceMapper;
import com.poly.paymentapplicationservice.port.input.InvoiceUsecase;
import com.poly.paymentdomain.model.entity.Invoice;
import com.poly.paymentdomain.model.entity.InvoiceItem;
import com.poly.paymentdomain.model.entity.Payment;
import com.poly.paymentdomain.model.entity.valueobject.*;
import com.poly.paymentdomain.output.InvoiceItemRepository;
import com.poly.paymentdomain.output.InvoiceRepository;
import com.poly.paymentdomain.output.PaymentRepository;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class InvoiceApplicationService implements InvoiceUsecase {

    private final InvoiceRepository invoiceRepository;

    private final PaymentRepository paymentRepository;


    public InvoiceApplicationService(InvoiceRepository invoiceRepository, PaymentRepository paymentRepository) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public InvoiceDto makeInvoice(CreateInvoiceCommand command) {

        validateCommand(command);

        Optional<Invoice> existingInvoice = invoiceRepository.findByBookingId(command.getBookingId());

        if (existingInvoice.isPresent()) {
            throw new RuntimeException("Hóa đơn đã tồn tại cho bookingId: " + existingInvoice.get().getBookingId());
        }

        List<InvoiceItem> invoiceItems = InvoiceMapper.mapToInvoiceItems(command.getInvoiceItemCommandList());

        Payment bookingDeposit = paymentRepository.findByBookingIdAndType(command.getBookingId(), PaymentTransactionType.DEPOSIT).orElseThrow();

        Invoice invoiceCreated = Invoice.builder()
                .id(InvoiceId.generate())
                .bookingId(BookingId.from(command.getBookingId()))
                .customerId(CustomerId.fromValue(command.getCustomerId()))
                .createdBy(StaffId.from(command.getStaffIdCreated()))
                .paidAmount(bookingDeposit.getAmount())
                .status(InvoiceStatus.PENDING)
                .taxRate(Money.from(command.getTaxAmount()))
                .voucherId(VoucherId.from(command.getVoucherId()))
                .discountAmount(Money.from(command.getAmountVoucher()))
                .items(invoiceItems)
                .build();

        bookingDeposit.setInvoiceId(invoiceCreated.getId());
        invoiceRepository.createInvoice(invoiceCreated);
        paymentRepository.updatePayment(bookingDeposit);
        return InvoiceMapper.from(invoiceCreated);
    }

    @Override
    public InvoiceDto updateInvoice(CreateInvoiceCommand command) {
        return null;
    }

    @Override
    public PageResult<InvoiceDto> retrieveInvoices(RetrieveInvoiceCommand command) {
        if (command.getCustomerId() == null) throw new RuntimeException("CustomerId không được để trống.");

        List<Invoice> invoiceList = invoiceRepository.findAll(command.getCustomerId());
        int totalItems = invoiceList.size();
        int fromIndex = Math.min(command.getPage() * command.getSize(), totalItems);
        int toIndex = Math.min(fromIndex + command.getSize(), totalItems);
        List<InvoiceDto> pageItems = invoiceList.subList(fromIndex, toIndex)
                .stream()
                .map(InvoiceMapper::from)
                .toList();
        return new PageResult<>(pageItems, command.getPage(), command.getSize(), totalItems);
    }

    private void validateCommand(CreateInvoiceCommand command) {
        if (command.getBookingId() == null) throw new RuntimeException("BookingId không được để trống.");
        if (command.getInvoiceItemCommandList() == null) throw new RuntimeException("Invoice Item không được trống.");
        if (command.getCustomerId() == null) throw new RuntimeException("CustomerId không được để trống.");
    }
}
