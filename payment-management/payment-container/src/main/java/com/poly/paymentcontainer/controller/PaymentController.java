    package com.poly.paymentcontainer.controller;

    import com.poly.domain.valueobject.PaymentMethod;
    import com.poly.paymentapplicationservice.dto.command.ok.CreateDepositCommand;
    import com.poly.paymentapplicationservice.dto.command.ok.CreatePaymentImmediateCommand;
    import com.poly.paymentapplicationservice.port.input.ok.DepositPaymentLinkUseCase;
    import com.poly.paymentapplicationservice.port.input.ok.InvoicePaymentLinkUseCase;
    import com.poly.paymentapplicationservice.port.input.ok.ProcessDirectPaymentUseCase;
    import com.poly.paymentcontainer.dto.CreateDepositRequest;
    import com.poly.paymentcontainer.dto.CreateProcessDirectRequest;
    import com.poly.paymentcontainer.dto.ItemRequest;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @Slf4j
    @RestController
    @RequestMapping("/api/payment")
    @RequiredArgsConstructor
    public class PaymentController {

        private final DepositPaymentLinkUseCase depositPaymentLinkUseCase;

        private final ProcessDirectPaymentUseCase processDirectPaymentUseCase;

        private final InvoicePaymentLinkUseCase invoicePaymentLinkUseCase;

        @PostMapping("/deposit")
        public ResponseEntity createDepositLink(@RequestBody CreateDepositRequest request) throws Exception {
            CreateDepositCommand command = CreateDepositCommand.builder()
                    .bookingId(request.getBookingId())
                    .name(request.getName())
                    .amount(request.getAmount())
                    .quantity(request.getQuantity())
                    .method(PaymentMethod.valueOf(request.getMethod()))
                    .build();
            return ResponseEntity.ok().body(depositPaymentLinkUseCase.createDepositLink(command));
        }

        @PostMapping("/service/restaurant/online")
        public ResponseEntity createProcessDirectPaymentLinkWithRestaurant (@RequestBody CreateProcessDirectRequest request) throws Exception {
            CreatePaymentImmediateCommand command = CreatePaymentImmediateCommand.builder()
                    .items(ItemRequest.mapToItemData(request.getItems()))
                    .staff(request.getStaff())
                    .taxRate(request.getTaxRate())
                    .method(PaymentMethod.PAYOS)
                    .typeSerivce("restaurant")
                    .build();
            return ResponseEntity.ok().body(processDirectPaymentUseCase.CreatePaymentLinkUseCase(command));
        }

        @PostMapping("/service/service/online")
        public ResponseEntity createProcessDirectPaymentLinkWithService (@RequestBody CreateProcessDirectRequest request) throws Exception {
            CreatePaymentImmediateCommand command = CreatePaymentImmediateCommand.builder()
                    .items(ItemRequest.mapToItemData(request.getItems()))
                    .staff(request.getStaff())
                    .taxRate(request.getTaxRate())
                    .method(PaymentMethod.PAYOS)
                    .typeSerivce("service")
                    .build();
            return ResponseEntity.ok().body(processDirectPaymentUseCase.CreatePaymentLinkUseCase(command));
        }

//        @PostMapping("/invoice/online")
//        public ResponseEntity createPaymentLinkWithInvoice () {
//            CreateInvoicePaymentCommand command = CreateInvoicePaymentCommand.builder()
//                    .invoiceId()
//                    .voucherId()
//                    .build();
//            return ResponseEntity.ok().body(invoicePaymentLinkUseCase.CreatePaymentLinkUseCase());
//        }
//    @PostMapping("/webhook/payos")
//    public ResponseEntity<Void> handlePayOSWebhook(@RequestBody PayOSWebhookPayload payload) {
//        PaymentStatus paymentStatus = new PaymentStatus(PaymentStatus.Status.FAILED);
//
//        if (payload.getData().getDesc().equals("success")) {
//            paymentStatus = new PaymentStatus(PaymentStatus.Status.COMPLETED);
//        }
//
//        if (payload.getData().getDesc().equals("failed")) {
//            paymentStatus = new PaymentStatus(PaymentStatus.Status.FAILED);
//        }
//
//        ConfirmPaymentCommand command = ConfirmPaymentCommand.builder()
//                .paymentStatus(paymentStatus)
//                .referenceCode(payload.getData().getOrderCode())
//                .amount(payload.getData().getAmount())
//                .transactionDateTime(payload.getData().getTransactionDateTime())
//                .build();
//
//        paymentUsecase.handleWebhookPayment(command);
//        return ResponseEntity.ok().build();
//    }

//        @PostMapping("/deposit")
//        public ResponseEntity createDepositLink(@RequestBody CreateDepositCommandController command) throws Exception {
//            CreateDepositCommand createPaymentLinkCommand = new CreateDepositCommand();
//            createPaymentLinkCommand.setAmount(command.getAmount());
//            createPaymentLinkCommand.setMethod(PaymentMethod.PAYOS);
//            createPaymentLinkCommand.setQuantity(command.getQuantity());
//            createPaymentLinkCommand.setBookingId(command.getBookingId());
//            createPaymentLinkCommand.setName(command.getName());
//            return ResponseEntity.ok().body(paymentUsecase.makeBookingDeposit(createPaymentLinkCommand));
//        }
//
//        @PostMapping("/checkout/online")
//        public ResponseEntity createCheckoutOnlineLink(@RequestBody CreatePaymentRequest request) throws Exception {
//            CreatePaymentCommand command = CreatePaymentCommand.builder()
//                    .invoiceId(request.getInvoiceId())
//                    .staffId(request.getStaffId())
//                    .method(PaymentMethod.PAYOS)
//                    .paymentTransactionType(PaymentTransactionType.INVOICE_PAYMENT)
//                    .build();
//            return ResponseEntity.ok().body(paymentUsecase.makePaymentCheckoutOnline(command));
//        }
//
//        @PostMapping("/service/online")
//        public ResponseEntity createServicePurchaseLink(@RequestBody CreateServicePurchadPaymentRequest request) throws Exception {
//            CreatePaymentCommand command = CreatePaymentCommand.builder()
//                    .bookingId(request.getBookingId())
//                    .invoiceId(request.getInvoiceId())
//                    .method(PaymentMethod.PAYOS)
//                    .staffId(request.getStaffId())
//                    .paymentTransactionType(PaymentTransactionType.OTHER)
//                    .items(ItemRequest.mapToItemData(request.getItems()))
//                    .note(request.getNote())
//                    .typeService("TTDV- ")
//                    .build();
//            return ResponseEntity.ok().body(paymentUsecase.makeServicePuchardPaymentOnline(command));
//        }
//
//        @PostMapping("/service/online")
//        public ResponseEntity createServicePurchaseLink(@RequestBody PaymentRequest request) throws Exception {
//            CreatePaymentCommand command = CreatePaymentCommand.builder()
//                    .amount(request.getAmount())
//                    .paymentMethod(request.getPaymentMethod())
//                    .serviceType(request.getServiceType())
//                    .paymentTransactionType(PaymentTransactionType.OTHER)
//                    .items(ItemRequest.mapToItemData(request.getItems()))
//                    .note(request.getNote())
//                    .build();
//            return ResponseEntity.ok().body(paymentUsecase.makeServicePuchardPaymentOnline(command));
//        }
//
//
//
//        @GetMapping("/return")
//        public ResponseEntity<String> handleReturn(
//                @RequestParam String code,
//                @RequestParam String id,
//                @RequestParam boolean cancel,
//                @RequestParam String status,
//                @RequestParam long orderCode
//        ) {
//            // Xử lý tại đây
//            return ResponseEntity.ok("Return received!");
//        }

    }
