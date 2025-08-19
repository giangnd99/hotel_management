package com.poly.room.management.domain.service.impl;

import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.dto.response.FurnitureResponse;
import com.poly.room.management.domain.service.FurnitureService;
import com.poly.room.management.domain.port.out.repository.FurnitureRepository;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.mapper.FurnitureDtoMapper;
import com.poly.room.management.domain.valueobject.FurnitureId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FurnitureServiceImpl implements FurnitureService {

    private final FurnitureRepository furnitureRepository;
    private final FurnitureDtoMapper furnitureDtoMapper;

    @Override
    public FurnitureResponse createFurniture(String inventoryItemId) throws RoomDomainException {
        log.info("Creating furniture with inventory item ID: {}", inventoryItemId);
        
        try {
            // Tạo furniture mới sử dụng Builder pattern
            Furniture furniture = Furniture.Builder.builder()
                    .name("Furniture from " + inventoryItemId)
                    .build();
            
            Furniture savedFurniture = furnitureRepository.save(furniture);
            
            log.info("Furniture created successfully with ID: {}", savedFurniture.getId().getValue());
            return furnitureDtoMapper.toResponse(savedFurniture);
            
        } catch (Exception e) {
            log.error("Error creating furniture: {}", e.getMessage());
            throw new RoomDomainException("Failed to create furniture: " + e.getMessage());
        }
    }

    @Override
    public FurnitureResponse updateFurnitureInventoryItem(Integer furnitureId, String inventoryItemId) throws RoomDomainException {
        log.info("Updating furniture {} with new inventory item ID: {}", furnitureId, inventoryItemId);
        
        try {
            Furniture furniture = furnitureRepository.findById(furnitureId)
                    .orElseThrow(() -> new RoomDomainException("Furniture not found with ID: " + furnitureId));
            
            // Cập nhật tên furniture
            furniture.setName("Furniture from " + inventoryItemId);
            Furniture updatedFurniture = furnitureRepository.update(furniture);
            
            log.info("Furniture updated successfully: {}", furnitureId);
            return furnitureDtoMapper.toResponse(updatedFurniture);
            
        } catch (Exception e) {
            log.error("Error updating furniture: {}", e.getMessage());
            throw new RoomDomainException("Failed to update furniture: " + e.getMessage());
        }
    }

    @Override
    public void deleteFurniture(Integer furnitureId) throws RoomDomainException {
        log.info("Deleting furniture with ID: {}", furnitureId);
        
        try {
            furnitureRepository.deleteById(furnitureId);
            
            log.info("Furniture deleted successfully: {}", furnitureId);
            
        } catch (Exception e) {
            log.error("Error deleting furniture: {}", e.getMessage());
            throw new RoomDomainException("Failed to delete furniture: " + e.getMessage());
        }
    }

    @Override
    public FurnitureResponse getFurnitureById(Integer furnitureId) throws RoomDomainException {
        log.info("Getting furniture by ID: {}", furnitureId);
        
        try {
            Furniture furniture = furnitureRepository.findById(furnitureId)
                    .orElseThrow(() -> new RoomDomainException("Furniture not found with ID: " + furnitureId));
            
            return furnitureDtoMapper.toResponse(furniture);
            
        } catch (Exception e) {
            log.error("Error getting furniture: {}", e.getMessage());
            throw new RoomDomainException("Failed to get furniture: " + e.getMessage());
        }
    }

    @Override
    public List<FurnitureResponse> getAllFurnitures() {
        log.info("Getting all furnitures");
        
        try {
            List<Furniture> furnitures = furnitureRepository.findAll();
            
            return furnitures.stream()
                    .map(furnitureDtoMapper::toResponse)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            log.error("Error getting all furnitures: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public FurnitureResponse getFurnitureByInventoryItemId(String inventoryItemId) throws RoomDomainException {
        log.info("Getting furniture by inventory item ID: {}", inventoryItemId);
        
        // Tìm furniture theo inventory item ID
        // Cần implement method này trong repository
        throw new UnsupportedOperationException("Method getFurnitureByInventoryItemId not yet implemented");
    }
}
