package com.poly.room.management.domain.port.in.service;

import com.poly.application.handler.ApplicationServiceException;
import com.poly.room.management.domain.dto.request.CreateRoomRequest;
import com.poly.room.management.domain.dto.request.UpdateRoomRequest;
import com.poly.room.management.domain.dto.response.RoomResponse;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface RoomService {
    RoomResponse createRoom(CreateRoomRequest request) throws ApplicationServiceException;
    RoomResponse updateRoom(UpdateRoomRequest request) throws ApplicationServiceException;
    void deleteRoom(Integer roomId) throws ApplicationServiceException;
    RoomResponse getRoomById(Integer roomId) throws ApplicationServiceException;
    List<RoomResponse> getAllRooms();
    RoomResponse updateRoomStatus(Integer roomId, String newStatus) throws ApplicationServiceException;
    List<RoomResponse> findAvailableRooms(Optional<Integer> roomTypeId, Optional<Integer> minFloor, Optional<Integer> maxFloor, Timestamp checkInDate, Timestamp checkOutDate);
    List<RoomResponse> getRoomsByStatus(String statusName);
    void requestInventoryItemForRoom(Integer roomId, String inventoryItemId, int quantity, String requestedByStaffId) throws ApplicationServiceException;
}