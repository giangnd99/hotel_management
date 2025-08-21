package com.poly.booking.management.domain.outbox.scheduler;

import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.booking.management.domain.outbox.service.RoomOutboxService;
import com.poly.booking.management.domain.port.out.message.publisher.RoomCheckOutMessagePublisher;
import com.poly.domain.valueobject.BookingStatus;
import com.poly.outbox.OutboxScheduler;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Room Check Out Publish Scheduler
 * <p>
 * CHỨC NĂNG:
 * - Xử lý outbox messages cho room check out process
 * - Gửi messages đến Kafka topic "room-check-out-topic"
 * - Đảm bảo tính nhất quán dữ liệu thông qua Outbox Pattern
 * <p>
 * MỤC ĐÍCH:
 * - Tự động xử lý các outbox messages đang chờ xử lý
 * - Gửi thông tin checkout phòng đến room management service
 * - Cập nhật trạng thái outbox messages sau khi gửi thành công
 * <p>
 * PATTERNS ÁP DỤNG:
 * - Outbox Pattern: Đảm bảo message delivery reliability
 * - Saga Pattern: Quản lý distributed transaction
 * - Scheduled Task: Xử lý định kỳ các outbox messages
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RoomCheckOutPublishScheduler implements OutboxScheduler {

    private final RoomOutboxService roomOutboxService;
    private final RoomCheckOutMessagePublisher roomCheckOutMessagePublisher;

    /**
     * Xử lý outbox messages định kỳ
     * <p>
     * LOGIC FLOW:
     * 1. Lấy danh sách outbox messages đang chờ xử lý
     * 2. Gửi từng message đến Kafka topic
     * 3. Cập nhật trạng thái outbox messages
     * 4. Log kết quả xử lý
     * <p>
     * SCHEDULING:
     * - Chạy định kỳ theo cấu hình trong application.yml
     * - Xử lý batch messages để tối ưu hiệu suất
     * - Đảm bảo transaction consistency
     */
    @Override
    @Transactional
    @Scheduled(fixedDelayString = "${booking-service.outbox-scheduler-fixed-rate}",
            initialDelayString = "${booking-service.outbox-scheduler-initial-delay}")
    public void processOutboxMessage() {
        Optional<List<RoomOutboxMessage>> outboxMessagesResponse =
                roomOutboxService.getRoomOutboxMessageByBookingIdAndStatus(
                        OutboxStatus.STARTED,
                        SagaStatus.PROCESSING);

        if (outboxMessagesResponse.isPresent() && !outboxMessagesResponse.get().isEmpty()) {
            List<RoomOutboxMessage> outboxMessages = outboxMessagesResponse.get();
            outboxMessages.stream().filter(outboxMessage -> outboxMessage.getBookingStatus().equals(BookingStatus.CHECKED_OUT)).toList();
            if (!outboxMessages.isEmpty()) {
                log.info("Received {} RoomCheckOutOutboxMessage with ids: {}, sending to message bus!",
                        outboxMessages.size(),
                        outboxMessages.stream().map(outboxMessage ->
                                outboxMessage.getId().toString()).collect(Collectors.joining(",")));

                // Gửi từng message đến Kafka topic
                outboxMessages.forEach(outboxMessage ->
                        roomCheckOutMessagePublisher.sendRoomCheckOutRequest(outboxMessage, this::updateOutboxStatus));

                log.info("{} RoomCheckOutOutboxMessage sent to message bus!", outboxMessages.size());
            }
        }
    }

    /**
     * Cập nhật trạng thái outbox message
     * <p>
     * Được gọi sau khi message được gửi thành công hoặc thất bại
     *
     * @param roomOutboxMessage Outbox message cần cập nhật
     * @param outboxStatus      Trạng thái mới của outbox message
     */
    private void updateOutboxStatus(RoomOutboxMessage roomOutboxMessage, OutboxStatus outboxStatus) {
        roomOutboxMessage.setOutboxStatus(outboxStatus);
        roomOutboxService.save(roomOutboxMessage);
        log.info("RoomCheckOutOutboxMessage is updated with outbox status: {}", outboxStatus.name());
    }
}
