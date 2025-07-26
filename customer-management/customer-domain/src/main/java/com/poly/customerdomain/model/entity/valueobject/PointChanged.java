package com.poly.customerdomain.model.entity.valueobject;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PointChanged {

    private BigDecimal valueChange;

    public PointChanged(BigDecimal valueChange) {
        this.valueChange = valueChange;
    }

    public BigDecimal getValueChange() {
        return valueChange;
    }

    public static PointChanged from(BigDecimal valueChange) {
        return new PointChanged(valueChange);
    }

    public static BigDecimal to(PointChanged pointChanged) {
        return pointChanged.getValueChange();
    }
}
