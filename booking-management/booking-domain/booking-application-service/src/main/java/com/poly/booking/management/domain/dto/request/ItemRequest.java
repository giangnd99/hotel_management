package com.poly.booking.management.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ItemRequest {
    private UUID id;
    private String name;
    private BigDecimal unitPrice;
    private Integer quantity;

    public static List<ItemCommand> mapToItemData(List<ItemRequest> items) {
        return items.stream()
                .map(item -> ItemCommand.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .quantity(item.getQuantity())
                        .amount(item.getUnitPrice())
                        .build())
                .toList();
    }
}
