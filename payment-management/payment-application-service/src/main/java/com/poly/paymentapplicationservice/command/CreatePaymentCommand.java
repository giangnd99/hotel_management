package com.poly.paymentapplicationservice.command;

import com.poly.paymentdomain.model.entity.valueobject.PaymentMethod;
import com.poly.paymentdomain.model.entity.valueobject.PaymentReference;
import com.poly.paymentdomain.model.entity.valueobject.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CreatePaymentCommand {
    private UUID staffId;
    private PaymentStatus paymentStatus;
    private BigDecimal amount;
    private PaymentMethod method;
    private PaymentReference referenceCode;
}
