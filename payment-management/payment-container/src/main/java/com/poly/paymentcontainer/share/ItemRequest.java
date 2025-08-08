package com.poly.paymentcontainer.share;

import com.poly.paymentapplicationservice.share.ItemData;
import com.poly.paymentdataaccess.share.ServiceTypeEntity;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
public class ItemRequest {
    private UUID serviceId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private ServiceTypeEntity serviceType;

    public static List<ItemData> mapToItemData(List<ItemRequest> items) {
        return items.stream()
                .map(item -> ItemData.builder()
                        .serviceId(item.getServiceId())
                        .name(item.getName())
                        .quantity(item.getQuantity())
                        .serviceType(item.getServiceType().name())
                        .price(item.getPrice())
                        .build())
                .toList();
    }


}
