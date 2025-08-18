package com.poly.payment.management.domain.value_object;

import lombok.Getter;

@Getter
public class Description {
    private final String value;

    public Description(String value) {
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
