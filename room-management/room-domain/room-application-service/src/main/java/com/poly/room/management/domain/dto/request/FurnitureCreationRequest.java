package com.poly.room.management.domain.dto.request;

import com.poly.domain.valueobject.Money;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FurnitureCreationRequest {

    private String name;
    private String price;
}
