package com.poly.paymentdomain.model.value_object;

import java.util.concurrent.ThreadLocalRandom;

public class OrderCode {

    private final long value;

    public OrderCode(long value) {
        this.value = value;
    }

    public static OrderCode generate() {
        long code = System.currentTimeMillis() * 1000 + ThreadLocalRandom.current().nextInt(1000);
        return new OrderCode(code);
    }

    public static OrderCode from(long value) {
        return new OrderCode(value);
    }

    public static long to(OrderCode orderCode) {
        return orderCode.getValue();
    }

    public long getValue() {
        return value;
    }

    public static OrderCode empty() {
        return  new OrderCode(00000);
    }
}
