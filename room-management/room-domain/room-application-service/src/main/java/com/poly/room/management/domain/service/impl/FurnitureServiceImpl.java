package com.poly.room.management.domain.service.impl;

import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.dto.response.FurnitureResponse;
import com.poly.room.management.domain.service.FurnitureService;
import com.poly.room.management.domain.port.out.repository.FurnitureRepository;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.mapper.FurnitureDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FurnitureServiceImpl implements FurnitureService {

    private final FurnitureRepository furnitureRepository;
    private final FurnitureDtoMapper furnitureDtoMapper;


    @Override
    public void deleteFurniture(UUID furnitureId) throws RoomDomainException {
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
    public FurnitureResponse getFurnitureById(UUID furnitureId) throws RoomDomainException {
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
}
