package com.poly.room.management.domain.command.furniture;

import com.poly.domain.valueobject.Money;
import com.poly.room.management.domain.dto.request.FurnitureCreationRequest;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.port.out.repository.FurnitureRepository;
import com.poly.room.management.domain.service.UpdateFurnitureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateFurnitureServiceImpl implements UpdateFurnitureService {


    private final FurnitureRepository repository;

    @Override
    public Furniture updateFurniture(UUID furnitureId, FurnitureCreationRequest furniture) {
        Furniture existingFurniture = repository.findById(furnitureId).orElseThrow(() -> new RuntimeException("Furniture not found"));

        if (furniture.getName() != null && !furniture.getName().isBlank()) {
            existingFurniture.setName(furniture.getName());
        }
        if (furniture.getPrice() != null) {
            existingFurniture.setPrice(Money.from(furniture.getPrice()));
        }
        return repository.save(existingFurniture);
    }
}
