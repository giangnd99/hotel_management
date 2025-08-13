package com.poly.restaurant.application.handler.conmand;

import com.poly.restaurant.application.dto.OrderDTO;

public interface CreateOrderWithRoomDetailCommand {
    /**
     * Tạo đơn hàng với chi tiết phòng.
     *
     * @param orderDTO DTO chứa thông tin đơn hàng và chi tiết phòng
     * @return OrderDTO đã được tạo
     */
    OrderDTO createOrderWithRoomDetail(OrderDTO orderDTO);

    /**
     * Kích hoạt yêu cầu thanh toán cho đơn hàng đã tạo.
     *
     * @param orderDTO DTO chứa thông tin đơn hàng đã tạo
     */
    void triggerPaymentRequest(OrderDTO orderDTO);
}
