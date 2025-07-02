package com.poly.room.management.domain.port.in.service;

import com.poly.application.handler.ApplicationServiceException;
import com.poly.room.management.domain.dto.response.FurnitureResponse;

import java.util.List;

public interface FurnitureService {
    FurnitureResponse createFurniture(String inventoryItemId) throws ApplicationServiceException;
    FurnitureResponse updateFurnitureInventoryItem(Integer furnitureId, String inventoryItemId) throws ApplicationServiceException;
    void deleteFurniture(Integer furnitureId) throws ApplicationServiceException;
    FurnitureResponse getFurnitureById(Integer furnitureId) throws ApplicationServiceException;
    List<FurnitureResponse> getAllFurnitures();
    FurnitureResponse getFurnitureByInventoryItemId(String inventoryItemId) throws ApplicationServiceException;
}