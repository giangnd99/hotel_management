package com.poly.room.management.application.service;

import com.poly.application.handler.ApplicationServiceException;
import com.poly.room.management.domain.dto.request.CreateRoomRequest;
import com.poly.room.management.domain.dto.request.UpdateRoomRequest;
import com.poly.room.management.domain.dto.response.RoomResponse;
import com.poly.room.management.domain.port.in.service.RoomService;
import com.poly.room.management.domain.service.RoomDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomDomainService roomDomainService;

    @Override
    @Transactional
    public RoomResponse createRoom(CreateRoomRequest request) throws ApplicationServiceException {
        return roomDomainService.createRoom(request);
    }

    @Override
    @Transactional
    public RoomResponse updateRoom(UpdateRoomRequest request) throws ApplicationServiceException {
        return roomDomainService.updateRoom(request);
    }

    @Override
    @Transactional
    public void deleteRoom(Integer roomId) throws ApplicationServiceException {
        roomDomainService.deleteRoom(roomId);
    }

    @Override
    public RoomResponse getRoomById(Integer roomId) throws ApplicationServiceException {
        return roomDomainService.getRoomById(roomId);
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        return roomDomainService.getAllRooms();
    }

    @Override
    @Transactional
    public RoomResponse updateRoomStatus(Integer roomId, String newStatus) throws ApplicationServiceException {
        return roomDomainService.updateRoomStatus(roomId, newStatus);
    }

    @Override
    public List<RoomResponse> findAvailableRooms(Optional<Integer> roomTypeId, Optional<Integer> minFloor, 
                                                Optional<Integer> maxFloor, Timestamp checkInDate, Timestamp checkOutDate) {
        return roomDomainService.findAvailableRooms(roomTypeId, minFloor, maxFloor, checkInDate, checkOutDate);
    }

    @Override
    public List<RoomResponse> getRoomsByStatus(String statusName) {
        return roomDomainService.getRoomsByStatus(statusName);
    }

    @Override
    @Transactional
    public void requestInventoryItemForRoom(Integer roomId, Integer inventoryItemId, int quantity, Integer requestedByStaffId) 
            throws ApplicationServiceException {
        roomDomainService.requestInventoryItemForRoom(roomId, inventoryItemId, quantity, requestedByStaffId);
    }
}