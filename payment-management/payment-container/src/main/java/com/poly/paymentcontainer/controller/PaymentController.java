    package com.poly.paymentcontainer.controller;

    import com.poly.paymentapplicationservice.dto.command.ConfirmPaymentCommand;
    import com.poly.paymentapplicationservice.dto.command.CreateDepositCommand;
    import com.poly.paymentapplicationservice.dto.command.CreatePaymentCommand;
    import com.poly.paymentapplicationservice.port.input.PaymentUsecase;
    import com.poly.paymentcontainer.dto.CreateDepositCommandController;
    import com.poly.paymentcontainer.dto.PayOSWebhookPayload;
    import com.poly.paymentcontainer.dto.payment.PaymentRequest;
    import com.poly.paymentcontainer.share.ItemRequest;
    import com.poly.paymentdomain.model.entity.valueobject2.PaymentMethod;
    import com.poly.paymentdomain.model.entity.valueobject2.PaymentStatus;
    import com.poly.paymentdomain.model.entity.value_object.PaymentTransactionType;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @Slf4j
    @RestController
    @RequestMapping("/api/payment")
    @RequiredArgsConstructor
    public class PaymentController {

        private final PaymentUsecase paymentUsecase;

        @PostMapping("/deposit")
        public ResponseEntity createDepositLink(@RequestBody CreateDepositCommandController command) throws Exception {
            CreateDepositCommand createPaymentLinkCommand = new CreateDepositCommand();
            createPaymentLinkCommand.setAmount(command.getAmount());
            createPaymentLinkCommand.setMethod(PaymentMethod.PAYOS);
            createPaymentLinkCommand.setQuantity(command.getQuantity());
            createPaymentLinkCommand.setBookingId(command.getBookingId());
            createPaymentLinkCommand.setName(command.getName());
            return ResponseEntity.ok().body(paymentUsecase.makeBookingDeposit(createPaymentLinkCommand));
        }

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

        @PostMapping("/service/online/1")
        public ResponseEntity createServicePurchaseLink(@RequestBody PaymentRequest request) throws Exception {
            CreatePaymentCommand command = CreatePaymentCommand.builder()
                    .amount(request.getAmount())
                    .paymentMethod(request.getPaymentMethod())
                    .serviceType(request.getServiceType())
                    .paymentTransactionType(PaymentTransactionType.OTHER)
                    .items(ItemRequest.mapToItemData(request.getItems()))
                    .note(request.getNote())
                    .build();
            return ResponseEntity.ok().body(paymentUsecase.makeServicePuchardPaymentOnline(command));
        }


        @PostMapping("/webhook/payos")
        public ResponseEntity<Void> handlePayOSWebhook(@RequestBody PayOSWebhookPayload payload) {
            PaymentStatus paymentStatus = new PaymentStatus(PaymentStatus.Status.FAILED);

            if ( payload.getData().getDesc().equals("success")) {
                paymentStatus = new PaymentStatus(PaymentStatus.Status.COMPLETED);
            }

            if (payload.getData().getDesc().equals("failed")) {
                paymentStatus = new PaymentStatus(PaymentStatus.Status.FAILED);
            }

            ConfirmPaymentCommand command = ConfirmPaymentCommand.builder()
                    .paymentStatus(paymentStatus)
                    .referenceCode(payload.getData().getOrderCode())
                    .amount(payload.getData().getAmount())
                    .transactionDateTime(payload.getData().getTransactionDateTime())
                    .build();

            paymentUsecase.handleWebhookPayment(command);
            return ResponseEntity.ok().build();
        }

//        @GetMapping("/return")
//            public ResponseEntity<String> handleReturn(
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
