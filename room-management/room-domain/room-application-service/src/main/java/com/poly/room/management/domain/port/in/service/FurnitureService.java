package com.poly.room.management.domain.port.in.service;

import com.poly.application.handler.ApplicationServiceException;
import com.poly.room.management.domain.dto.response.FurnitureResponse;

import java.util.List;

public interface FurnitureService {
    FurnitureResponse createFurniture(Integer inventoryItemId) throws ApplicationServiceException;
    FurnitureResponse updateFurnitureInventoryItem(Integer furnitureId, Integer newInventoryItemId) throws ApplicationServiceException;
    void deleteFurniture(Integer furnitureId) throws ApplicationServiceException;
    FurnitureResponse getFurnitureById(Integer furnitureId) throws ApplicationServiceException;
    List<FurnitureResponse> getAllFurnitures();
    FurnitureResponse getFurnitureByInventoryItemId(Integer inventoryItemId) throws ApplicationServiceException;
}