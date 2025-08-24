package com.poly.room.management.domain.command.furniture;

import com.poly.domain.valueobject.Money;
import com.poly.room.management.domain.dto.request.FurnitureCreationRequest;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.port.out.repository.FurnitureRepository;
import com.poly.room.management.domain.service.CreationFurnitureService;
import com.poly.room.management.domain.valueobject.FurnitureId;
import jakarta.persistence.ManyToOne;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreationFurnitureServiceImpl implements CreationFurnitureService {

    private final FurnitureRepository repository;


    @Override
    public Furniture createFurniture(FurnitureCreationRequest request) {

        if (request.getName() == null || request.getName().isBlank()) {
            throw new RoomDomainException("Furniture name cannot be empty");
        }
        String name = request.getName();
        if (request.getPrice() == null) {
            throw new RoomDomainException("Furniture price cannot be empty");
        }
        Money price = Money.from(request.getPrice());

        FurnitureId id = new FurnitureId(UUID.randomUUID());

        Furniture newFurniture = Furniture.Builder.builder()
                .id(id)
                .name(name)
                .price(price)
                .build();

        return repository.save(newFurniture);
    }
}
