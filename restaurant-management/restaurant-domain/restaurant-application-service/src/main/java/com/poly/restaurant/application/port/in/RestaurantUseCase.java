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

    /**
     * Retrieves the list of menu items.
     *
     * @return list of MenuDTO objects representing the menu
     */
    List<MenuDTO> getMenu();

    /**
     * Creates a new menu item.
     *
     * @param request the MenuDTO containing menu item details
     */
    void createMenu(MenuDTO request);

    /**
     * Updates an existing menu item.
     *
     * @param id      the ID of the menu item to update
     * @param request the MenuDTO containing updated details
     */
    void updateMenu(Long id, MenuDTO request);

    /**
     * Deletes a menu item.
     *
     * @param id the ID of the menu item to delete
     */
    void deleteMenu(Long id);

    /**
     * Adds a review to a menu item.
     *
     * @param menuId  the ID of the menu item
     * @param request the ReviewDTO containing review details
     */
    void addReview(Long menuId, ReviewDTO request);

    /**
     * Retrieves reviews for a menu item.
     *
     * @param menuId the ID of the menu item
     * @return an Object containing the reviews for the menu item
     */
    Object getReviews(Long menuId);
}
