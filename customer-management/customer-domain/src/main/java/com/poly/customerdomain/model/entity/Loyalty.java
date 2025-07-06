package com.poly.customerdomain.model.entity;

import com.poly.customerdomain.model.entity.valueobject.LoyaltyId;
import com.poly.customerdomain.model.entity.valueobject.Point;
import com.poly.domain.valueobject.CustomerId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class Loyalty {

    private LoyaltyId id;
    private final CustomerId customerId;
    private Point points;
    private LocalDateTime lastUpdated;

    private Loyalty(Builder builder) {
        id = builder.id;
        customerId = builder.customerId;
        points = builder.points;
        lastUpdated = builder.lastUpdated;
    }

    public static Loyalty createNew(CustomerId customerId) {
        return new Builder(customerId)
                .id(LoyaltyId.generate())
                .points(Point.zero())
                .lastUpdated(LocalDateTime.now())
                .build();
    }

    public void addPoints(Point amount) {
        this.points = this.points.add(amount);
        this.lastUpdated = LocalDateTime.now();
    }

    public void subtractPoints(Point amount) {
        this.points = this.points.subtract(amount);
        this.lastUpdated = LocalDateTime.now();
    }

    public static final class Builder {
        private LoyaltyId id;
        private final CustomerId customerId;
        private Point points;
        private LocalDateTime lastUpdated;

        public Builder(CustomerId customerId) {
            this.customerId = customerId;
        }

        public Builder id(LoyaltyId val) {
            id = val;
            return this;
        }

        public Builder points(Point val) {
            points = val;
            return this;
        }

        public Builder lastUpdated(LocalDateTime val) {
            lastUpdated = val;
            return this;
        }

        public Loyalty build() {
            return new Loyalty(this);
        }
    }
}
