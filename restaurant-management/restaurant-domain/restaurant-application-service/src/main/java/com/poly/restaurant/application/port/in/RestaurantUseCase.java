package com.poly.restaurant.application.port.in;

import com.poly.restaurant.application.dto.*;

import java.util.List;

public interface RestaurantUseCase {
    /**
     * Retrieves the list of tables with their current status (AVAILABLE, OCCUPIED, RESERVED).
     *
     * @return list of TableDTO objects representing all tables
     */
    List<TableDTO> getAllTables();

    /**
     * Retrieves the list of staff members.
     *
     * @return list of StaffDTO objects representing all staff
     */
    List<StaffDTO> getAllStaff();

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


}
