package com.poly.paymentapplicationservice.command;

import com.poly.paymentdomain.model.entity.InvoiceItem;
import com.poly.paymentdomain.model.entity.valueobject.ServiceType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class CreateInvoiceItemCommand {
    private UUID serviceId;
    private String description;
    private ServiceType serviceType;
    private Integer quantity;
    private BigDecimal unitPrice;
    private String note;

    private List<InvoiceItem> mapToInvoiceItems(List<CreateInvoiceItemCommand> commandList) {
        return commandList.stream()
                .map(cmd -> InvoiceItem.builder()
                        .createdAt()
                        .build()
                ).collect(Collectors.toList());
    }

}
