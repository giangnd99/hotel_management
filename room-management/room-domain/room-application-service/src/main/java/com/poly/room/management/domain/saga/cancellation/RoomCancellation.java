package com.poly.room.management.domain.saga.cancellation;

import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.message.RoomCancellationRequestMessage;
import com.poly.room.management.domain.message.RoomCancellationResponseMessage;
import com.poly.room.management.domain.port.out.repository.RoomRepository;
import com.poly.room.management.domain.service.RoomCancellationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Room Cancellation Saga Helper - Hỗ trợ xử lý business logic hủy phòng
 * <p>
 * CHỨC NĂNG:
 * - Validate và quản lý outbox messages cho cancellation
 * - Thực hiện business logic hủy phòng
 * - Quản lý saga status và outbox messages
 * - Gửi thông báo hủy phòng đến booking service
 * <p>
 * MỤC ĐÍCH:
 * - Tách biệt business logic khỏi saga step
 * - Cung cấp các helper methods cho cancellation process
 * - Đảm bảo tính nhất quán dữ liệu
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RoomCancellation {

    private final RoomRepository roomRepository;
    private final RoomCancellationService roomCancellationDomainService;

    public RoomCancellationResponseMessage cancelRoom(RoomCancellationRequestMessage requestMessage) {
        log.info("Executing room cancellation for room: {}", requestMessage.getRoomId());

        try {
            // Tìm room entity
            Room room = findRoom(requestMessage.getRoomId());

            // Thực hiện business logic hủy phòng
            roomCancellationDomainService.cancelRoom(room, requestMessage.getBookingId().toString(), requestMessage.getCancellationReason());

            // Lưu trạng thái mới
            roomRepository.save(room);

            log.info("Room cancellation successful for room: {}", room.getRoomNumber());

            return RoomCancellationResponseMessage.builder()
                    .bookingId(requestMessage.getBookingId())
                    .roomId(requestMessage.getRoomId())
                    .cancellationStatus("SUCCESS")
                    .message("Room " + room.getRoomNumber() + " has been successfully cancelled.")
                    .build();

        } catch (Exception e) {
            log.error("Failed to cancel room for id: {}. Reason: {}", requestMessage.getRoomId(), e.getMessage());

            return RoomCancellationResponseMessage.builder()
                    .bookingId(requestMessage.getBookingId())
                    .roomId(requestMessage.getRoomId())
                    .cancellationStatus("FAILED")
                    .message("Room cancellation failed. Reason: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Tìm room theo ID
     *
     * @param roomId Room ID
     * @return Room entity
     */
    private Room findRoom(UUID roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found for cancellation: " + roomId));
    }
}
