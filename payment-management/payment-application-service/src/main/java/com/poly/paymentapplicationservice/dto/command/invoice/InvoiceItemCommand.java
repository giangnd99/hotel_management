package com.poly.paymentapplicationservice.dto.command.invoice;

import com.poly.paymentdomain.model.entity.value_object.Money;
import com.poly.paymentdomain.model.entity.value_object.Quantity;
import com.poly.paymentdomain.model.entity.value_object.ServiceId;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InvoiceItemCommand {
    private ServiceId serviceId;
    private Quantity quantity;
    private Money unitPrice;
}