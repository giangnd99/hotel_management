    package com.poly.paymentcontainer.controller;

    import com.poly.paymentapplicationservice.command.ConfirmDepositPaymentCommand;
    import com.poly.paymentapplicationservice.command.CreateDepositCommand;
    import com.poly.paymentapplicationservice.dto.CreatePaymentLinkCommand;
    import com.poly.paymentapplicationservice.port.input.PaymentUsecase;
    import com.poly.paymentcontainer.dto.CreateDepositCommandController;
    import com.poly.paymentcontainer.dto.PayOSWebhookPayload;
    import com.poly.paymentdomain.model.entity.valueobject.PaymentMethod;
    import com.poly.paymentdomain.model.entity.valueobject.PaymentStatus;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    @Slf4j
    @RestController
    @RequestMapping("/api/payment")
    @RequiredArgsConstructor
    public class PaymentController {

        private final PaymentUsecase paymentUsecase;

        @PostMapping("/deposit")
        public ResponseEntity createPaymentLink(@RequestBody CreateDepositCommandController command) throws Exception {
            CreateDepositCommand createPaymentLinkCommand = new CreateDepositCommand();
            createPaymentLinkCommand.setAmount(command.getAmount());
            createPaymentLinkCommand.setMethod(PaymentMethod.from(command.getMethod().name()));
            createPaymentLinkCommand.setQuantity(command.getQuantity());
            createPaymentLinkCommand.setBookingId(command.getBookingId());
            createPaymentLinkCommand.setName(command.getName());
            return ResponseEntity.ok().body(paymentUsecase.makeBookingDeposit(createPaymentLinkCommand));
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

            ConfirmDepositPaymentCommand command = ConfirmDepositPaymentCommand.builder()
                    .paymentStatus(paymentStatus)
                    .referenceCode(payload.getData().getOrderCode())
                    .amount(payload.getData().getAmount())
                    .transactionDateTime(payload.getData().getTransactionDateTime())
                    .build();


            paymentUsecase.handleWebhookPayment(command);
            return ResponseEntity.ok().build();
        }

    }
