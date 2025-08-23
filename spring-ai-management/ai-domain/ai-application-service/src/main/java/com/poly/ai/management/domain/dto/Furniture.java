package com.poly.ai.management.domain.dto;

import com.poly.domain.valueobject.Money;
import lombok.Data;

@Data
public class Furniture{

    private String name;
    private Money price;
}
