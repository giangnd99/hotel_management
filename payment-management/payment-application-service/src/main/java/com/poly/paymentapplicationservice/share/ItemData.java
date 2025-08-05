package com.poly.paymentapplicationservice.share;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class ItemData {
    private @NonNull String name;
    private @NonNull Integer quantity;
    private @NonNull Integer price;
}
