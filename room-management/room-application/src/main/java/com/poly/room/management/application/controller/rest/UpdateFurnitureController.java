package com.poly.room.management.application.controller.rest;

import com.poly.room.management.domain.dto.request.FurnitureCreationRequest;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.service.UpdateFurnitureService;
import com.poly.room.management.domain.valueobject.FurnitureId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/furnitures")
public class UpdateFurnitureController {

    private final UpdateFurnitureService updateFurnitureService;

    @PostMapping("/{furnitureId}")
    public Furniture updateFurniture(
            @PathVariable UUID furnitureId,
            @RequestBody FurnitureCreationRequest furniture) {
        if (furniture == null) {
            throw new IllegalArgumentException("Furniture cannot be null");
        }
        if (furnitureId == null) {
            throw new IllegalArgumentException("Furniture id cannot be null");
        }

        return updateFurnitureService.updateFurniture(furnitureId,furniture);
    }
}
