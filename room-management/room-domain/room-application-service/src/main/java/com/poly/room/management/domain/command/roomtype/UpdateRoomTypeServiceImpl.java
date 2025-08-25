package com.poly.room.management.domain.command.roomtype;

import com.poly.domain.valueobject.Money;
import com.poly.room.management.domain.dto.request.UpdateRoomTypeRequest;
import com.poly.room.management.domain.dto.response.RoomTypeResponse;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.mapper.RoomTypeDtoMapper;
import com.poly.room.management.domain.port.out.repository.RoomTypeRepository;
import com.poly.room.management.domain.service.UpdateRoomTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateRoomTypeServiceImpl implements UpdateRoomTypeService {

    private final RoomTypeRepository repository;
    private final RoomTypeHelper roomTypeHelper;
    private final RoomTypeDtoMapper mapper;

    @Override
    @Transactional
    public RoomTypeResponse updateRoomType(UUID roomTypeId, UpdateRoomTypeRequest request) {
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
        if (request.getBasePrice() != null && !request.getBasePrice().isBlank()) {
            existingRoomType.setBasePrice(Money.from(request.getBasePrice()));
        }

        if (request.getFurnitureRequirements() != null) {
            existingRoomType.setRoomTypeFurnitures(roomTypeHelper.buildFurnitureRequirements(
                    request.getFurnitureRequirements(), existingRoomType));
        }

        return mapper.toResponse(repository.save(existingRoomType));
    }
}