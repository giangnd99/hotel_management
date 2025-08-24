package com.poly.room.management.domain.command.roomtype;

import com.poly.room.management.domain.dto.request.FurnitureRequirementRequest;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.entity.RoomTypeFurniture;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.port.out.repository.FurnitureRepository;
import com.poly.room.management.domain.valueobject.FurnitureRequirementId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoomTypeHelper {

    private final FurnitureRepository furnitureRepository;

    public List<RoomTypeFurniture> buildFurnitureRequirements(
            List<FurnitureRequirementRequest> requestFurnitureRequirements, RoomType roomType) {

        if (requestFurnitureRequirements == null || requestFurnitureRequirements.isEmpty()) {
            return List.of();
        }

        List<UUID> furnitureIds = requestFurnitureRequirements.stream()
                .map(FurnitureRequirementRequest::getFurnitureId)
                .collect(Collectors.toList());

        List<Furniture> furnitures = furnitureRepository.findAllByIdIn(furnitureIds);

        if (furnitures.size() != furnitureIds.size()) {
            throw new RoomDomainException("One or more furniture IDs are invalid.");
        }

        return requestFurnitureRequirements.stream()
                .map(req -> {
                    Furniture furniture = furnitures.stream()
                            .filter(f -> f.getId().getValue().equals(req.getFurnitureId()))
                            .findFirst()
                            .orElseThrow(() -> new RoomDomainException("Furniture with ID " + req.getFurnitureId() + " not found."));

                    if (req.getQuantity() <= 0) {
                        throw new RoomDomainException("Furniture quantity must be a positive value.");
                    }

                    RoomTypeFurniture roomTypeFurniture = new RoomTypeFurniture();
                    roomTypeFurniture.setId(new FurnitureRequirementId(UUID.randomUUID()));
                    roomTypeFurniture.setFurniture(furniture);
                    roomTypeFurniture.setRoomType(roomType);
                    roomTypeFurniture.setRequiredQuantity(req.getQuantity());

                    return roomTypeFurniture;
                })
                .collect(Collectors.toList());
    }
}