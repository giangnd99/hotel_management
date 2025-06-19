package com.poly.room.management.domain.handler;

import com.poly.application.handler.ApplicationServiceException;
import com.poly.domain.valueobject.InventoryItemId;
import com.poly.room.management.domain.dto.response.FurnitureResponse;
import com.poly.room.management.domain.dto.response.ItemDTO;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.mapper.FurnitureDtoMapper;
import com.poly.room.management.domain.port.out.feign.InventoryServiceClient;
import com.poly.room.management.domain.port.out.repository.FurnitureRepository;
import com.poly.room.management.domain.service.RoomDomainService;
import com.poly.service.handler.BaseHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class FurnitureCreationHandler extends BaseHandler<RoomDomainService, FurnitureRepository> {
    private static final String INVENTORY_ID_EMPTY_MESSAGE = "inventoryItemId cannot be null or empty";
    private static final String ITEM_NOT_FOUND_MESSAGE = "Item not found";
    private static final String FURNITURE_CREATION_ERROR = "Failed to create furniture";

    private final FurnitureDtoMapper furnitureDtoMapper;
    private final InventoryServiceClient inventoryServiceClient;

    public FurnitureCreationHandler(RoomDomainService domainService,
                                    FurnitureRepository furnitureRepository,
                                    FurnitureDtoMapper furnitureDtoMapper,
                                    InventoryServiceClient inventoryServiceClient) {
        super(domainService, furnitureRepository);
        this.furnitureDtoMapper = furnitureDtoMapper;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    public FurnitureResponse createFurniture(String inventoryItemId) {
        try {
            validateInventoryItemId(inventoryItemId);
            ItemDTO item = fetchAndValidateInventoryItem(inventoryItemId);
            Furniture furniture = createAndSaveFurniture(item);
            return furnitureDtoMapper.toResponse(furniture);
        } catch (RoomDomainException e) {
            throw new ApplicationServiceException(FURNITURE_CREATION_ERROR, e);
        }
    }

    private void validateInventoryItemId(String inventoryItemId) {
        if (inventoryItemId == null || inventoryItemId.isEmpty()) {
            throw new ApplicationServiceException(INVENTORY_ID_EMPTY_MESSAGE);
        }
    }

    private ItemDTO fetchAndValidateInventoryItem(String inventoryItemId) {
        ResponseEntity<ItemDTO> response = inventoryServiceClient.getItem(inventoryItemId);
        ItemDTO item = response.getBody();
        if (item == null) {
            throw new ApplicationServiceException(ITEM_NOT_FOUND_MESSAGE);
        }
        return item;
    }

    private Furniture createAndSaveFurniture(ItemDTO item) {
        InventoryItemId domainInventoryItemId = new InventoryItemId(item.getItemCode());
        Furniture furniture = domainService.getFurnitureCommandService()
                .createFurniture(domainInventoryItemId);
        return repository.save(furniture);
    }
}
