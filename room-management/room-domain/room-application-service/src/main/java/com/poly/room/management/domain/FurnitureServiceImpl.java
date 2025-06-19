package com.poly.room.management.application.service;

import com.poly.application.handler.ApplicationServiceException;
import com.poly.room.management.domain.dto.response.FurnitureResponse;
import com.poly.room.management.domain.port.in.service.FurnitureService;
import com.poly.room.management.domain.service.RoomDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FurnitureServiceImpl implements FurnitureService {
    private final RoomDomainService roomDomainService;

    @Override
    @Transactional
    public FurnitureResponse createFurniture(Integer inventoryItemId) throws ApplicationServiceException {
        return roomDomainService.createFurniture(inventoryItemId);
    }

    @Override
    @Transactional
    public FurnitureResponse updateFurnitureInventoryItem(Integer furnitureId, Integer newInventoryItemId) throws ApplicationServiceException {
        return roomDomainService.updateFurnitureInventoryItem(furnitureId, newInventoryItemId);
    }

    @Override
    @Transactional
    public void deleteFurniture(Integer furnitureId) throws ApplicationServiceException {
        roomDomainService.deleteFurniture(furnitureId);
    }

    @Override
    public FurnitureResponse getFurnitureById(Integer furnitureId) throws ApplicationServiceException {
        return roomDomainService.getFurnitureById(furnitureId);
    }

    @Override
    public List<FurnitureResponse> getAllFurnitures() {
        return roomDomainService.getAllFurnitures();
    }

    @Override
    public FurnitureResponse getFurnitureByInventoryItemId(Integer inventoryItemId) throws ApplicationServiceException {
        return roomDomainService.getFurnitureByInventoryItemId(inventoryItemId);
    }
}