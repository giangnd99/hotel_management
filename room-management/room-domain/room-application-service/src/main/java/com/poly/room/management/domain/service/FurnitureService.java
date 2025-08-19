package com.poly.room.management.domain.service;

import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.dto.response.FurnitureResponse;

import java.util.List;

public interface FurnitureService {
    FurnitureResponse createFurniture(String inventoryItemId) throws RoomDomainException;
    FurnitureResponse updateFurnitureInventoryItem(Integer furnitureId, String inventoryItemId) throws RoomDomainException;
    void deleteFurniture(Integer furnitureId) throws RoomDomainException;
    FurnitureResponse getFurnitureById(Integer furnitureId) throws RoomDomainException;
    List<FurnitureResponse> getAllFurnitures();
    FurnitureResponse getFurnitureByInventoryItemId(String inventoryItemId) throws RoomDomainException;
}