package com.poly.paymentapplicationservice.dto.command.invoice;

import com.poly.paymentdomain.model.entity.value_object.CustomerId;
import com.poly.paymentdomain.model.entity.value_object.Description;
import com.poly.paymentdomain.model.entity.value_object.StaffId;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CreateInvoiceCommand {
    private CustomerId customerId;
    private StaffId createdBy;
    private List<InvoiceItemCommand> items;
    private Description note;
}
