package com.poly.room.management.domain;

import com.poly.application.handler.ApplicationServiceException;
import com.poly.domain.valueobject.InventoryItemId;
import com.poly.room.management.domain.dto.response.FurnitureResponse;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.mapper.FurnitureDtoMapper;
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
    private final FurnitureDtoMapper furnitureDtoMapper;

    @Override
    @Transactional
    public FurnitureResponse createFurniture(String inventoryItemId) throws ApplicationServiceException {
        try {
            if (inventoryItemId == null) {
                throw new ApplicationServiceException("inventoryItemId cannot be null");
            }
            InventoryItemId domainInventoryItemId = new InventoryItemId(inventoryItemId);
            Furniture furniture = roomDomainService.getFurnitureCommandService()
                    .createFurniture(domainInventoryItemId);

            return furnitureDtoMapper.toResponse(furniture);
        } catch (RoomDomainException e) {
            throw new ApplicationServiceException("Failed to create furniture", e);
        }
    }

    @Override
    public FurnitureResponse updateFurnitureInventoryItem(Integer furnitureId, String inventoryItemId) throws ApplicationServiceException {
        return null;
    }

    @Override
    public void deleteFurniture(Integer furnitureId) throws ApplicationServiceException {

    }

    @Override
    public FurnitureResponse getFurnitureById(Integer furnitureId) throws ApplicationServiceException {
        return null;
    }

    @Override
    public List<FurnitureResponse> getAllFurnitures() {
        return List.of();
    }

    @Override
    public FurnitureResponse getFurnitureByInventoryItemId(String inventoryItemId) throws ApplicationServiceException {
        return null;
    }

//    @Override
//    @Transactional
//    public FurnitureResponse updateFurnitureInventoryItem(Integer furnitureId, String inventoryItemId)
//            throws ApplicationServiceException {
//        try {
//            if (furnitureId == null) {
//                throw new ApplicationServiceException("furnitureId cannot be null");
//            }
//            if (inventoryItemId == null) {
//                throw new ApplicationServiceException("inventoryItemId cannot be null");
//            }
//
//            // First get the furniture entity
//            Furniture furniture = roomDomainService.getFurnitureQueryService().getFurnitureById(furnitureId);
//            InventoryItemId domainInventoryItemId = new InventoryItemId(inventoryItemId);
//
//            // Then update it
//            Furniture updatedFurniture = roomDomainService.getFurnitureCommandService()
//                    .updateFurnitureInventoryItem(furniture, domainInventoryItemId);
//
//            return furnitureDtoMapper.toResponse(updatedFurniture);
//        } catch (RoomDomainException e) {
//            throw new ApplicationServiceException("Failed to update furniture inventory item", e);
//        }
//    }
//
//    @Override
//    public void deleteFurniture(Integer furnitureId) throws ApplicationServiceException {
//        if (furnitureId == null) {
//            throw new ApplicationServiceException("furnitureId cannot be null");
//        }
//        roomDomainService.getFurnitureCommandService().deleteFurniture(furnitureId);
//    }
//
//    @Override
//    public FurnitureResponse getFurnitureById(Integer furnitureId) throws ApplicationServiceException {
//        if (furnitureId == null) {
//            throw new ApplicationServiceException("furnitureId cannot be null");
//        }
//        return roomDomainService.getFurnitureQueryService().getFurnitureById(furnitureId);
//    }
//
//    @Override
//    public List<FurnitureResponse> getAllFurnitures() {
//        return roomDomainService.getFurnitureQueryService().getAllFurnitures();
//    }
//
//    @Override
//    public FurnitureResponse getFurnitureByInventoryItemId(String inventoryItemId) throws ApplicationServiceException {
//        if (inventoryItemId == null) {
//            throw new ApplicationServiceException("inventoryItemId cannot be null");
//        }
//        return roomDomainService.getFurnitureQueryService().getFurnitureByInventoryItemId(inventoryItemId);
//    }
}