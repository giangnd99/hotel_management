package com.poly.paymentapplicationservice.command;

import com.poly.paymentapplicationservice.share.ItemData;
import com.poly.paymentdomain.model.entity.valueobject.PaymentMethod;
import com.poly.paymentdomain.model.entity.valueobject.PaymentReference;
import com.poly.paymentdomain.model.entity.valueobject.PaymentStatus;
import com.poly.paymentdomain.model.entity.valueobject.PaymentTransactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class CreatePaymentCommand {
    private UUID bookingId;
    private UUID invoiceId;
    private UUID staffId;
    private UUID voucherId;
    private BigDecimal amountVoucher;
    private PaymentMethod method;
    private PaymentTransactionType paymentTransactionType;
    private List<ItemData> items;
    private String note;
    private String typeService;
}
