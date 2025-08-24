package com.poly.room.management.application.controller.rest;

import com.poly.room.management.domain.dto.request.FurnitureCreationRequest;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.service.CreationFurnitureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/furniture")
@RequiredArgsConstructor
@Slf4j
public class CreateFurnitureController {

    private final CreationFurnitureService creationFurnitureService;
    @PostMapping
    public Furniture createFurniture(FurnitureCreationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Furniture creation request cannot be null");
        }
        log.info("Creating new furniture: {}", request.getName());
        return creationFurnitureService.createFurniture(request);
    }
}
