package com.poly.room.management.domain.service;

import com.poly.room.management.domain.dto.request.FurnitureCreationRequest;
import com.poly.room.management.domain.entity.Furniture;

public interface CreationFurnitureService {

    Furniture createFurniture(FurnitureCreationRequest request);
}
