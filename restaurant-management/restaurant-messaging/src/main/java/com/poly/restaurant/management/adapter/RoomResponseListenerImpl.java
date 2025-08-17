package com.poly.restaurant.management.adapter;

import com.poly.message.model.room.RoomResponseMessage;
import com.poly.restaurant.application.port.in.message.listener.RoomResponseListener;
import com.poly.restaurant.application.port.out.OrderRepositoryPort;
import com.poly.restaurant.application.port.out.RoomOrderRepositoryPort;
import com.poly.restaurant.application.port.out.RoomRepositoryPort;
import com.poly.restaurant.domain.entity.Order;
import com.poly.restaurant.domain.entity.OrderRoom;
import com.poly.restaurant.domain.entity.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
@Slf4j
public class RoomResponseListenerImpl implements RoomResponseListener {

    private final OrderRepositoryPort orderRepositoryPort;
    private final RoomOrderRepositoryPort roomOrderRepositoryPort;
    private final RoomRepositoryPort roomRepositoryPort;

    @Override
    public void onRoomMergeSuccess(RoomResponseMessage roomResponseMessage) {
        Order orderToUpdate = getOrderFromMessage(roomResponseMessage);
        if (orderToUpdate == null) {
            return;
        }
        orderToUpdate.canBeModified();
        updateOrder(orderToUpdate);
        saveRoomOrder(roomResponseMessage);
        log.info("Room merge successful for order with id: {}", roomResponseMessage.getCorrelationId());
    }

    @Override
    public void onRoomMergeFailure(RoomResponseMessage roomResponseMessage) {
        Order orderToUpdate = getOrderFromMessage(roomResponseMessage);
        if (orderToUpdate == null) {
            return;
        }
        orderToUpdate.isCancelled();
        log.info("Room merge failed for order with id: {}", roomResponseMessage.getCorrelationId());
    }

    private Order getOrderFromMessage(RoomResponseMessage roomResponseMessage) {

        Optional<Order> order = orderRepositoryPort.findById(roomResponseMessage.getCorrelationId());
        if (order.isEmpty()) {
            log.error("Order not found with id: {}", roomResponseMessage.getCorrelationId());
            return null;
        }
        return order.get();
    }

    private void updateOrder(Order order) {
        orderRepositoryPort.save(order);
        log.info("Order with id: {} updated with status {}", order.getId(),
                order.getStatus() == null ? "null" : order.getStatus().name());
    }

    private void saveRoomOrder(RoomResponseMessage roomResponseMessage) {
        Order order = getOrderFromMessage(roomResponseMessage);
        Room room = getRoomFromMessage(roomResponseMessage);

        assert order != null;

        OrderRoom orderRoom = OrderRoom.builder()
                .order(order)
                .room(room)
                .price(order.getTotalPrice())
                .build();

        roomOrderRepositoryPort.save(orderRoom);
        log.info("Room order saved for order with id: {}", roomResponseMessage.getCorrelationId());
    }

    private Room getRoomFromMessage(RoomResponseMessage roomResponseMessage) {
        Optional<Room> room = roomRepositoryPort.findById(roomResponseMessage.getRoomId());
        if (room.isEmpty()) {
            log.error("Room not found with id: {}", roomResponseMessage.getRoomId());
            return null;
        }
        return room.get();
    }
}
