package com.poly.room.management.dao.room.mapper;

import com.poly.domain.valueobject.Money;
import com.poly.room.management.dao.room.entity.FurnitureEntity;
import com.poly.room.management.dao.room.entity.RoomTypeEntity;
import com.poly.room.management.dao.room.entity.RoomTypeFurnitureEntity;
import com.poly.room.management.dao.room.repository.RoomTypeFurnitureJpaRepository;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.entity.RoomTypeFurniture;
import com.poly.room.management.domain.valueobject.FurnitureId;
import com.poly.room.management.domain.valueobject.FurnitureRequirementId;
import com.poly.room.management.domain.valueobject.RoomTypeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomTypeMapper {

    private final RoomTypeFurnitureJpaRepository repository;

    public RoomType toDomainEntity(RoomTypeEntity entity) {
        Money basePrice = new Money(entity.getBasePrice());
        return RoomType.Builder.builder()
                .id(new RoomTypeId(entity.getRoomTypeId()))
                .typeName(entity.getTypeName())
                .basePrice(basePrice)
                .description(entity.getDescription())
                .maxOccupancy(entity.getMaxOccupancy())
                .furnitures(entity.getFurnituresRequirements().stream()
                        .map(this::toDomain).toList())
                .build();
    }

    private RoomTypeFurniture toDomain(RoomTypeFurnitureEntity entity) {
        return RoomTypeFurniture.Builder.builder()
                .id(new FurnitureRequirementId(entity.getRoomTypeFurnitureId()))
                .furniture(Furniture.Builder.builder()
                        .id(new FurnitureId(entity.getFurniture().getFurnitureId()))
                        .name(entity.getFurniture().getName())
                        .price(new Money(entity.getFurniture().getPrice()))
                        .build())
                .roomType(RoomType.Builder.builder()
                        .id(new RoomTypeId(entity.getRoomType().getRoomTypeId()))
                        .typeName(entity.getRoomType().getTypeName())
                        .basePrice(new Money(entity.getRoomType().getBasePrice()))
                        .maxOccupancy(entity.getRoomType().getMaxOccupancy())
                        .description(entity.getRoomType().getDescription())
                        .build())
                .requiredQuantity(entity.getRequireQuantity())
                .build();
    }

    public RoomTypeEntity toEntity(RoomType domain) {
        return RoomTypeEntity.builder()
                .roomTypeId(domain.getId().getValue())
                .furnituresRequirements(domain.getFurnituresRequirements()
                        .stream()
                        .map(this::toEntity)
                        .toList())
                .typeName(domain.getTypeName())
                .description(domain.getDescription())
                .basePrice(domain.getBasePrice().getAmount())
                .maxOccupancy(domain.getMaxOccupancy())
                .build();
    }

    private RoomTypeFurnitureEntity toEntity(RoomTypeFurniture domain) {
        return RoomTypeFurnitureEntity
                .builder()
                .furniture(FurnitureEntity.builder()
                        .furnitureId(domain.getFurniture().getId().getValue())
                        .name(domain.getFurniture().getName())
                        .price(domain.getFurniture().getPrice().getAmount())
                        .build())
                .roomType(RoomTypeEntity.builder()
                        .roomTypeId(domain.getFurniture().getId().getValue())
                        .typeName(domain.getFurniture().getName())
                        .basePrice(domain.getFurniture().getPrice().getAmount())
                        .build())
                .build();
    }
}
