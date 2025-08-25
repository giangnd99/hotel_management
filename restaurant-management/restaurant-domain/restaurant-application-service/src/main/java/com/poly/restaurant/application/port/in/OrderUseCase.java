package com.poly.restaurant.application.port.in;

import com.poly.restaurant.application.dto.OrderDTO;

import java.util.List;


public interface OrderUseCase {

    // ========== BASIC CRUD OPERATIONS ==========

    /**
     * Creates a new order from the provided request.
     *
     * @param request the OrderDTO containing order details
     * @return the created OrderDTO
     */
    OrderDTO createOrder(OrderDTO request);

    /**
     * Retrieves the list of all placed orders.
     *
     * @return list of OrderDTO objects representing all orders
     */
    List<OrderDTO> getAllOrders();

    /**
     * Retrieves an order by its ID.
     *
     * @param id the order ID
     * @return the OrderDTO if found
     */
    OrderDTO getOrderById(String id);

    /**
     * Updates an existing order.
     *
     * @param id      the order ID
     * @param request the updated OrderDTO
     * @return the updated OrderDTO
     */
    OrderDTO updateOrder(String id, OrderDTO request);

    /**
     * Deletes an order by its ID.
     *
     * @param id the order ID
     */
    void deleteOrder(String id);

    // ========== BUSINESS OPERATIONS ==========

    /**
     * Tạo đơn hàng hoàn chỉnh với validation, payment và notification
     */
    OrderDTO createOrderWithPayment(OrderDTO orderDTO);

    /**
     * Cập nhật trạng thái đơn hàng
     */
    OrderDTO updateOrderStatus(String orderId, String status);

    /**
     * Hủy đơn hàng
     */
    OrderDTO cancelOrder(String orderId);

    /**
     * Xử lý đơn hàng (chuyển từ NEW sang IN_PROGRESS)
     */
    OrderDTO processOrder(String orderId);

    /**
     * Hoàn thành đơn hàng (chuyển từ IN_PROGRESS sang COMPLETED)
     */
    OrderDTO completeOrder(String orderId);

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

    // ========== QUERY OPERATIONS ==========

    /**
     * Lấy đơn hàng theo khách hàng
     */
    List<OrderDTO> getOrdersByCustomer(String customerId);

    /**
     * Lấy đơn hàng theo bàn
     */
    List<OrderDTO> getOrdersByTable(String tableId);

    /**
     * Lấy đơn hàng theo trạng thái
     */
    List<OrderDTO> getOrdersByStatus(String status);

    // ========== NEW ORDER TYPES ==========

    /**
     * Tạo đơn hàng với thanh toán trực tiếp
     * 
     * @param orderDTO Thông tin đơn hàng
     * @return OrderDTO đã được tạo
     */
//    OrderDTO createDirectOrder(OrderDTO orderDTO);
//
//    /**
//     * Tạo đơn hàng đính kèm vào room
//     *
//     * @param orderDTO Thông tin đơn hàng
//     * @return OrderDTO đã được tạo
//     */
//    OrderDTO createRoomAttachedOrder(OrderDTO orderDTO);
//
//    /**
//     * Kích hoạt payment request cho đơn hàng trực tiếp
//     *
//     * @param orderDTO Thông tin đơn hàng
//     */
//    void triggerDirectPaymentRequest(OrderDTO orderDTO);
//
//    /**
//     * Kích hoạt payment request cho đơn hàng đính kèm room (khi checkout)
//     *
//     * @param orderDTO Thông tin đơn hàng
//     */
//    void triggerRoomOrderPaymentRequest(OrderDTO orderDTO);
}
