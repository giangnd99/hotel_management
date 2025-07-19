package com.poly.room.management.domain.port.in.service;

import com.poly.restaurant.domain.handler.AppException;
import com.poly.room.management.domain.dto.response.FurnitureResponse;

import java.util.List;

public interface FurnitureService {
    FurnitureResponse createFurniture(String inventoryItemId) throws AppException;
    FurnitureResponse updateFurnitureInventoryItem(Integer furnitureId, String inventoryItemId) throws AppException;
    void deleteFurniture(Integer furnitureId) throws AppException;
    FurnitureResponse getFurnitureById(Integer furnitureId) throws AppException;
    List<FurnitureResponse> getAllFurnitures();
    FurnitureResponse getFurnitureByInventoryItemId(String inventoryItemId) throws AppException;
}