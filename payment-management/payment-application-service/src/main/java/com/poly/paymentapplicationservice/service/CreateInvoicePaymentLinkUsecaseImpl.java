package com.poly.paymentapplicationservice.service;

import com.poly.domain.valueobject.PaymentMethod;
import com.poly.domain.valueobject.PaymentStatus;
import com.poly.paymentapplicationservice.dto.command.CreateInvoicePaymentCommand;
import com.poly.paymentapplicationservice.dto.command.CreatePaymentLinkCommand;
import com.poly.paymentapplicationservice.dto.result.PaymentLinkResult;
import com.poly.paymentapplicationservice.port.input.CreateInvoicePaymentLinkUsecase;
import com.poly.paymentapplicationservice.port.output.PaymentGateway;
import com.poly.paymentapplicationservice.share.CheckoutResponseData;
import com.poly.paymentapplicationservice.share.ItemData;
import com.poly.paymentdomain.model.Invoice;
import com.poly.paymentdomain.model.InvoicePayment;
import com.poly.paymentdomain.model.Payment;
import com.poly.paymentdomain.model.value_object.Description;
import com.poly.paymentdomain.model.value_object.OrderCode;
import com.poly.paymentdomain.model.value_object.PaymentId;
import com.poly.paymentdomain.output.InvoicePaymentRepository;
import com.poly.paymentdomain.output.InvoiceRepository;
import com.poly.paymentdomain.output.PaymentRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CreateInvoicePaymentLinkUsecaseImpl implements CreateInvoicePaymentLinkUsecase {

    private final PaymentRepository paymentRepository;

    private final InvoicePaymentRepository invoicePaymentRepository;

    private final InvoiceRepository invoiceRepository;

    private final PaymentGateway payOSClient;

    public CreateInvoicePaymentLinkUsecaseImpl(PaymentRepository paymentRepository, InvoicePaymentRepository invoicePaymentRepository, InvoiceRepository invoiceRepository, PaymentGateway payOSClient) {
        this.paymentRepository = paymentRepository;
        this.invoicePaymentRepository = invoicePaymentRepository;
        this.invoiceRepository = invoiceRepository;
        this.payOSClient = payOSClient;
    }

    @Override
    public PaymentLinkResult createPaymentLinkUseCase(CreateInvoicePaymentCommand command) throws Exception {
        Optional<Invoice> existedInvoice = invoiceRepository.findById(command.getInvoiceId());

        if (existedInvoice.isEmpty()) throw new Exception("Invoice not found");

        if (existedInvoice.isPresent() && existedInvoice.get().getStatus().equals(PaymentStatus.PAID)) throw new Exception("Payment already exists");

        Optional<InvoicePayment> existedInvoicePayment = invoicePaymentRepository.findByInvoiceId(existedInvoice.get().getId().getValue());

        if (existedInvoicePayment.isEmpty()) throw new Exception("Invoice not found");

        Invoice invoice = existedInvoice.get();

        InvoicePayment invoicePayment = existedInvoicePayment.get();

        Payment payment = Payment.builder()
                .paymentId(PaymentId.generate())
                .status(PaymentStatus.PENDING)
                .amount(invoice.getTotalAmount())
                .method(PaymentMethod.valueOf(command.getMethod() != null ? command.getMethod() : PaymentMethod.PAYOS.name()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .orderCode(OrderCode.generate())
                .description(Description.from("invoice_payment"))
                .build();

        InvoicePayment newInvoicePayment = InvoicePayment.builder()
                .paymentId(payment.getId())
                .invoiceId(invoice.getId())
                .build();

        // Tạo 1 đối tượng mà PayOS cần.
        CreatePaymentLinkCommand createPaymentLinkCommand = CreatePaymentLinkCommand.builder()
                .orderCode(payment.getOrderCode().getValue())
                .amount(BigDecimal.valueOf(payment.getAmount().getValue().setScale(0, RoundingMode.DOWN).longValue()))
                .items(List.of(ItemData.builder()
                                .price(payment.getAmount().getValue())
                                .name("Hóa đơn: " + payment.getOrderCode().getValue())
                                .quantity(1)
                        .build()))
                .description("TTHD- " + payment.getOrderCode().getValue())
                .build();

        // Dữ liệu mà PayOS trả về khi tạo thành công hóa đơn
        CheckoutResponseData paymentLinkResult = payOSClient.createPaymentLink(createPaymentLinkCommand);

        // Map lại dữ liệu gồm link thanh toán, mã order, trạng thái
        PaymentLinkResult result = PaymentLinkResult.builder()
                .paymentId(payment.getId().getValue())
                .orderCode(createPaymentLinkCommand.getOrderCode())
                .status(paymentLinkResult.getStatus())
                .paymentLink(paymentLinkResult.getCheckoutUrl())
                .build();

        // Set lại link thanh toán payment
        payment.setPaymentLink(result.getPaymentLink());

        // Lưu lại payment vào cuối để tránh lỗi từ PayOS
        invoicePaymentRepository.save(newInvoicePayment);
        paymentRepository.save(payment);

        // Result trả về: id payment, mã orderCode, link thanh toán , trạng thái
        return result;
    }
}
