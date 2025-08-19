package com.poly.room.management.domain.service.impl;

import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.dto.request.CreateRoomTypeRequest;
import com.poly.room.management.domain.dto.request.FurnitureRequirementRequest;
import com.poly.room.management.domain.dto.request.UpdateRoomTypeRequest;
import com.poly.room.management.domain.dto.response.RoomTypeResponse;
import com.poly.room.management.domain.service.RoomTypeService;
import com.poly.room.management.domain.port.out.repository.RoomTypeRepository;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.mapper.RoomTypeDtoMapper;
import com.poly.domain.valueobject.Money;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomTypeDtoMapper roomTypeDtoMapper;

    @Override
    public RoomTypeResponse createRoomType(CreateRoomTypeRequest request) throws RoomDomainException {
        log.info("Creating room type: {}", request.getTypeName());
        
        try {
            // Tạo room type mới sử dụng Builder pattern
            RoomType roomType = RoomType.Builder.builder()
                    .typeName(request.getTypeName())
                    .description(request.getDescription())
                    .basePrice(new Money(new BigDecimal(request.getBasePrice())))
                    .maxOccupancy(request.getMaxOccupancy())
                    .build();
            
            // Validate room type
            roomType.validateRoomType();
            
            RoomType savedRoomType = roomTypeRepository.save(roomType);
            
            log.info("Room type created successfully with ID: {}", savedRoomType.getId().getValue());
            return roomTypeDtoMapper.toResponse(savedRoomType);
            
        } catch (Exception e) {
            log.error("Error creating room type: {}", e.getMessage());
            throw new RoomDomainException("Failed to create room type: " + e.getMessage());
        }
    }

    @Override
    public RoomTypeResponse updateRoomType(UpdateRoomTypeRequest request) throws RoomDomainException {
        log.info("Updating room type: {}", request.getRoomTypeId());
        
        try {
            RoomType existingRoomType = roomTypeRepository.findById(request.getRoomTypeId())
                    .orElseThrow(() -> new RoomDomainException("Room type not found with ID: " + request.getRoomTypeId()));
            
            // Cập nhật thông tin room type
            existingRoomType.setTypeName(request.getTypeName());
            existingRoomType.setDescription(request.getDescription());
            existingRoomType.setBasePrice(new Money(new BigDecimal(request.getBasePrice())));
            existingRoomType.setMaxOccupancy(request.getMaxOccupancy());
            
            // Validate room type sau khi cập nhật
            existingRoomType.validateRoomType();
            
            RoomType updatedRoomType = roomTypeRepository.update(existingRoomType, request.getRoomTypeId());
            
            log.info("Room type updated successfully: {}", request.getRoomTypeId());
            return roomTypeDtoMapper.toResponse(updatedRoomType);
            
        } catch (Exception e) {
            log.error("Error updating room type: {}", e.getMessage());
            throw new RoomDomainException("Failed to update room type: " + e.getMessage());
        }
    }

    @Override
    public void deleteRoomType(Integer roomTypeId) throws RoomDomainException {
        log.info("Deleting room type: {}", roomTypeId);
        
        try {
            RoomType roomType = roomTypeRepository.findById(roomTypeId)
                    .orElseThrow(() -> new RoomDomainException("Room type not found with ID: " + roomTypeId));
            
            roomTypeRepository.deleteById(roomType.getId().getValue());
            
            log.info("Room type deleted successfully: {}", roomTypeId);
            
        } catch (Exception e) {
            log.error("Error deleting room type: {}", e.getMessage());
            throw new RoomDomainException("Failed to delete room type: " + e.getMessage());
        }
    }

    @Override
    public RoomTypeResponse getRoomTypeById(Integer roomTypeId) throws RoomDomainException {
        log.info("Getting room type by ID: {}", roomTypeId);
        
        try {
            RoomType roomType = roomTypeRepository.findById(roomTypeId)
                    .orElseThrow(() -> new RoomDomainException("Room type not found with ID: " + roomTypeId));
            
            return roomTypeDtoMapper.toResponse(roomType);
            
        } catch (Exception e) {
            log.error("Error getting room type: {}", e.getMessage());
            throw new RoomDomainException("Failed to get room type: " + e.getMessage());
        }
    }

    @Override
    public List<RoomTypeResponse> getAllRoomTypes() {
        log.info("Getting all room types");
        
        try {
            List<RoomType> roomTypes = roomTypeRepository.findAll();
            
            return roomTypes.stream()
                    .map(roomTypeDtoMapper::toResponse)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            log.error("Error getting all room types: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public RoomTypeResponse addFurnitureToRoomType(Integer roomTypeId, FurnitureRequirementRequest request) throws RoomDomainException {
        log.info("Adding furniture to room type: {}", roomTypeId);
        
        try {
            RoomType roomType = roomTypeRepository.findById(roomTypeId)
                    .orElseThrow(() -> new RoomDomainException("Room type not found with ID: " + roomTypeId));
            
            // Tạo furniture requirement mới
            // Cần implement logic tạo RoomTypeFurniture entity
            throw new UnsupportedOperationException("Method addFurnitureToRoomType not yet implemented");
            
        } catch (Exception e) {
            log.error("Error adding furniture to room type: {}", e.getMessage());
            throw new RoomDomainException("Failed to add furniture to room type: " + e.getMessage());
        }
    }

    @Override
    public RoomTypeResponse removeFurnitureFromRoomType(Integer roomTypeId, Integer furnitureId) throws RoomDomainException {
        log.info("Removing furniture {} from room type: {}", furnitureId, roomTypeId);
        
        try {
            RoomType roomType = roomTypeRepository.findById(roomTypeId)
                    .orElseThrow(() -> new RoomDomainException("Room type not found with ID: " + roomTypeId));
            
            // Xóa furniture requirement
            // Cần implement logic xóa RoomTypeFurniture entity
            throw new UnsupportedOperationException("Method removeFurnitureFromRoomType not yet implemented");
            
        } catch (Exception e) {
            log.error("Error removing furniture from room type: {}", e.getMessage());
            throw new RoomDomainException("Failed to remove furniture from room type: " + e.getMessage());
        }
    }

    @Override
    public RoomTypeResponse updateFurnitureQuantityInRoomType(Integer roomTypeId, FurnitureRequirementRequest request) throws RoomDomainException {
        log.info("Updating furniture quantity in room type: {}", roomTypeId);
        
        try {
            RoomType roomType = roomTypeRepository.findById(roomTypeId)
                    .orElseThrow(() -> new RoomDomainException("Room type not found with ID: " + roomTypeId));
            
            // Cập nhật số lượng furniture requirement
            // Cần implement logic cập nhật RoomTypeFurniture entity
            throw new UnsupportedOperationException("Method updateFurnitureQuantityInRoomType not yet implemented");
            
        } catch (Exception e) {
            log.error("Error updating furniture quantity in room type: {}", e.getMessage());
            throw new RoomDomainException("Failed to update furniture quantity in room type: " + e.getMessage());
        }
    }
}
