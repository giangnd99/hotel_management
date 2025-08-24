package com.poly.room.management.domain.command.roomtype;

import com.poly.domain.valueobject.Money;
import com.poly.room.management.domain.dto.request.CreateRoomTypeRequest;
import com.poly.room.management.domain.dto.request.FurnitureRequirementRequest;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.entity.RoomTypeFurniture;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.port.out.repository.FurnitureRepository;
import com.poly.room.management.domain.port.out.repository.RoomTypeRepository;
import com.poly.room.management.domain.service.CreationRoomTypeService;
import com.poly.room.management.domain.valueobject.RoomTypeId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreationRoomTypeServiceImpl implements CreationRoomTypeService {

    private final RoomTypeRepository repository;
    private final FurnitureRepository furnitureRepository;


    @Override
    @Transactional
    public RoomType createRoomType(CreateRoomTypeRequest request) {
        log.info("Executing room type creation for request: {}", request);
        if (request.getTypeName() == null) {
            throw new RoomDomainException("Room type name cannot be empty");
        }
        if (request.getDescription() == null) {
            throw new RoomDomainException("Room type description cannot be empty");
        }
        String typeName = request.getTypeName();
        String description = request.getDescription();
        Integer maxOccupancy = request.getMaxOccupancy();
        Money basePrice = Money.from(request.getBasePrice());
        RoomTypeId roomTypeId = new RoomTypeId(UUID.randomUUID());
        RoomType newRoomType = RoomType.Builder.builder()
                .id(roomTypeId)
                .typeName(typeName)
                .description(description)
                .maxOccupancy(maxOccupancy)
                .basePrice(basePrice)
                .build();
        List<Furniture> furnitures;
        List<RoomTypeFurniture> furnitureRequirements = new ArrayList<>();
        if (request.getFurnitureRequirements() != null) {
            List<RoomTypeFurniture> newFurnitureRequirements = new ArrayList<>();
            if (!request.getFurnitureRequirements().isEmpty()) {
                furnitures = request.getFurnitureRequirements().stream()
                        .map(furnitureRequirementRequest ->
                                furnitureRepository.findById(furnitureRequirementRequest.getFurnitureId()))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();
                if (furnitures.size() != request.getFurnitureRequirements().size()) {
                    for (FurnitureRequirementRequest furnitureRequirementRequest : request.getFurnitureRequirements()) {
                        Furniture furniture = furnitures.stream()
                                .filter(f -> f.getId().getValue().equals(furnitureRequirementRequest.getFurnitureId()))
                                .findFirst()
                                .orElseThrow(() -> new RoomDomainException("Furniture with ID " + furnitureRequirementRequest.getFurnitureId() + " not found."));
                        RoomTypeFurniture roomTypeFurniture = getRoomTypeFurniture(furnitureRequirementRequest, furniture, newRoomType);
                        newFurnitureRequirements.add(roomTypeFurniture);
                    }
                }
                newRoomType.setRoomTypeFurnitures(newFurnitureRequirements);
                return repository.save(newRoomType);
            }
        }

        return repository.save(newRoomType);
    }

    @NotNull
    private static RoomTypeFurniture getRoomTypeFurniture(FurnitureRequirementRequest furnitureRequirementRequest, Furniture furniture, RoomType existingRoomType) {
        RoomTypeFurniture roomTypeFurniture = new RoomTypeFurniture();

        roomTypeFurniture.setFurniture(furniture);
        roomTypeFurniture.setRoomType(existingRoomType);

        if (furnitureRequirementRequest.getQuantity() < 0) {
            roomTypeFurniture.setRequiredQuantity(1);
        }
        if (furnitureRequirementRequest.getQuantity() == 0) {
            roomTypeFurniture.setRequiredQuantity(1);
        }
        roomTypeFurniture.setRequiredQuantity(furnitureRequirementRequest.getQuantity());
        return roomTypeFurniture;
    }
}
