package com.poly.room.management.domain.service;

import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.dto.request.CreateRoomTypeRequest;
import com.poly.room.management.domain.dto.request.FurnitureRequirementRequest;
import com.poly.room.management.domain.dto.request.UpdateRoomTypeRequest;
import com.poly.room.management.domain.dto.response.RoomTypeResponse;

import java.util.List;

public interface RoomTypeService {
    RoomTypeResponse createRoomType(CreateRoomTypeRequest request) throws RoomDomainException;
    RoomTypeResponse updateRoomType(UpdateRoomTypeRequest request) throws RoomDomainException;
    void deleteRoomType(Integer roomTypeId) throws RoomDomainException;
    RoomTypeResponse getRoomTypeById(Integer roomTypeId) throws RoomDomainException;
    List<RoomTypeResponse> getAllRoomTypes();
    RoomTypeResponse addFurnitureToRoomType(Integer roomTypeId, FurnitureRequirementRequest request) throws RoomDomainException;
    RoomTypeResponse removeFurnitureFromRoomType(Integer roomTypeId, Integer furnitureId) throws RoomDomainException;
    RoomTypeResponse updateFurnitureQuantityInRoomType(Integer roomTypeId, FurnitureRequirementRequest request) throws RoomDomainException;
}