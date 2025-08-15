package com.poly.room.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.InventoryItemId;
import com.poly.domain.valueobject.Money;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.valueobject.FurnitureId;

public class Furniture extends BaseEntity<FurnitureId> {

    private String name;
    private Money price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    private Furniture(Builder builder) {
        super.setId(builder.id);
        name = builder.name;
        price = builder.price;
    }

    public static final class Builder {
        private FurnitureId id;
        private String name;
        private Money price;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(FurnitureId val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Furniture build() {
            return new Furniture(this);
        }
    }
}
