    package com.poly.payment.management.application.rest.controller;

    import com.poly.payment.management.domain.dto.ItemData;
    import com.poly.payment.management.domain.dto.request.*;

    import com.poly.payment.management.domain.model.Payment;
    import com.poly.payment.management.domain.port.input.service.*;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.stream.Collectors;

    @Slf4j
    @RestController
    @RequestMapping("/payment")
    @RequiredArgsConstructor
    public class PaymentController {

        private final CreateDepositPaymentLinkUsecase createDepositPaymentLinkUsecase;

        private final ProcessWebhookDataUseCase processWebhookDataUseCase;

        private final CreateInvoicePaymentLinkUsecase createInvoicePaymentLinkUsecase;
        private final CreateDirectPaymentLinkUsecase createDirectPaymentLinkUsecase;

        private final RetrieveAllPayment retrieveAllPaymentLinkUsecase;

        @PostMapping("/deposit")
        public ResponseEntity createDepositLink(@RequestBody CreateDepositRequest request) throws Exception {
            CreatePaymentDepositCommand command = CreatePaymentDepositCommand.builder()
                    .referenceId(request.getReferenceId())
                    .amount(request.getAmount())
                    .items(request.getItems().stream().map(item -> ItemData.builder()
                            .quantity(item.getQuantity())
                            .price(item.getUnitPrice())
                            .name(item.getName())
                            .build()).collect(Collectors.toList()))
                    .description(request.getDescription())
                    .method("PAYOS")
                    .build();
            return ResponseEntity.ok().body(createDepositPaymentLinkUsecase.createPaymentLinkUseCase(command));
        }

        @PostMapping("/invoice/online")
        public ResponseEntity createPaymentInvoiceLink(@RequestBody CreateInvoicePaymentRequest request) throws Exception {
            CreateInvoicePaymentCommand command = CreateInvoicePaymentCommand.builder()
                    .invoiceId(request.getInvoiceId())
                    .build();
            return ResponseEntity.ok().body(createInvoicePaymentLinkUsecase.createPaymentLinkUseCase(command));
        }

        @PostMapping("/direct/online")
        public ResponseEntity createDirectPaymentLink(@RequestBody CreateDirectPaymentRequest request) throws Exception {
            CreateDirectPaymentCommand command = CreateDirectPaymentCommand.builder()
                    .items(request.getItems().stream().map(item -> ItemData.builder()
                            .quantity(item.getQuantity())
                            .price(item.getUnitPrice())
                            .name(item.getName())
                            .build()).collect(Collectors.toList()))
                    .customerId(request.getCustomerId())
                    .staffId(request.getStaffId())
                    .referenceId(request.getReferenceId())
                    .voucherAmount(request.getVoucherAmount())
                    .taxRate(request.getTaxRate())
                    .subTotal(request.getSubTotal())
                    .notes(request.getNotes())
                    .build();
            return ResponseEntity.ok().body(createDirectPaymentLinkUsecase.createPaymentLinkUseCase(command));
        }

        @PostMapping("/webhook/payos")
        public ResponseEntity<Void> handlePayOSWebhook(@RequestBody PayOSWebhookPayloadRequest payload) {
            ConfirmPaymentCommand command = ConfirmPaymentCommand.builder()
                    .status(payload.isSuccess())
                    .referenceCode(Long.parseLong(payload.getData().getOrderCode()))
                    .amount(payload.getData().getAmount())
                    .transactionDateTime(LocalDateTime.now())
                    .build();
            processWebhookDataUseCase.handleProcessWebhook(command);
            return ResponseEntity.ok().build();
        }

        @GetMapping
        public  ResponseEntity<List<Payment>> retrieveAllPayment() {
            return ResponseEntity.ok(retrieveAllPaymentLinkUsecase.retrieveAllPayment());
        }
    }
