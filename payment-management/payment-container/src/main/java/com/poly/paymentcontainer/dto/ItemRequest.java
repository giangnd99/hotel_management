package com.poly.paymentcontainer.dto;

import com.poly.paymentapplicationservice.dto.command.ok.ItemCommand;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
public class ItemRequest {
    private UUID id;
    private String name;
    private BigDecimal amount;
    private Integer quantity;

    public static List<ItemCommand> mapToItemData(List<ItemRequest> items) {
        return items.stream()
                .map(item -> ItemCommand.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .quantity(item.getQuantity())
                        .amount(item.getAmount())
                        .build())
                .toList();
    }
}
