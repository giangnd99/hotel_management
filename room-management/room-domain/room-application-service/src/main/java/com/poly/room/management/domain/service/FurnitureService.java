package com.poly.room.management.domain.service;

import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.dto.response.FurnitureResponse;

import java.util.List;
import java.util.UUID;

public interface FurnitureService {
    void deleteFurniture(UUID furnitureId) throws RoomDomainException;
    FurnitureResponse getFurnitureById(UUID furnitureId) throws RoomDomainException;
    List<FurnitureResponse> getAllFurnitures();
}