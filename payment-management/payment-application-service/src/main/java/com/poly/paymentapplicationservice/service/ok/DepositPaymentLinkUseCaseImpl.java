//package com.poly.paymentapplicationservice.service.ok;
//
//import com.poly.domain.valueobject.PaymentStatus;
//import com.poly.paymentapplicationservice.dto.command.ok.CreateDepositCommand;
//import com.poly.paymentapplicationservice.dto.command.CreatePaymentLinkCommand;
//import com.poly.paymentapplicationservice.dto.result.PaymentLinkResult;
//import com.poly.paymentapplicationservice.exception.ApplicationServiceException;
//import com.poly.paymentapplicationservice.port.input.ok.DepositPaymentLinkUseCase;
//import com.poly.paymentapplicationservice.port.output.PaymentGateway;
//import com.poly.paymentapplicationservice.share.CheckoutResponseData;
//import com.poly.paymentapplicationservice.share.ItemData;
//import com.poly.paymentdomain.model.entity.Payment;
//import com.poly.paymentdomain.model.entity.value_object.*;
//import com.poly.paymentdomain.output.PaymentRepository;
//
//import java.math.RoundingMode;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//public class DepositPaymentLinkUseCaseImpl implements DepositPaymentLinkUseCase {
//
//    private final PaymentRepository paymentRepository;
//
//    private final InvoiceBookingRepository invoiceBookingRepository;
//
//    private final PaymentGateway payOSClient;
//
//    public DepositPaymentLinkUseCaseImpl(PaymentRepository paymentRepository, InvoiceBookingRepository invoiceBookingRepository, PaymentGateway payOSClient) {
//        this.paymentRepository = paymentRepository;
//        this.invoiceBookingRepository = invoiceBookingRepository;
//        this.payOSClient = payOSClient;
//    }
//
//    @Override
//    public PaymentLinkResult createDepositLink(CreateDepositCommand cmd) throws Exception {
//        // Chặn trùng: chỉ chặn nếu đã có PENDING/COMPLETED
//        Optional<Payment> existing = paymentRepository.findActiveDepositByBookingId(cmd.getBookingId());
//
//        // Trả về lại link thanh toán cũ nếu giao dịch đang diễn ra ngặp lỗi
//        if (existing.isPresent() && existing.get().getPaymentStatus().equals(PaymentStatus.PENDING)) {
//            return PaymentLinkResult.builder()
//                    .paymentId(existing.get().getId().getValue())
//                    .orderCode(existing.get().getReferenceCode().getValue())
//                    .paymentLink(existing.get().getPaymentLink())
//                    .status(existing.get().getPaymentStatus().name())
//                    .build();
//        }
//
//        // Ném ngoại lệ nếu status = EXPIRED
//        if (existing.isPresent() && existing.get().getPaymentStatus().equals(PaymentStatus.EXPIRED))
//            throw new ApplicationServiceException("Payment is expired");
//
//        // Ném ngoại lệ nếu status = CANCELLED
//        if (existing.isPresent() && existing.get().getPaymentStatus().equals(PaymentStatus.CANCELLED))
//            throw new ApplicationServiceException("Payment is cancelled");
//
//        // Ném ngoại lệ nếu status = COMPLETED
//        if (existing.isPresent() && existing.get().getPaymentStatus().equals(PaymentStatus.COMPLETED))
//            throw new ApplicationServiceException("Deposit already exists for this booking");
//
//        // Tạo payment
//        Payment payment = Payment.builder()
//                .paymentId(PaymentId.generate())
//                .bookingId(BookingId.from(cmd.getBookingId()))
//                .amount(Money.from(cmd.getAmount()))
//                .method(cmd.getMethod())
//                .paymentStatus(PaymentStatus.PENDING)
//                .referenceCode(OrderCode.generate())
//                .createdAt(LocalDateTime.now())
//                .paymentTransactionType(PaymentTransactionType.DEPOSIT)
//                .build();
//
//        // Tạo đối tượng liên kết giữa payment và booking
//        InvoiceBooking invoiceBooking = InvoiceBooking.builder()
//                .id(InvoiceBookingId.generate())
//                .bookingId(BookingId.from(cmd.getBookingId()))
//                .roomName(cmd.getName())
//                .unitPrice(Money.from(cmd.getAmount()))
//                .quantity(Quantity.from(cmd.getQuantity()))
//                .build();
//
//        // Tạo 1 đối tượng mà PayOS cần.
//        CreatePaymentLinkCommand createPaymentLinkCommand = CreatePaymentLinkCommand.builder()
//                .referenceCode(payment.getReferenceCode().getValue())
//                .amount(
//                        payment.getAmount().getValue().setScale(0, RoundingMode.HALF_UP).intValue()
//                )
//                .items(List.of(ItemData.builder()
//                        .name(cmd.getName())
//                        .price(cmd.getAmount())
//                        .quantity(cmd.getQuantity())
//                        .build()))
//                .description("TTDC- " + payment.getReferenceCode().getValue())
//                .build();
//
//        // Dữ liệu mà PayOS trả về khi tạo thành công hóa đơn
//        CheckoutResponseData paymentLinkResult = payOSClient.createPaymentLink(createPaymentLinkCommand);
//
//        // Map lại dữ liệu gồm link thanh toán, mã order, trạng thái
//        PaymentLinkResult result = PaymentLinkResult.builder()
//                .paymentId(payment.getId().getValue())
//                .orderCode(createPaymentLinkCommand.getReferenceCode())
//                .status(paymentLinkResult.getStatus())
//                .paymentLink(paymentLinkResult.getCheckoutUrl())
//                .build();
//
//        // Set lại link thanh toán payment
//        payment.setPaymentLink(result.getPaymentLink());
//
//        // Lưu lại payment vào cuối để tránh lỗi từ PayOS
//        invoiceBookingRepository.save(invoiceBooking);
//        paymentRepository.save(payment);
//
//        // Result trả về: id payment, mã orderCode, link thanh toán , trạng thái
//        return result;
//    }
//}
