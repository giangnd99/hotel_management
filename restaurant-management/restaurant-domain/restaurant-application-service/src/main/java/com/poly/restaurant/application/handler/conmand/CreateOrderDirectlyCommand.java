package com.poly.restaurant.application.handler.conmand;

import com.poly.restaurant.application.dto.OrderDTO;

public interface CreateOrderDirectlyCommand {

    OrderDTO createOrder(OrderDTO orderDTO);
    void triggerPaymentRequest(OrderDTO orderDTO);
}
