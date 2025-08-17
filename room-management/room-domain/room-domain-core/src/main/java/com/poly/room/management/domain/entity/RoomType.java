package com.poly.room.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.Money;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.valueobject.FurnitureId;
import com.poly.room.management.domain.valueobject.RoomTypeId;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RoomType extends BaseEntity<RoomTypeId> {

    private String typeName;
    private String description;
    private Money basePrice;
    private int maxOccupancy;
    private List<RoomTypeFurniture> furnituresRequirements;

    private RoomType(Builder builder) {
        super.setId(builder.id);
        setTypeName(builder.typeName);
        setDescription(builder.description);
        setBasePrice(builder.basePrice);
        setMaxOccupancy(builder.maxOccupancy);
        furnituresRequirements = builder.furnitures;
    }

    /**
     * Phương thức validate tổng thể cho entity RoomType.
     *
     * @throws RoomDomainException Nếu dữ liệu không hợp lệ.
     */
    public void validateRoomType() {
        checkTypeName();
        checkDescription();
        checkBasePriceAndOccupancy();
        checkFurnitureRequirements();
    }

    /**
     * Thêm một yêu cầu nội thất vào loại phòng.
     *
     * @param roomTypeFurniture Yêu cầu nội thất cần thêm.
     * @throws RoomDomainException Nếu yêu cầu nội thất đã tồn tại hoặc không hợp lệ.
     */
    public void addFurnitureRequirement(RoomTypeFurniture roomTypeFurniture) {
        if (roomTypeFurniture == null || roomTypeFurniture.getFurniture() == null) {
            throw new RoomDomainException("Furniture requirement cannot be null or have null furniture ID.");
        }
        boolean exists = furnituresRequirements.stream()
                .anyMatch(f -> f.getFurniture().equals(roomTypeFurniture.getFurniture()));
        if (exists) {
            throw new RoomDomainException("Furniture with ID " + roomTypeFurniture.getFurniture().getId().getValue().toString() + " already exists in this RoomType.");
        }
        this.furnituresRequirements.add(roomTypeFurniture);
        checkFurnitureRequirements();
    }

    /**
     * Cập nhật số lượng yêu cầu cho một loại nội thất.
     *
     * @param furnitureId ID của nội thất.
     * @param newQuantity Số lượng mới.
     * @throws RoomDomainException Nếu nội thất không tồn tại hoặc số lượng không hợp lệ.
     */
    public void updateFurnitureRequirementQuantity(FurnitureId furnitureId, int newQuantity) {
        if (newQuantity <= 0) {
            throw new RoomDomainException("New quantity must be greater than zero.");
        }
        Optional<RoomTypeFurniture> existing = furnituresRequirements.stream()
                .filter(f -> f.getFurniture().equals(furnitureId))
                .findFirst();
        if (existing.isPresent()) {
            existing.get().setRequiredQuantity(newQuantity);
        } else {
            throw new RoomDomainException("Furniture with ID " + furnitureId.getValue() + " not found in this RoomType.");
        }
    }

    /**
     * Xóa một yêu cầu nội thất khỏi loại phòng.
     *
     * @param furnitureId ID của nội thất cần xóa.
     * @throws RoomDomainException Nếu nội thất không tồn tại trong loại phòng này.
     */
    public void removeFurnitureRequirement(FurnitureId furnitureId) {
        boolean removed = this.furnituresRequirements.removeIf(f -> f.getFurniture().equals(furnitureId));
        if (!removed) {
            throw new RoomDomainException("Furniture with ID " + furnitureId.getValue() + " not found in this RoomType to remove.");
        }
        // Có thể thêm logic: nếu sau khi xóa danh sách trống và nghiệp vụ yêu cầu không được rỗng
        // if (furnituresRequirements.isEmpty()) { ... }
    }

    /**
     * Kiểm tra danh sách yêu cầu nội thất.
     *
     * @throws RoomDomainException Nếu danh sách null hoặc rỗng, hoặc có yêu cầu không hợp lệ.
     */
    private void checkFurnitureRequirements() {
        if (furnituresRequirements == null || furnituresRequirements.isEmpty()) {
            throw new RoomDomainException("Furniture requirements list for RoomType cannot be null or empty");
        }
        for (RoomTypeFurniture req : furnituresRequirements) {
            // Validate từng FurnitureRequirement (FurnitureRequirement tự validate khi tạo)
            if (req.getFurniture() == null) {
                throw new RoomDomainException("Furniture in requirement cannot be null or have null ID.");
            }
            if (req.getRequiredQuantity() <= 0) { // Mặc dù FurnitureRequirement đã validate, double check ở đây.
                throw new RoomDomainException("Required quantity for furniture must be greater than zero.");
            }
        }
    }

    /**
     * Kiểm tra tên loại phòng.
     *
     * @throws RoomDomainException Nếu tên null hoặc rỗng.
     */
    private void checkTypeName() {
        if (typeName == null || typeName.trim().isEmpty()) { // Kiểm tra null trước khi trim
            throw new RoomDomainException("RoomType name cannot be null or empty");
        }
        this.typeName = typeName.trim(); // Trim sau khi kiểm tra
    }

    /**
     * Kiểm tra mô tả loại phòng.
     *
     * @throws RoomDomainException Nếu mô tả null hoặc rỗng.
     */
    private void checkDescription() {
        if (description == null || description.trim().isEmpty()) { // Kiểm tra null trước khi trim
            throw new RoomDomainException("Description cannot be null or empty");
        }
        this.description = description.trim(); // Trim sau khi kiểm tra
    }

    /**
     * Kiểm tra giá cơ bản và sức chứa tối đa.
     *
     * @throws RoomDomainException Nếu giá hoặc sức chứa không hợp lệ.
     */
    public void checkBasePriceAndOccupancy() {
        // Giả định Money.getAmount() trả về BigDecimal
        if (basePrice == null || basePrice.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RoomDomainException("Base price must be greater than zero.");
        }
        if (maxOccupancy <= 0) {
            throw new RoomDomainException("Max occupancy must be greater than zero.");
        }
    }

    // Getters
    public String getTypeName() {
        return typeName;
    }

    public String getDescription() {
        return description;
    }

    public List<RoomTypeFurniture> getFurnituresRequirements() {
        return Collections.unmodifiableList(furnituresRequirements); // Trả về danh sách không thể sửa đổi bên ngoài
    }

    public Money getBasePrice() {
        return basePrice;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBasePrice(Money basePrice) {
        this.basePrice = basePrice;
    }

    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    public static final class Builder {
        private RoomTypeId id;
        private String typeName;
        private String description;
        private Money basePrice;
        private int maxOccupancy;
        private List<RoomTypeFurniture> furnitures;

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

        public Builder furnitures(List<RoomTypeFurniture> val) {
            furnitures = val;
            return this;
        }

        public RoomType build() {
            return new RoomType(this);
        }
    }
}
