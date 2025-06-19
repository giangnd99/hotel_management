package com.poly.room.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.Money;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.valueobject.FurnitureId;
import com.poly.room.management.domain.valueobject.RoomTypeId;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RoomType extends BaseEntity<RoomTypeId> {

    private String typeName;
    private String description;
    private Money basePrice;
    private int maxOccupancy;
    private List<FurnitureRequirement> furnitures;

    /**
     * Constructor mặc định.
     */
    public RoomType() {
        this.furnitures = new ArrayList<>();
    }

    /**
     * Constructor đầy đủ để tạo một loại phòng.
     *
     * @param typeName     Tên loại phòng.
     * @param description  Mô tả loại phòng.
     * @param basePrice    Giá cơ bản.
     * @param maxOccupancy Sức chứa tối đa.
     * @param furnitures   Danh sách các yêu cầu nội thất.
     */
    public RoomType(String typeName, String description, Money basePrice, int maxOccupancy, List<FurnitureRequirement> furnitures) {
        this.typeName = typeName;
        this.description = description;
        this.basePrice = basePrice;
        this.maxOccupancy = maxOccupancy;
        this.furnitures = furnitures != null ? new ArrayList<>(furnitures) : new ArrayList<>();
        validateRoomType();
    }

    private RoomType(Builder builder) {
        super.setId(builder.id);
        setTypeName(builder.typeName);
        setDescription(builder.description);
        setBasePrice(builder.basePrice);
        setMaxOccupancy(builder.maxOccupancy);
        furnitures = builder.furnitures;
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
     * @param furnitureRequirement Yêu cầu nội thất cần thêm.
     * @throws RoomDomainException Nếu yêu cầu nội thất đã tồn tại hoặc không hợp lệ.
     */
    public void addFurnitureRequirement(FurnitureRequirement furnitureRequirement) {
        if (furnitureRequirement == null || furnitureRequirement.getFurniture() == null || furnitureRequirement.getFurniture().getId() == null) {
            throw new RoomDomainException("Furniture requirement cannot be null or have null furniture ID.");
        }
        boolean exists = furnitures.stream()
                .anyMatch(f -> f.getFurniture().getId().equals(furnitureRequirement.getFurniture().getId()));
        if (exists) {
            throw new RoomDomainException("Furniture with ID " + furnitureRequirement.getFurniture().getId().getValue() + " already exists in this RoomType.");
        }
        this.furnitures.add(furnitureRequirement);
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
        Optional<FurnitureRequirement> existing = furnitures.stream()
                .filter(f -> f.getFurniture().getId().equals(furnitureId))
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
        boolean removed = this.furnitures.removeIf(f -> f.getFurniture().getId().equals(furnitureId));
        if (!removed) {
            throw new RoomDomainException("Furniture with ID " + furnitureId.getValue() + " not found in this RoomType to remove.");
        }
        // Có thể thêm logic: nếu sau khi xóa danh sách trống và nghiệp vụ yêu cầu không được rỗng
        // if (furnitures.isEmpty()) { ... }
    }

    /**
     * Kiểm tra danh sách yêu cầu nội thất.
     *
     * @throws RoomDomainException Nếu danh sách null hoặc rỗng, hoặc có yêu cầu không hợp lệ.
     */
    private void checkFurnitureRequirements() {
        if (furnitures == null || furnitures.isEmpty()) {
            throw new RoomDomainException("Furniture requirements list for RoomType cannot be null or empty");
        }
        for (FurnitureRequirement req : furnitures) {
            // Validate từng FurnitureRequirement (FurnitureRequirement tự validate khi tạo)
            if (req.getFurniture() == null || req.getFurniture().getId() == null) {
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

    public List<FurnitureRequirement> getFurnitures() {
        return Collections.unmodifiableList(furnitures); // Trả về danh sách không thể sửa đổi bên ngoài
    }

    public Money getBasePrice() {
        return basePrice;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    // Setters (Nếu thay đổi sau khi tạo, cần validate lại)
    public void setTypeName(String typeName) {
        this.typeName = typeName;
        checkTypeName();
    }

    public void setDescription(String description) {
        this.description = description;
        checkDescription();
    }

    public void setBasePrice(Money basePrice) {
        this.basePrice = basePrice;
        checkBasePriceAndOccupancy();
    }

    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
        checkBasePriceAndOccupancy();
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
            furnitures = val;
            return this;
        }

        public RoomType build() {
            return new RoomType(this);
        }
    }
}
