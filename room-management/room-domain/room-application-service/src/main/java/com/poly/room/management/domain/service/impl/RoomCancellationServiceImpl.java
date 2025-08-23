package com.poly.room.management.domain.service.impl;

import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.event.RoomCancellationEvent;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.domain.valueobject.RoomStatus;
import com.poly.room.management.domain.service.RoomCancellationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Room Cancellation Domain Service - Xử lý business logic hủy phòng
 * <p>
 * CHỨC NĂNG:
 * - Validate điều kiện hủy phòng
 * - Thực hiện hủy phòng và tạo domain event
 * - Cập nhật trạng thái phòng
 * <p>
 * MỤC ĐÍCH:
 * - Đảm bảo business rules được tuân thủ khi hủy phòng
 * - Xử lý logic giải phóng phòng
 * - Cập nhật trạng thái phòng một cách nhất quán
 */
@Slf4j
@Service
public class RoomCancellationServiceImpl implements RoomCancellationService {

    /**
     * Hủy phòng với validation đầy đủ
     * <p>
     * BUSINESS RULES:
     * - Chỉ cho phép hủy phòng có trạng thái hợp lệ
     * - Phòng phải đang được đặt (RESERVED hoặc OCCUPIED)
     * - Cập nhật trạng thái phòng về VACANT
     * <p>
     * VALIDATION:
     * - Kiểm tra trạng thái phòng có thể hủy
     * - Kiểm tra phòng có đang được đặt hay không
     * - Xác định lý do hủy
     *
     * @param room Phòng cần hủy
     * @param bookingId ID của booking bị hủy
     * @param cancellationReason Lý do hủy
     * @return RoomCancellationEvent chứa thông tin hủy
     */
    public RoomCancellationEvent cancelRoom(Room room, String bookingId, String cancellationReason) {
        log.info("Processing room cancellation for room: {} with booking: {} and reason: {}", 
                room.getRoomNumber(), bookingId, cancellationReason);

        // Validate phòng có thể hủy
        validateRoomCanBeCancelled(room);

        // Cập nhật trạng thái phòng
        room.setRoomStatus(RoomStatus.VACANT);

        // Tạo domain event
        RoomCancellationEvent cancelledEvent = new RoomCancellationEvent(room, bookingId, cancellationReason);

        log.info("Room cancelled successfully: {}. Status updated to: {}", 
                room.getRoomNumber(), room.getRoomStatus());

        return cancelledEvent;
    }

    /**
     * Validate phòng có thể hủy hay không
     * <p>
     * CHECKS:
     * - Trạng thái phòng phải hợp lệ để hủy
     * - Không thể hủy phòng đã trống
     * - Phòng phải đang được đặt
     *
     * @param room Phòng cần validate
     * @throws RoomDomainException nếu không thể hủy
     */
    private void validateRoomCanBeCancelled(Room room) {
        if (room == null) {
            throw new RoomDomainException("Room cannot be null for cancellation");
        }

        RoomStatus currentStatus = room.getRoomStatus();
        
        if (currentStatus == RoomStatus.VACANT) {
            throw new RoomDomainException("Room is already vacant: " + room.getRoomNumber());
        }

        if (currentStatus == RoomStatus.MAINTENANCE) {
            throw new RoomDomainException("Cannot cancel room under maintenance: " + room.getRoomNumber());
        }

        if (currentStatus == RoomStatus.CLEANING) {
            throw new RoomDomainException("Cannot cancel room being cleaned: " + room.getRoomNumber());
        }

        log.debug("Room validation passed for cancellation: {}", room.getRoomNumber());
    }

    /**
     * Kiểm tra xem có thể hủy phòng hay không
     *
     * @param room Phòng cần kiểm tra
     * @return true nếu có thể hủy
     */
    public boolean canCancelRoom(Room room) {
        try {
            validateRoomCanBeCancelled(room);
            return true;
        } catch (RoomDomainException e) {
            log.debug("Room cannot be cancelled: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Lấy danh sách phòng có thể hủy
     *
     * @param rooms Danh sách phòng cần kiểm tra
     * @return Danh sách phòng có thể hủy
     */
    public List<Room> getCancellableRooms(List<Room> rooms) {
        return rooms.stream()
                .filter(this::canCancelRoom)
                .collect(Collectors.toList());
    }
}
