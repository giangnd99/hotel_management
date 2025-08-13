package com.poly.paymentapplicationservice.service.ok;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentapplicationservice.dto.command.invoice.CreateInvoiceCommand;
import com.poly.paymentapplicationservice.dto.command.invoice.InvoiceItemCommand;
import com.poly.paymentapplicationservice.dto.result.CreateInvoiceResult;
import com.poly.paymentapplicationservice.exception.ApplicationServiceException;
import com.poly.paymentapplicationservice.port.input.ok.CreateFinalInvoiceUseCase;
import com.poly.paymentdomain.model.entity.*;
import com.poly.paymentdomain.model.entity.value_object.InvoiceStatus;
import com.poly.paymentdomain.model.entity.value_object.InvoiceVoucherId;
import com.poly.paymentdomain.model.entity.value_object.Money;
import com.poly.paymentdomain.model.entity.value_object.VoucherId;
import com.poly.paymentdomain.output.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CreateFinalInvoiceUseCaseImpl implements CreateFinalInvoiceUseCase {

    private final InvoiceRepository invoiceRepository;

    private final InvoiceBookingRepository invoiceBookingRepository;

    private final InvoiceRestaurantRepository invoiceRestaurantRepository;

    private final InvoiceVoucherRepository invoiceVoucherRepository;

    private final InvoiceServiceRepository invoiceServiceRepository;

    private final InvoicePaymentRepository invoicePaymentRepository;

    private final PaymentRepository paymentRepository;

    public CreateFinalInvoiceUseCaseImpl(InvoiceRepository invoiceRepository, InvoiceBookingRepository invoiceBookingRepository, InvoiceRestaurantRepository invoiceRestaurantRepository, InvoiceVoucherRepository invoiceVoucherRepository, InvoiceServiceRepository invoiceServiceRepository, InvoicePaymentRepository invoicePaymentRepository, PaymentRepository paymentRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceBookingRepository = invoiceBookingRepository;
        this.invoiceRestaurantRepository = invoiceRestaurantRepository;
        this.invoiceVoucherRepository = invoiceVoucherRepository;
        this.invoiceServiceRepository = invoiceServiceRepository;
        this.invoicePaymentRepository = invoicePaymentRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public CreateInvoiceResult createInvoice(CreateInvoiceCommand cmd) {
        // Nhận vào bookindId, customerId. staffId,
        // Query toàn bộ InvoiceRestaurant, InvoiceService, InvoicePayment, Payment, InvoiceBooking
        // Lấy giá khác cọc ở InvoiceBooking, Tính tổng sản phẩm khách gọi ở InvoiceService, InvoiceRestaurant
        Optional<InvoiceBooking> exitedInvoiceBooking = invoiceBookingRepository.findByBookingId(cmd.getBookingId());

        if (exitedInvoiceBooking.isEmpty()) {
            throw new ApplicationServiceException("Invoice booking not found");
        }

        List<InvoiceRestaurant> invoiceRestaurantList = invoiceRestaurantRepository.findAllByBookingId(cmd.getBookingId());
        Money totalAmountRestaurant = Money.zero();
        if (invoiceRestaurantList.size() > 0) {
            totalAmountRestaurant = invoiceRestaurantList.stream().map(item -> (item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity().getValue())))).reduce(Money.zero(), Money::add);
        }
        Money totalAmountService = Money.zero();
        List<InvoiceService> invoiceServiceList = invoiceServiceRepository.findAllByBookingId(cmd.getBookingId());
        if (invoiceServiceList.size() > 0) {
            totalAmountService = invoiceServiceList.stream().map(item -> (item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity().getValue())))).reduce(Money.zero(), Money::add);
        }

        Money totalSubtotal = Money.zero();
        if (totalAmountRestaurant != Money.zero() || totalAmountService != Money.zero()) {
            totalSubtotal = totalAmountRestaurant.add(totalAmountService);
        }

        Money totalAmount = Money.zero();
        if (totalSubtotal != Money.zero()) {
            totalAmount = totalSubtotal.subtract(cmd.getVoucherAmount() != null ? Money.from(cmd.getVoucherAmount()) : Money.zero()).subtract(exitedInvoiceBooking.get().getUnitPrice());
        }

        Invoice invoice = Invoice.builder()
                .invoiceId(InvoiceId.generate())
                .customerId(cmd.getCustomerId())
                .createdBy(cmd.getCreatedBy())
                .invoiceStatus(InvoiceStatus.DRAFT)
                .subTotal(totalSubtotal)
                .totalAmount(calculateTotal(totalAmount.getValue(), cmd.getTax()))
                .createdAt(LocalDateTime.now())
                .note(cmd.getNote())
                .build();

        InvoiceBooking invoiceBooking = InvoiceBooking.builder()
                .bookingId(exitedInvoiceBooking.get().getBookingId())
                .invoiceId(invoice.getId())
                .quantity(exitedInvoiceBooking.get().getQuantity())
                .unitPrice(exitedInvoiceBooking.get().getUnitPrice())
                .build();

        InvoiceVoucher invoiceVoucher = InvoiceVoucher.builder()
                .invoiceId(invoice.getId())
                .voucherId(VoucherId.from(cmd.getVoucherId()))
                .amount(Money.from(cmd.getVoucherAmount()))
                .id(InvoiceVoucherId.generate())
                .build();

        InvoicePayment invoicePayment = InvoicePayment.builder()

                .build();

        invoiceVoucherRepository.save(invoiceVoucher);
        invoiceRepository.save(invoice);
        invoiceBookingRepository.update(invoiceBooking);

        return CreateInvoiceResult.builder()
                .invoiceId(invoice.getId().getValue())
                .invoiceStatus(invoice.getInvoiceStatus().name())
                .build();
    }

    private Money calculateSubTotal(List<InvoiceItemCommand> items) {
        if (items == null || items.isEmpty()) throw new ApplicationServiceException("Invoice items cannot be empty");
        return items.stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity().getValue())))
                .reduce(Money.zero(), Money::add);
    }

    private Money calculateTotal(BigDecimal totalAmount, BigDecimal tax) {
        BigDecimal taxAmount = tax.divide(BigDecimal.valueOf(100)).multiply(totalAmount);
        BigDecimal total = totalAmount.add(taxAmount);
        return Money.from(total);
    }
}
