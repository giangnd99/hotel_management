package com.poly.restaurant.application.port.in;

import com.poly.restaurant.application.dto.*;

import java.util.List;

public interface RestaurantUseCase {

    /**
     * Lấy danh sách bàn ăn với trạng thái hiện tại (AVAILABLE, OCCUPIED, RESERVED)
     */
    List<TableDTO> getAllTables();

    /**
     * Lấy danh sách món ăn (menu)
     */
    List<MenuItemDTO> getMenu();

    /**
     * Tạo đơn hàng mới từ request gửi lên
     */
    OrderDTO createOrder(CreateOrderRequest request);

    /**
     * Lấy danh sách đơn hàng đã đặt
     */
    List<OrderDTO> getAllOrders();

    /**
     * Lấy danh sách nhân viên
     */
    List<StaffDTO> getAllStaff();
}

