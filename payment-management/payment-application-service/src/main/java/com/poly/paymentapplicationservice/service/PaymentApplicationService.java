//package com.poly.paymentapplicationservice.service;
//
//
//import com.poly.domain.valueobject.PaymentStatus;
//import com.poly.paymentapplicationservice.dto.command.ConfirmPaymentCommand;
//import com.poly.paymentapplicationservice.dto.command.ok.CreateDepositCommand;
//import com.poly.paymentapplicationservice.dto.command.CreatePaymentLinkCommand;
//import com.poly.paymentapplicationservice.dto.command.payment.CreateInvoicePaymentLinkCommand;
//import com.poly.paymentapplicationservice.dto.result.PaymentLinkResult;
//import com.poly.paymentapplicationservice.exception.ApplicationServiceException;
//import com.poly.paymentapplicationservice.port.input.PaymentUsecase;
//import com.poly.paymentapplicationservice.port.output.PaymentGateway;
//import com.poly.paymentapplicationservice.share.CheckoutResponseData;
//import com.poly.paymentapplicationservice.share.ItemData;
//import com.poly.paymentdomain.model.entity.Payment;
//import com.poly.paymentdomain.model.entity.value_object.*;
//import com.poly.paymentdomain.output.PaymentRepository;
//import lombok.extern.log4j.Log4j2;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Log4j2
//public class PaymentApplicationService implements PaymentUsecase {
//
//    private final PaymentRepository paymentRepository;
//    private final PaymentGateway payOSClient;
//
//    public PaymentApplicationService(PaymentRepository paymentRepository, PaymentGateway payOS) {
//        this.paymentRepository = paymentRepository;
//        this.payOSClient = payOS;
//    }
//
//    @Override
//    public CheckoutResponseData makeBookingDeposit(CreateDepositCommand command) throws Exception {
//
//        Optional<Payment> existingDeposit = paymentRepository.findByBookingIdAndType(
//                command.getBookingId(),
//                DEPOSIT
//        );
//
//        if (existingDeposit.isPresent()) {
//            throw new ExistingDepositException();
//        }
//
//        Payment newDeposit = Payment.builder()
//                .id(PaymentId.randomPaymentId())
//                .bookingId(BookingId.from(command.getBookingId()))
//                .amount(Money.from(command.getAmount()))
//                .method(command.getMethod())
//                .paymentTransactionType(DEPOSIT)
//                .referenceCode(PaymentReference.generate())
//                .build();
//
//        CreateDepositPaymentLinkCommand comand = CreateDepositPaymentLinkCommand.builder()
//                .referenceCode(Long.valueOf(newDeposit.getReferenceCode().getValue()))
//                .amount(newDeposit.getAmount().getValue())
//                .description("TTDC- " + newDeposit.getReferenceCode().getValue())
//                .items(List.of(
//                        ItemData.builder()
//                                .name(command.getName())
//                                .quantity(command.getQuantity())
//                                .price(command.getAmount())
//                                .build()
//                )).build();
//
//        CheckoutResponseData result = payOS.createDepositPaymentLink(comand);
//        paymentRepository.createPayment(newDeposit);
//
//        //redirect o day
//        return result;
//    }
//
//    @Override
//    public void paymentWebhookService(ConfirmPaymentCommand command) {
//        // 1) Nhận webhook từ payos
//        // 2) Lấy orderCode(reference code)
//        // 3) Tìm payment từ orderCode
//        // 4) Check status của payment đó đã là COMPLETE hay FAILED -> Nếu đã là COMPLETE thì return status, paymentId, bookingId, orderCode
//        // 5) Check xem payment transaction là gì DEPOSIT, INVOICE, SERVICE, REFUND, OTHER
//        // 6) Nếu là:
//        // 6.1) DEPOSIT -> Check trạng thái PayOS trả về là true hay false -> Chuyển status thành COMPLETE hay FAILED
//        // 6.2) INVOICE -> Check trạng thái -> Sửa
//        // 6.3) SERVICE -> Check trạng thái -> Sửa
//        Optional<Payment> payment = paymentRepository.findByReferenceCode(command.getReferenceCode());
//
//        if (payment.isPresent()) {
//            log.info("Webhook đã xử lý giao dịch này rồi: PaymentId %s ".formatted(payment.get().getId()));
//            return;
//        }
//
//        switch (payment.get().getPaymentTransactionType()) {
//            case DEPOSIT -> {
//                payment.get().setStatus(command.getPaymentStatus());
//                break;
//            }
//            case INVOICE -> {
//                payment.get().setStatus(command.getPaymentStatus());
//                break;
//            }
//            case REFUND -> {
//                payment.get().setStatus(command.getPaymentStatus());
//                break;
//            }
//            case OTHER -> {
//                payment.get().setStatus(command.getPaymentStatus());
//                break;
//            }
//            default -> {
//                payment.get().setStatus(PaymentStatus.FAILED);
//            }
//        }
//        paymentRepository.update(payment.get());
//    }
//
//////-------------------
////// Xử lý riêng biệt
////
////    private void handleDeposit(Payment payment, String status, LocalDateTime dateTime) {
////        switch (status) {
////            case "COMPLETED" -> payment.markAsPaid(dateTime);
////            case "FAILED" -> payment.markAsFailed(dateTime);
////            case "CANCELLED" -> payment.markAsCancelled(dateTime);
////            default -> log.warn("Không hỗ trợ trạng thái: {}", status);
////        }
////    }
////
////    private void handleInvoicePayment(Payment payment, String status, LocalDateTime dateTime) {
////        Invoice invoice = invoiceRepository.findInvoiceById(payment.getInvoiceId().getValue())
////                .orElseThrow(InvoiceNotFoundException::new);
////
////        switch (status) {
////            case "COMPLETED" -> {
////                payment.markAsPaid(dateTime);
////                invoice.markAsPaid(dateTime);
////            }
////            case "FAILED" -> payment.markAsFailed(dateTime);
////            case "CANCELLED" -> {
////                payment.markAsCancelled(dateTime);
////                invoice.markAsCancel(dateTime);
////            }
////            default -> {
////                log.warn("Không hỗ trợ trạng thái: {}", status);
////                return;
////            }
////        }
//////        paymentRepository.updatePayment(payment);
////        invoiceRepository.createInvoice(invoice);
////    }
////
////    @Override
////    public void cancelExpiredPayments() throws Exception {
////        List<Payment> pendingPayments = paymentRepository.findExpiredPendingPayments();
////        if (!pendingPayments.isEmpty()) {
////            for (Payment payment : pendingPayments) {
////                if (payment.isExpired()) {
////                    try {
////                        payOS.cancelPaymentLink(Long.valueOf(payment.getReferenceCode().getValue()), "Expired");
////                    } finally {
////                        payment.markAsExpired();
////                    }
////                    paymentRepository.updatePayment(payment);
////                }
////            }
////        }
////    }
////
////    @Override
////    public CheckoutResponseData makeServicePuchardPaymentOnline(CreatePaymentCommand command) throws Exception {
////
////        Invoice newInvoice = Invoice.builder()
////                .id(InvoiceId.generate())
////                .items(InvoiceItemMapper.mapToDomain(command.getItems()))
////                .status(InvoiceStatus.PENDING)
////                .note(Description.from(command.getNote()))
////                .build();
////
////        Payment newPayment = Payment.builder()
////                .id(PaymentId.randomPaymentId())
////                .invoiceId(newInvoice.getId())
////                .amount(newInvoice.getTotalAmount())
////                .method(PaymentMethod.PAYOS)
////                .createdAt(LocalDateTime.now())
////                .paymentTransactionType(command.getPaymentTransactionType())
////                .referenceCode(PaymentReference.generate())
////                .build();
////
////        CreateDepositPaymentLinkCommand comand = CreateDepositPaymentLinkCommand.builder()
////                .referenceCode(Long.valueOf(newPayment.getReferenceCode().getValue()))
////                .amount(newPayment.getAmount().getValue())
////                .description("TTDV-" + newPayment.getReferenceCode().getValue())
////                .items(command.getItems())
////                .build();
////
////        CheckoutResponseData result = payOS.createDepositPaymentLink(comand);
////        invoiceRepository.createInvoice(newInvoice);
////        paymentRepository.createPayment(newPayment);
////
////        return result;
////    }
////
////    @Override
////    public CheckoutResponseData makePaymentCheckoutOnline(CreatePaymentCommand command) throws Exception {
////
//////       Optional<Invoice> existedInvoice = invoiceRepository.findInvoiceById(command.getInvoiceId());
//////
//////       if (existedInvoice.isEmpty()) {
//////           throw new InvoiceNotFoundException();
//////       }
//////
//////        Payment newPayment = Payment.builder()
//////                .id(PaymentId.randomPaymentId())
//////                .invoiceId(existedInvoice.get().getId())
//////                .bookingId(existedInvoice.get().getBookingId())
//////                .amount(existedInvoice.get().getTotalAmount())
//////                .method(command.getMethod())
//////                .paymentTransactionType(INVOICE_PAYMENT)
//////                .referenceCode(PaymentReference.generate())
//////                .build();
//////
//////        CreateDepositPaymentLinkCommand comand = CreateDepositPaymentLinkCommand.builder()
//////                .referenceCode(Long.valueOf(newPayment.getReferenceCode().getValue()))
//////                .amount(newPayment.getAmount().getValue())
//////                .description("TTHD- " + newPayment.getReferenceCode().getValue())
//////                .items(InvoiceItemMapper.mapToEntity(existedInvoice.get().getItems())).build();
//////
//////        CheckoutResponseData result = payOS.createDepositPaymentLink(comand);
//////        paymentRepository.createPayment(newPayment);
//////
//////        return result;
////        return  null;
////    }
//
//    @Override
//    public PaymentLinkResult createDepositLink(CreateDepositCommand cmd) throws Exception {
//        // 1) Chặn trùng: chỉ chặn nếu đã có PENDING/COMPLETED
//        Optional<Payment> existing = paymentRepository
//                .findActiveDepositByBookingId(cmd.getBookingId()); // where type=DEPOSIT and status in (PENDING, COMPLETED)
//        if (existing.isPresent()) {
//            throw new ApplicationServiceException("Deposit already exists for this booking");
//        }
//        // 2) Tạo payment
//        Payment payment = Payment.builder()
//                .paymentId(PaymentId.generate())
//                .bookingId(BookingId.from(cmd.getBookingId()))
//                .amount(Money.from(cmd.getAmount()))
//                .method(cmd.getMethod())
//                .paymentStatus(PaymentStatus.PENDING)
//                .referenceCode(PaymentReference.generate())
//                .createdAt(LocalDateTime.now())
//                .paymentTransactionType(PaymentTransactionType.DEPOSIT)
//                .build();
//        // Tạo 1 đối tượng mà PayOS cần.
//        CreatePaymentLinkCommand createPaymentLinkCommand = CreatePaymentLinkCommand.builder()
//                .referenceCode(payment.getReferenceCode().getValue())
//                .amount(payment.getAmount().getValue())
//                .items(List.of(ItemData.builder()
//                        .name(cmd.getName())
//                        .price(cmd.getAmount())
//                        .quantity(cmd.getQuantity())
//                        .build()))
//                .description("TTDC- " + payment.getReferenceCode().getValue())
//                .build();
//        // Dữ liệu mà PayOS trả về khi tạo thành công hóa đơn
//        CheckoutResponseData paymentLinkResult = payOSClient.createPaymentLink(createPaymentLinkCommand);
//        // Map lại dữ liệu gồm link thanh toán, mã order, trạng thái
//        PaymentLinkResult result = PaymentLinkResult.builder()
//                .orderCode(createPaymentLinkCommand.getReferenceCode())
//                .status(paymentLinkResult.getStatus())
//                .paymentLinkId(paymentLinkResult.getPaymentLinkId())
//                .build();
//        // Lưu lại payment vào cuối để tránh lỗi từ PayOS
//        paymentRepository.save(payment);
//
//        return result;
//    }
//
//    @Override
//    public PaymentLinkResult createInvoicePaymentLink(CreateInvoicePaymentLinkCommand cmd) throws Exception {
//        // Tạo giao dịch
//        Payment payment = Payment.builder()
//                .paymentId(PaymentId.generate())
//                .amount(cmd.getAmount())
//                .method(cmd.getMethod())
//                .paymentStatus(PaymentStatus.PENDING)
//                .referenceCode(PaymentReference.generate())
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        // Gọi PayOS để tạo link
//        CreatePaymentLinkCommand createPaymentLinkCommand = CreatePaymentLinkCommand.builder()
//                .referenceCode(payment.getReferenceCode().hashCode())
//                .amount(payment.getAmount().getValue())
//                .items(cmd.get)
//                .description()
//                .build();
//        CheckoutResponseData paymentLinkResult = payOSClient.createPaymentLink(createPaymentLinkCommand);
//
//        // Mock kết quả từ payos
//        PaymentLinkResult result = PaymentLinkResult.builder()
//                .orderCode(createPaymentLinkCommand.getReferenceCode())
//                .status(paymentLinkResult.getStatus())
//                .paymentLinkId(paymentLinkResult.getPaymentLinkId())
//                .build();
//
//        paymentRepository.save(payment);
//        return result;
//    }
//}
