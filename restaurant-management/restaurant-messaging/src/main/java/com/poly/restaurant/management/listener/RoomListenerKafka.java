package com.poly.restaurant.management.listener;

import com.poly.config.KafkaTopicsConfig;
import com.poly.consumer.AbstractConsumer;
import com.poly.dedup.DedupStore;
import com.poly.message.BaseResponse;
import com.poly.message.model.payment.PaymentResponseMessage;
import com.poly.message.model.room.RoomResponseMessage;
import com.poly.restaurant.application.port.in.message.listener.RoomResponseListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class RoomListenerKafka extends AbstractConsumer<RoomResponseMessage, BaseResponse> {

    private final RoomResponseListener roomResponseListener;

    protected RoomListenerKafka(DedupStore dedup,
                                long dedupTtlMs,
                                long responseCacheTtlMs,
                                RoomResponseListener roomResponseListener) {
        super(dedup, dedupTtlMs, responseCacheTtlMs);
        this.roomResponseListener = roomResponseListener;
    }

    @Override
    protected String idempotencyKey(RoomResponseMessage msg) {
        return msg.getSourceService() + msg.getCorrelationId() + msg.getRoomId();
    }

    @Override
    protected BaseResponse handleBusiness(RoomResponseMessage request) {
        log.info("Received room response for order {}. Status: {}", request.getCorrelationId(), request.getStatus());
        if (request.getRoomApprovalStatus().equalsIgnoreCase("APPROVED")) {
            roomResponseListener.onRoomMergeSuccess(request);
        } else {
            log.warn("Room merge failed for order {}. Reason: {}", request.getCorrelationId(), request.getErrorMessage());
            roomResponseListener.onRoomMergeFailure(request);
        }
        return null;
    }

    @KafkaListener(topics = KafkaTopicsConfig.ROOM_APPROVAL_RESPONSE_TOPIC,
            containerFactory = "jsonKafkaListenerContainerFactory")
    public void listenPaymentResponse(@Payload List<RoomResponseMessage> messages,
                                      @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                                      @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                                      @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        super.handleBatch(messages, keys, partitions, offsets);
    }
}
