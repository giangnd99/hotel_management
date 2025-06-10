package com.poly.room.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.Money;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.valueobject.FurnitureRequirement;
import com.poly.room.management.domain.valueobject.RoomTypeId;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RoomType extends BaseEntity<RoomTypeId> {

    private String typeName;
    private String description;
    private Money basePrice;
    private int maxOccupancy;
    private List<FurnitureRequirement> furnitures;


    private RoomType(Builder builder) {
        super.setId(builder.id);
        typeName = builder.typeName;
        description = builder.description;
        basePrice = builder.basePrice;
        maxOccupancy = builder.maxOccupancy;
        furnitures = builder.furnitures;
        validateRoomType();
    }


    public void persistFurnitureRequirement(Furniture furniture, int quantityRequired) {
        for (int i = 0; i < furnitures.size(); i++) {
            FurnitureRequirement furnitureRequirement = furnitures.get(i);
            if (furnitureRequirement.equals(furniture)) {
                furnitures.set(i, new FurnitureRequirement(
                        furnitureRequirement.getFurniture(),
                        furnitureRequirement.getRequiredQuantity() + quantityRequired));
                return;
            }
        }
        if (quantityRequired <= 0) {
            quantityRequired = FurnitureRequirement.DEFAULT_QUANTITY;
        }
        furnitures.add(new FurnitureRequirement(furniture, quantityRequired));
    }

    public void removeFurniture(Furniture furniture, int quantityRequired) {
        for (FurnitureRequirement furnitureRequirement : furnitures) {
            if (furnitureRequirement.getFurniture().equals(furniture)) {
                furnitures.remove(furnitureRequirement);
                return;
            }
        }
    }

    public void validateRoomType() {
        checkTypeName();
        checkDescription();
        checkFurnitureRequirements();
    }

    private void checkFurnitureRequirements() {
        if (furnitures == null || furnitures.isEmpty()) {
            throw new RoomDomainException("Furniture requirements list for RoomType cannot be null or empty");
        }
        for (FurnitureRequirement req : furnitures) {
            if (req.getFurniture() == null || req.getFurniture().getId() == null) {
                throw new RoomDomainException("Furniture in requirement cannot be null or have null ID.");
            }
            if (req.getRequiredQuantity() <= 0) {
                throw new RoomDomainException("Required quantity for furniture must be greater than zero.");
            }
        }
    }

    private void checkTypeName() {
        if (typeName.isEmpty() || typeName == null) {
            throw new RoomDomainException("typeName is empty");
        }
        this.typeName = typeName.trim();
    }

    private void checkDescription() {

        if (description.isEmpty() || description == null) {
            throw new RoomDomainException("description is empty");
        }
        this.description = description.trim();
    }

    private void checkBasePriceAndOccupancy() {
        if (basePrice == null || basePrice.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RoomDomainException("Base price must be greater than zero.");
        }
        if (maxOccupancy <= 0) {
            throw new RoomDomainException("Max occupancy must be greater than zero.");
        }
    }

    public RoomType() {
        furnitures = new ArrayList<FurnitureRequirement>();
    }

    public String getTypeName() {
        return typeName;
    }

    public String getDescription() {
        return description;
    }

    public List<FurnitureRequirement> getFurnitures() {
        return furnitures;
    }

    public Money getBasePrice() {
        return basePrice;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public static final class Builder {
        private RoomTypeId id;
        private String typeName;
        private String description;
        private Money basePrice;
        private int maxOccupancy;
        private List<FurnitureRequirement> furnitures;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(RoomTypeId val) {
            id = val;
            return this;
        }

        public Builder typeName(String val) {
            typeName = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder basePrice(Money val) {
            basePrice = val;
            return this;
        }

        public Builder maxOccupancy(int val) {
            maxOccupancy = val;
            return this;
        }

        public Builder furnitures(List<FurnitureRequirement> val) {
            furnitures = val != null ? new ArrayList<>(val) : new ArrayList<>();
            return this;
        }

        public RoomType build() {
            return new RoomType(this);
        }
    }
}
