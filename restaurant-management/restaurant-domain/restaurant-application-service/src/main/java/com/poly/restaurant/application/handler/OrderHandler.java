package com.poly.restaurant.application.handler;

import com.poly.restaurant.application.dto.OrderDTO;
import com.poly.restaurant.domain.entity.Order;
import com.poly.restaurant.domain.entity.OrderStatus;

import java.util.List;

public interface OrderHandler extends GenericHandler<Order, String> {
    
    /**
     * Tạo đơn hàng mới với validation
     */
    Order createOrder(Order order);
    
    /**
     * Cập nhật trạng thái đơn hàng
     */
    Order updateOrderStatus(String orderId, OrderStatus newStatus);
    
    /**
     * Hủy đơn hàng
     */
    Order cancelOrder(String orderId);
    
    /**
     * Lấy đơn hàng theo khách hàng
     */
    List<Order> getOrdersByCustomer(String customerId);
    
    /**
     * Lấy đơn hàng theo bàn
     */
    List<Order> getOrdersByTable(String tableId);
    
    /**
     * Lấy đơn hàng theo trạng thái
     */
    List<Order> getOrdersByStatus(OrderStatus status);
    
    /**
     * Xử lý đơn hàng (chuyển từ NEW sang IN_PROGRESS)
     */
    Order processOrder(String orderId);
    
    /**
     * Hoàn thành đơn hàng (chuyển từ IN_PROGRESS sang COMPLETED)
     */
    Order completeOrder(String orderId);

    /**
     * Tạo đơn hàng hoàn chỉnh với validation, payment và notification
     */
    OrderDTO createOrderWithPayment(OrderDTO orderDTO);
    
    /**
     * Xử lý đơn hàng với notification
     */
    OrderDTO processOrderWithNotification(String orderId);
    
    /**
     * Hoàn thành đơn hàng với notification
     */
    OrderDTO completeOrderWithNotification(String orderId);
    
    /**
     * Hủy đơn hàng với refund và notification
     */
    OrderDTO cancelOrderWithRefundAndNotification(String orderId, String reason);
}