package com.poly.room.management.domain.entity;

import com.poly.domain.valueobject.Money;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cost {
    private String id;

    private String referenceId;

    private String name;

    private Money price;
}
