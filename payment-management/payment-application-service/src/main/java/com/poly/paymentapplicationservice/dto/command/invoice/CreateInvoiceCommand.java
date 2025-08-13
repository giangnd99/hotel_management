package com.poly.paymentapplicationservice.dto.command.invoice;

import com.poly.paymentdomain.model.entity.value_object.CustomerId;
import com.poly.paymentdomain.model.entity.value_object.Description;
import com.poly.paymentdomain.model.entity.value_object.StaffId;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class CreateInvoiceCommand {
    private UUID bookingId;
    private CustomerId customerId;
    private StaffId staffId;
    private BigDecimal tax;
    private BigDecimal subTotal;
    private BigDecimal totalAmount;
    private Description note;
}
