package com.poly.paymentapplicationservice.command;

import com.poly.paymentdomain.model.entity.valueobject.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ConfirmDepositPaymentCommand {
    private final PaymentStatus paymentStatus;
    private final String referenceCode;      // Mã giao dịch ngân hàng
    private final long amount;
    private final LocalDateTime transactionDateTime;

}
