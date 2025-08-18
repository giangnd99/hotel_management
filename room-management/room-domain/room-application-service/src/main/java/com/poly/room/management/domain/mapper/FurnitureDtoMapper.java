package com.poly.room.management.domain.mapper;

import com.poly.domain.valueobject.InventoryItemId;
import com.poly.domain.valueobject.Money;
import com.poly.room.management.domain.dto.request.FurnitureCreationRequest;
import com.poly.room.management.domain.dto.response.FurnitureResponse;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.valueobject.FurnitureId;
import org.springframework.stereotype.Component;

@Component
public class FurnitureDtoMapper {

    public Furniture toEntity(FurnitureCreationRequest request) {
        return Furniture.Builder.builder()
                .name(request.getName())
                .price(Money.from(request.getPrice()))
                .build();
    }

    public FurnitureResponse toResponse(Furniture furniture) {
        return FurnitureResponse.builder()
                .id(furniture.getId().getValue())
                .name(furniture.getName())
                .price(furniture.getPrice().getAmount().toString())
                .build();
    }

}