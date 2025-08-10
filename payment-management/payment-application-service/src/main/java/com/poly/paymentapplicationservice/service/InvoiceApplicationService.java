package com.poly.paymentapplicationservice.service;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentapplicationservice.dto.InvoiceDto;
import com.poly.paymentapplicationservice.dto.PageResult;
import com.poly.paymentapplicationservice.dto.command.RetrieveInvoiceCommand;
import com.poly.paymentapplicationservice.dto.command.invoice.CreateInvoiceCommand;
import com.poly.paymentapplicationservice.dto.command.invoice.InvoiceItemCommand;
import com.poly.paymentapplicationservice.dto.result.CreateInvoiceResult;
import com.poly.paymentapplicationservice.exception.ApplicationServiceException;
import com.poly.paymentapplicationservice.mapper.InvoiceMapper;
import com.poly.paymentapplicationservice.port.input.InvoiceUsecase;
import com.poly.paymentdomain.model.entity.Invoice;
import com.poly.paymentdomain.model.entity.value_object.InvoiceStatus;
import com.poly.paymentdomain.model.entity.value_object.Money;
import com.poly.paymentdomain.output.InvoiceRepository;
import com.poly.paymentdomain.output.PaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class InvoiceApplicationService implements InvoiceUsecase {

    private final InvoiceRepository invoiceRepository;

    private final PaymentRepository paymentRepository;


    public InvoiceApplicationService(InvoiceRepository invoiceRepository, PaymentRepository paymentRepository) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
    }

//    @Override
//    public InvoiceDto makeInvoice(CreateInvoiceCommand command) {
//
//        validateCommand(command);
//
//        Optional<Invoice> existingInvoice = invoiceRepository.findByBookingId(command.getBookingId());
//
//        if (existingInvoice.isPresent()) {
//            throw new RuntimeException("Hóa đơn đã tồn tại cho bookingId: " + existingInvoice.get().getBookingId());
//        }
//
//        List<InvoiceBooking> invoiceBookings = InvoiceMapper.mapToInvoiceItems(command.getInvoiceItemCommandList());
//
//        Payment bookingDeposit = paymentRepository.findByBookingIdAndType(command.getBookingId(), PaymentTransactionType.DEPOSIT).orElseThrow();
//
//        Invoice invoiceCreated = Invoice.builder()
//                .id(InvoiceId.generate())
//                .bookingId(BookingId.from(command.getBookingId()))
//                .customerId(CustomerId.fromValue(command.getCustomerId()))
//                .createdBy(StaffId.from(command.getStaffIdCreated()))
//                .paidAmount(bookingDeposit.getAmount())
//                .status(InvoiceStatus.PENDING)
//                .taxRate(Money.from(command.getTaxAmount()))
//                .voucherId(VoucherId.from(command.getVoucherId()))
//                .discountAmount(Money.from(command.getAmountVoucher()))
//                .items(invoiceBookings)
//                .build();
//
//        bookingDeposit.setInvoiceId(invoiceCreated.getId());
//        invoiceRepository.createInvoice(invoiceCreated);
//        paymentRepository.updatePayment(bookingDeposit);
//        return InvoiceMapper.from(invoiceCreated);
//    }
//
//    @Override
//    public InvoiceDto updateInvoice(CreateInvoiceCommand command) {
//        return null;
//    }
    @Override
    public CreateInvoiceResult createInvoice(CreateInvoiceCommand cmd) {

        Invoice invoice = Invoice.builder()
                .invoiceId(InvoiceId.generate())
                .customerId(cmd.getCustomerId())
                .createdBy(cmd.getCreatedBy())
                .invoiceStatus(InvoiceStatus.DRAFT)
                .subTotal(calculateSubTotal(cmd.getItems()))
                .totalAmount(calculateTotal(cmd.getItems()))
                .createdAt(LocalDateTime.now())
                .note(cmd.getNote())
                .build();

        invoiceRepository.save(invoice);

        return CreateInvoiceResult.builder()
                .invoiceId(invoice.getId().getValue())
                .invoiceStatus(invoice.getInvoiceStatus().name())
                .build();
    }

    @Override
    public PageResult<InvoiceDto> retrieveInvoices(RetrieveInvoiceCommand command) {
        if (command.getCustomerId() == null) throw new RuntimeException("CustomerId không được để trống.");

        List<Invoice> invoiceList = invoiceRepository.findAllById(command.getCustomerId());
        int totalItems = invoiceList.size();
        int fromIndex = Math.min(command.getPage() * command.getSize(), totalItems);
        int toIndex = Math.min(fromIndex + command.getSize(), totalItems);
        List<InvoiceDto> pageItems = invoiceList.subList(fromIndex, toIndex)
                .stream()
                .map(InvoiceMapper::from)
                .toList();
        return new PageResult<>(pageItems, command.getPage(), command.getSize(), totalItems);
    }

    private Money calculateSubTotal(List<InvoiceItemCommand> items) {
        if (items == null || items.isEmpty()) throw new ApplicationServiceException("Invoice items cannot be empty");
        return items.stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity().getValue())))
                .reduce(Money.zero(), Money::add);
    }

    private Money calculateTotal(List<InvoiceItemCommand> items) {
        // Ở đây có thể cộng thêm thuế, phí nếu cần
        return calculateSubTotal(items);
    }
}
