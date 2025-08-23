package com.poly.ai.management.domain.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomType {

    private String typeName;
    private String description;
    private BigDecimal basePrice;
    private int maxOccupancy;
}
