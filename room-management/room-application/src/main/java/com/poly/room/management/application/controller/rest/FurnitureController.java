package com.poly.room.management.application.controller.rest;

import com.poly.room.management.domain.dto.response.FurnitureResponse;
import com.poly.room.management.domain.service.FurnitureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/furnitures")
@RequiredArgsConstructor
@Slf4j
public class FurnitureController {

    private final FurnitureService furnitureService;

    @GetMapping
    public List<FurnitureResponse> getAllFurniture() {
        log.info("Getting all furniture");
        return furnitureService.getAllFurnitures();
    }
}
