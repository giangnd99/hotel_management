package com.poly.room.management.application.service;

import com.poly.application.handler.ApplicationServiceException;
import com.poly.room.management.domain.dto.request.CreateRoomTypeRequest;
import com.poly.room.management.domain.dto.request.FurnitureRequirementRequest;
import com.poly.room.management.domain.dto.request.UpdateRoomTypeRequest;
import com.poly.room.management.domain.dto.response.RoomTypeResponse;
import com.poly.room.management.domain.port.in.service.RoomTypeService;
import com.poly.room.management.domain.service.RoomDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {
    private final RoomDomainService roomDomainService;

    @Override
    @Transactional
    public RoomTypeResponse createRoomType(CreateRoomTypeRequest request) throws ApplicationServiceException {
        return roomDomainService.createRoomType(request);
    }

    @Override
    @Transactional
    public RoomTypeResponse updateRoomType(UpdateRoomTypeRequest request) throws ApplicationServiceException {
        return roomDomainService.updateRoomType(request);
    }

    @Override
    @Transactional
    public void deleteRoomType(Integer roomTypeId) throws ApplicationServiceException {
        roomDomainService.deleteRoomType(roomTypeId);
    }

    @Override
    public RoomTypeResponse getRoomTypeById(Integer roomTypeId) throws ApplicationServiceException {
        return roomDomainService.getRoomTypeById(roomTypeId);
    }

    @Override
    public List<RoomTypeResponse> getAllRoomTypes() {
        return roomDomainService.getAllRoomTypes();
    }

    @Override
    @Transactional
    public RoomTypeResponse addFurnitureToRoomType(Integer roomTypeId, FurnitureRequirementRequest request) throws ApplicationServiceException {
        return roomDomainService.addFurnitureToRoomType(roomTypeId, request);
    }

    @Override
    @Transactional
    public RoomTypeResponse removeFurnitureFromRoomType(Integer roomTypeId, Integer furnitureId) throws ApplicationServiceException {
        return roomDomainService.removeFurnitureFromRoomType(roomTypeId, furnitureId);
    }

    @Override
    @Transactional
    public RoomTypeResponse updateFurnitureQuantityInRoomType(Integer roomTypeId, FurnitureRequirementRequest request) throws ApplicationServiceException {
        return roomDomainService.updateFurnitureQuantityInRoomType(roomTypeId, request);
    }
}