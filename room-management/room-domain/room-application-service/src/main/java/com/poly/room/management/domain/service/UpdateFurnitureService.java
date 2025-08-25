package com.poly.room.management.domain.service;

import com.poly.room.management.domain.dto.request.FurnitureCreationRequest;
import com.poly.room.management.domain.entity.Furniture;

import java.util.UUID;

public interface UpdateFurnitureService {

    Furniture updateFurniture(UUID furnitureId, FurnitureCreationRequest furniture);
}
