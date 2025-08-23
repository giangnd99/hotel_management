package com.poly.room.management.domain.service;

import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.dto.request.CreateRoomTypeRequest;
import com.poly.room.management.domain.dto.request.FurnitureRequirementRequest;
import com.poly.room.management.domain.dto.request.UpdateRoomTypeRequest;
import com.poly.room.management.domain.dto.response.RoomTypeResponse;

import java.util.List;
import java.util.UUID;

public interface RoomTypeService {
    RoomTypeResponse createRoomType(CreateRoomTypeRequest request) throws RoomDomainException;
    RoomTypeResponse updateRoomType(UpdateRoomTypeRequest request) throws RoomDomainException;
    void deleteRoomType(UUID roomTypeId) throws RoomDomainException;
    RoomTypeResponse getRoomTypeById(UUID roomTypeId) throws RoomDomainException;
    List<RoomTypeResponse> getAllRoomTypes();
    RoomTypeResponse addFurnitureToRoomType(UUID roomTypeId, FurnitureRequirementRequest request) throws RoomDomainException;
    RoomTypeResponse removeFurnitureFromRoomType(UUID roomTypeId, Integer furnitureId) throws RoomDomainException;
    RoomTypeResponse updateFurnitureQuantityInRoomType(UUID roomTypeId, FurnitureRequirementRequest request) throws RoomDomainException;
}