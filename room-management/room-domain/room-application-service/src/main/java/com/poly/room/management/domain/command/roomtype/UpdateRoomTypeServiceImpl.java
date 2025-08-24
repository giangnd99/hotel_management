package com.poly.room.management.domain.command.roomtype;

import com.poly.domain.valueobject.Money;
import com.poly.room.management.domain.dto.request.FurnitureRequirementRequest;
import com.poly.room.management.domain.dto.request.UpdateRoomTypeRequest;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.entity.RoomTypeFurniture;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.port.out.repository.FurnitureRepository;
import com.poly.room.management.domain.port.out.repository.RoomTypeRepository;
import com.poly.room.management.domain.service.UpdateRoomTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateRoomTypeServiceImpl implements UpdateRoomTypeService {

    private final RoomTypeRepository repository;
    private final FurnitureRepository furnitureRepository;


    @Override
    public RoomType updateRoomType(UUID roomTypeId, UpdateRoomTypeRequest request) {
        log.info("Executing room type update for request: {}", request);

        Optional<RoomType> existingRoomTypeOptional = repository.findById(roomTypeId);
        if (existingRoomTypeOptional.isEmpty()) {
            throw new RoomDomainException("Room type with ID " + roomTypeId + " not found.");
        }
        RoomType existingRoomType = existingRoomTypeOptional.get();

        if (request.getTypeName() != null && !request.getTypeName().isBlank()) {
            existingRoomType.setTypeName(request.getTypeName());
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            existingRoomType.setDescription(request.getDescription());
        }
        if (request.getMaxOccupancy() > 0) {
            existingRoomType.setMaxOccupancy(request.getMaxOccupancy());
        }
        if (request.getBasePrice().isEmpty() || request.getBasePrice().equals("0.00") || request.getBasePrice().equals("0")) {
            existingRoomType.setBasePrice(Money.from(request.getBasePrice()));
        }

        if (request.getFurnitureRequirements() != null) {
            List<RoomTypeFurniture> newFurnitureRequirements = new ArrayList<>();
            if (!request.getFurnitureRequirements().isEmpty()) {
                List<Furniture> furnitures = request.getFurnitureRequirements().stream()
                        .map(furnitureRequirementRequest ->
                                furnitureRepository.findById(furnitureRequirementRequest.getFurnitureId()))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();

                if (furnitures.size() != request.getFurnitureRequirements().size()) {
                    throw new RoomDomainException("One or more furniture IDs are invalid.");
                }
                for (FurnitureRequirementRequest furnitureRequirementRequest : request.getFurnitureRequirements()) {
                    Furniture furniture = furnitures.stream()
                            .filter(f -> f.getId().getValue().equals(furnitureRequirementRequest.getFurnitureId()))
                            .findFirst()
                            .orElseThrow(() -> new RoomDomainException("Furniture with ID " + furnitureRequirementRequest.getFurnitureId() + " not found."));
                    RoomTypeFurniture roomTypeFurniture = getRoomTypeFurniture(furnitureRequirementRequest, furniture, existingRoomType);
                    newFurnitureRequirements.add(roomTypeFurniture);
                }

                existingRoomType.setRoomTypeFurnitures(newFurnitureRequirements);
                return repository.save(existingRoomType);
            }
        }
        return repository.save(existingRoomType);
    }

    @NotNull
    private RoomTypeFurniture getRoomTypeFurniture(FurnitureRequirementRequest furnitureRequirementRequest, Furniture furniture, RoomType existingRoomType) {
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