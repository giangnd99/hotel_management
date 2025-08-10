package com.poly.paymentdomain.model.entity.value_object;

import com.poly.paymentdomain.model.exception.InvalidValueException;
import lombok.Getter;

@Getter
public class Description {
    private final String value;

    public Description(String value) {
        if (value == null) throw new InvalidValueException("Description");
        this.value = value;
    }

    public static Description from(String value) {
        return new Description(value);
    }

    public static String to(Description description) {
        return description.getValue();
    }

    public String getValue() {
        return value.toString();
    }

    public static Description empty() {
        return new Description("Empty");
    }
}
