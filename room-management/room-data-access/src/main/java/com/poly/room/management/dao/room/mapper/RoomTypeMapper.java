package com.poly.room.management.dao.room.mapper;

import com.poly.domain.valueobject.Money;
import com.poly.room.management.dao.room.entity.FurnitureEntity;
import com.poly.room.management.dao.room.entity.RoomTypeEntity;
import com.poly.room.management.dao.room.entity.RoomTypeFurnitureEntity;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.entity.RoomTypeFurniture;
import com.poly.room.management.domain.valueobject.FurnitureId;
import com.poly.room.management.domain.valueobject.FurnitureRequirementId;
import com.poly.room.management.domain.valueobject.RoomTypeId;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RoomTypeMapper {

    public RoomType toDomainEntity(RoomTypeEntity entity) {
        RoomType roomType = RoomType.Builder.builder()
                .id(new RoomTypeId(entity.getRoomTypeId()))
                .typeName(entity.getTypeName())
                .basePrice(new Money(entity.getBasePrice()))
                .description(entity.getDescription())
                .maxOccupancy(entity.getMaxOccupancy())
                .build();

        roomType.setRoomTypeFurnitures(entity.getFurnituresRequirements().stream()
                .map(rtfEntity -> {
                    RoomTypeFurniture roomTypeFurniture = new RoomTypeFurniture();

                    Furniture furniture = new Furniture();
                    furniture.setId(new FurnitureId(rtfEntity.getFurniture().getFurnitureId()));
                    furniture.setName(rtfEntity.getFurniture().getName());
                    furniture.setPrice(new Money(rtfEntity.getFurniture().getPrice()));
                    roomTypeFurniture.setFurniture(furniture);

                    roomTypeFurniture.setRoomType(roomType);

                    roomTypeFurniture.setRequiredQuantity(rtfEntity.getRequireQuantity());
                    roomTypeFurniture.setId(new FurnitureRequirementId(rtfEntity.getRoomTypeFurnitureId()));
                    return roomTypeFurniture;
                })
                .collect(Collectors.toList()));

        return roomType;
    }

    public RoomTypeEntity toEntity(RoomType domain) {
        RoomTypeEntity entity = new RoomTypeEntity();
        entity.setRoomTypeId(domain.getId().getValue());
        entity.setTypeName(domain.getTypeName());
        entity.setDescription(domain.getDescription());
        entity.setBasePrice(domain.getBasePrice().getAmount());
        entity.setMaxOccupancy(domain.getMaxOccupancy());

        if (domain.getRoomTypeFurnitures() != null) {
            entity.setFurnituresRequirements(domain.getRoomTypeFurnitures().stream()
                    .map(rtfDomain -> {
                        RoomTypeFurnitureEntity rtfEntity = new RoomTypeFurnitureEntity();

                        FurnitureEntity furnitureEntity = new FurnitureEntity();
                        furnitureEntity.setFurnitureId(rtfDomain.getFurniture().getId().getValue());
                        furnitureEntity.setName(rtfDomain.getFurniture().getName());
                        furnitureEntity.setPrice(rtfDomain.getFurniture().getPrice().getAmount());
                        rtfEntity.setFurniture(furnitureEntity);

                        RoomTypeEntity roomTypeEntity = new RoomTypeEntity();
                        roomTypeEntity.setRoomTypeId(domain.getId().getValue());
                        rtfEntity.setRoomType(roomTypeEntity);
                        UUID rtfId = rtfDomain.getId() != null ? rtfDomain.getId().getValue() : null;
                        rtfEntity.setRoomTypeFurnitureId(rtfId);
                        rtfEntity.setRequireQuantity(rtfDomain.getRequiredQuantity());
                        return rtfEntity;
                    })
                    .collect(Collectors.toList()));
        }

        return entity;
    }
}