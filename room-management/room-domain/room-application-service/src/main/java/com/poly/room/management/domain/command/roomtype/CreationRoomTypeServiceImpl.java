package com.poly.room.management.domain.command.roomtype;

import com.poly.domain.valueobject.Money;
import com.poly.room.management.domain.dto.request.CreateRoomTypeRequest;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.port.out.repository.RoomTypeRepository;
import com.poly.room.management.domain.service.CreationRoomTypeService;
import com.poly.room.management.domain.valueobject.RoomTypeId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreationRoomTypeServiceImpl implements CreationRoomTypeService {

    private final RoomTypeRepository repository;

    @Override
    @Transactional
    public RoomType createRoomType(CreateRoomTypeRequest request) {
        log.info("Executing room type creation for request: {}", request);
        if (request.getTypeName() != null && !request.getTypeName().isBlank()) {
            throw new RoomDomainException("Room type name cannot be empty");
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
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
        return repository.save(newRoomType);
    }
}
