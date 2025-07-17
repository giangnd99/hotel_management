package com.poly.room.management.domain.port.in.service;

import com.poly.restaurant.domain.handler.AppException;
import com.poly.room.management.domain.dto.request.CreateRoomTypeRequest;
import com.poly.room.management.domain.dto.request.FurnitureRequirementRequest;
import com.poly.room.management.domain.dto.request.UpdateRoomTypeRequest;
import com.poly.room.management.domain.dto.response.RoomTypeResponse;

import java.util.List;

public interface RoomTypeService {
    RoomTypeResponse createRoomType(CreateRoomTypeRequest request) throws AppException;
    RoomTypeResponse updateRoomType(UpdateRoomTypeRequest request) throws AppException;
    void deleteRoomType(Integer roomTypeId) throws AppException;
    RoomTypeResponse getRoomTypeById(Integer roomTypeId) throws AppException;
    List<RoomTypeResponse> getAllRoomTypes();
    RoomTypeResponse addFurnitureToRoomType(Integer roomTypeId, FurnitureRequirementRequest request) throws AppException;
    RoomTypeResponse removeFurnitureFromRoomType(Integer roomTypeId, Integer furnitureId) throws AppException;
    RoomTypeResponse updateFurnitureQuantityInRoomType(Integer roomTypeId, FurnitureRequirementRequest request) throws AppException;
}