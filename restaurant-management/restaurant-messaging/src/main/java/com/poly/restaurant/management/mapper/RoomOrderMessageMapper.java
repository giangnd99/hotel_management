package com.poly.restaurant.management.mapper;

import com.poly.restaurant.management.message.RoomOrderRequestMessage;
import com.poly.restaurant.management.message.RoomOrderRequestMessageAvro;
import com.poly.restaurant.management.message.RoomOrderResponseMessage;
import com.poly.restaurant.management.message.RoomOrderResponseMessageAvro;
import org.springframework.stereotype.Component;

/**
 * Mapper để chuyển đổi giữa domain messages và Avro messages cho room order
 * 
 * NGHIỆP VỤ:
 * - Chuyển đổi RoomOrderRequestMessage ↔ RoomOrderRequestMessageAvro
 * - Chuyển đổi RoomOrderResponseMessage ↔ RoomOrderResponseMessageAvro
 * 
 * PATTERNS ÁP DỤNG:
 * - Mapper Pattern: Chuyển đổi giữa các loại object
 * - Factory Pattern: Tạo Avro objects
 */
@Component
public class RoomOrderMessageMapper {

    /**
     * Chuyển đổi RoomOrderRequestMessage thành RoomOrderRequestMessageAvro
     */
    public RoomOrderRequestMessageAvro roomOrderRequestMessageToRoomOrderRequestMessageAvro(
            RoomOrderRequestMessage roomOrderRequestMessage) {
        if (roomOrderRequestMessage == null) {
            return null;
        }

        return RoomOrderRequestMessageAvro.newBuilder()
                .setId(roomOrderRequestMessage.getId())
                .setOrderId(roomOrderRequestMessage.getOrderId())
                .setRoomId(roomOrderRequestMessage.getRoomId())
                .setCustomerId(roomOrderRequestMessage.getCustomerId())
                .setAmount(roomOrderRequestMessage.getAmount())
                .setOrderStatus(roomOrderRequestMessage.getOrderStatus())
                .setRequestType(roomOrderRequestMessage.getRequestType())
                .build();
    }

    /**
     * Chuyển đổi RoomOrderRequestMessageAvro thành RoomOrderRequestMessage
     */
    public RoomOrderRequestMessage roomOrderRequestMessageAvroToRoomOrderRequestMessage(
            RoomOrderRequestMessageAvro roomOrderRequestMessageAvro) {
        if (roomOrderRequestMessageAvro == null) {
            return null;
        }

        return RoomOrderRequestMessage.builder()
                .id(roomOrderRequestMessageAvro.getId())
                .orderId(roomOrderRequestMessageAvro.getOrderId())
                .roomId(roomOrderRequestMessageAvro.getRoomId())
                .customerId(roomOrderRequestMessageAvro.getCustomerId())
                .amount(roomOrderRequestMessageAvro.getAmount())
                .orderStatus(roomOrderRequestMessageAvro.getOrderStatus())
                .requestType(roomOrderRequestMessageAvro.getRequestType())
                .build();
    }

    /**
     * Chuyển đổi RoomOrderResponseMessage thành RoomOrderResponseMessageAvro
     */
    public RoomOrderResponseMessageAvro roomOrderResponseMessageToRoomOrderResponseMessageAvro(
            RoomOrderResponseMessage roomOrderResponseMessage) {
        if (roomOrderResponseMessage == null) {
            return null;
        }

        return RoomOrderResponseMessageAvro.newBuilder()
                .setId(roomOrderResponseMessage.getId())
                .setOrderId(roomOrderResponseMessage.getOrderId())
                .setRoomId(roomOrderResponseMessage.getRoomId())
                .setCustomerId(roomOrderResponseMessage.getCustomerId())
                .setResponseStatus(roomOrderResponseMessage.getResponseStatus())
                .setMessage(roomOrderResponseMessage.getMessage())
                .setRequestType(roomOrderResponseMessage.getRequestType())
                .build();
    }

    /**
     * Chuyển đổi RoomOrderResponseMessageAvro thành RoomOrderResponseMessage
     */
    public RoomOrderResponseMessage roomOrderResponseMessageAvroToRoomOrderResponseMessage(
            RoomOrderResponseMessageAvro roomOrderResponseMessageAvro) {
        if (roomOrderResponseMessageAvro == null) {
            return null;
        }

        return RoomOrderResponseMessage.builder()
                .id(roomOrderResponseMessageAvro.getId())
                .orderId(roomOrderResponseMessageAvro.getOrderId())
                .roomId(roomOrderResponseMessageAvro.getRoomId())
                .customerId(roomOrderResponseMessageAvro.getCustomerId())
                .responseStatus(roomOrderResponseMessageAvro.getResponseStatus())
                .message(roomOrderResponseMessageAvro.getMessage())
                .requestType(roomOrderResponseMessageAvro.getRequestType())
                .build();
    }
}
