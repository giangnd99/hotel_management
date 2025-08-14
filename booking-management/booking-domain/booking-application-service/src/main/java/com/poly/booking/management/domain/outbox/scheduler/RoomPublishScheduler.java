package com.poly.booking.management.domain.outbox.scheduler.room;

import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.booking.management.domain.outbox.service.RoomOutboxService;
import com.poly.booking.management.domain.port.out.message.publisher.room.RoomRequestReserveMessagePublisher;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class RoomPublishScheduler implements OutboxScheduler {

    private final RoomOutboxService roomOutboxService;
    private final RoomRequestReserveMessagePublisher publish;

    @Override
    @Transactional
    @Scheduled(fixedDelayString = "${booking-service.outbox-scheduler-fixed-rate}",
            initialDelayString = "${booking-service.outbox-scheduler-initial-delay}")
    public void processOutboxMessage() {
        Optional<List<RoomOutboxMessage>> outboxMessagesResponse =
                roomOutboxService.getRoomOutboxMessageByBookingIdAndStatus(
                        OutboxStatus.STARTED,
                        SagaStatus.PROCESSING);
        if (outboxMessagesResponse.isPresent() && outboxMessagesResponse.get().size() > 0) {
            List<RoomOutboxMessage> outboxMessages = outboxMessagesResponse.get();
            log.info("Received {} OrderApprovalOutboxMessage with ids: {}, sending to message bus!",
                    outboxMessages.size(),
                    outboxMessages.stream().map(outboxMessage ->
                            outboxMessage.getId().toString()).collect(Collectors.joining(",")));
            outboxMessages.forEach(outboxMessage ->
                    publish.sendRoomReserveRequest(outboxMessage, this::updateOutboxStatus));
            log.info("{} OrderApprovalOutboxMessage sent to message bus!", outboxMessages.size());

        }
    }

    private void updateOutboxStatus(RoomOutboxMessage roomOutboxMessage, OutboxStatus outboxStatus) {
        roomOutboxMessage.setOutboxStatus(outboxStatus);
        roomOutboxService.save(roomOutboxMessage);
        log.info("OrderApprovalOutboxMessage is updated with outbox status: {}", outboxStatus.name());
    }
}
