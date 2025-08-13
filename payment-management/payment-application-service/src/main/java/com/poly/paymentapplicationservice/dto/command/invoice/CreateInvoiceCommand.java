package com.poly.paymentapplicationservice.dto.command.invoice;

import com.poly.paymentdomain.model.entity.value_object.CustomerId;
import com.poly.paymentdomain.model.entity.value_object.Description;
import com.poly.paymentdomain.model.entity.value_object.StaffId;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class CreateInvoiceCommand {
    private UUID bookingId;
    private CustomerId customerId;
    private StaffId createdBy;
    private UUID voucherId;
    private BigDecimal voucherAmount;
    private BigDecimal tax;
    private Description note;
}
