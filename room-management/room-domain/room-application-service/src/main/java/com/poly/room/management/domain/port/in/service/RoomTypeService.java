package com.poly.room.management.domain.port.in.service;

import com.poly.application.handler.ApplicationServiceException;
import com.poly.room.management.domain.dto.request.CreateRoomTypeRequest;
import com.poly.room.management.domain.dto.request.FurnitureRequirementRequest;
import com.poly.room.management.domain.dto.request.UpdateRoomTypeRequest;
import com.poly.room.management.domain.dto.response.RoomTypeResponse;

import java.util.List;

public interface RoomTypeService {
    RoomTypeResponse createRoomType(CreateRoomTypeRequest request) throws ApplicationServiceException;
    RoomTypeResponse updateRoomType(UpdateRoomTypeRequest request) throws ApplicationServiceException;
    void deleteRoomType(Integer roomTypeId) throws ApplicationServiceException;
    RoomTypeResponse getRoomTypeById(Integer roomTypeId) throws ApplicationServiceException;
    List<RoomTypeResponse> getAllRoomTypes();
    RoomTypeResponse addFurnitureToRoomType(Integer roomTypeId, FurnitureRequirementRequest request) throws ApplicationServiceException;
    RoomTypeResponse removeFurnitureFromRoomType(Integer roomTypeId, Integer furnitureId) throws ApplicationServiceException;
    RoomTypeResponse updateFurnitureQuantityInRoomType(Integer roomTypeId, FurnitureRequirementRequest request) throws ApplicationServiceException;
}