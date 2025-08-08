package com.poly.paymentdomain.model.entity;

import com.poly.paymentdomain.model.entity.valueobject.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class InvoiceItem {
    private ServiceId serviceId;
    private Description description; // Mô tả dịch vụ
    private ServiceType serviceType; // Loại dịch vụ dùng
    private Quantity quantity; // Só lượng
    private Money unitPrice; // Giá trên từng đơn vị
    private LocalDateTime usedAt;
    private Description note;

    private InvoiceItem(Builder builder) {
        this.serviceId = builder.serviceId;
        this.description = builder.description;
        this.serviceType = builder.serviceType;
        this.quantity = builder.quantity;
        this.unitPrice = builder.unitPrice;
        this.usedAt = LocalDateTime.now();
        this.note = builder.note == null ? Description.empty() : builder.note;
    }

    public static final class Builder {
        private ServiceId serviceId;
        private Description description;
        private ServiceType serviceType;
        private Quantity quantity;
        private Money unitPrice;
        private LocalDateTime usedAt;
        private Description note;

        public Builder() {
        }

        public Builder serviceId(ServiceId val) {
            serviceId = val;
            return this;
        }

        public Builder description(Description val) {
            description = val;
            return this;
        }

        public Builder serviceType(ServiceType val) {
            serviceType = val;
            return this;
        }

        public Builder quantity(Quantity val) {
            quantity = val;
            return this;
        }

        public Builder unitPrice(Money val) {
            unitPrice = val;
            return this;
        }

        public Builder usedAt(LocalDateTime val) {
            usedAt = val;
            return this;
        }

        public Builder note(Description val) {
            note = val;
            return this;
        }

        public InvoiceItem build() {
            return new InvoiceItem(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Money amount() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity.getValue())); // Quantity.value() trả int
    }
}
