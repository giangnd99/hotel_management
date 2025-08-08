package com.poly.restaurant.application.port.in;

import com.poly.restaurant.application.dto.MenuDTO;
import com.poly.restaurant.application.dto.ReviewDTO;

import java.util.List;

public interface MenuUseCase {

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
    void updateMenu(Integer id, MenuDTO request);

    /**
     * Deletes a menu item.
     *
     * @param id the ID of the menu item to delete
     */
    void deleteMenu(Integer id);

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

    MenuDTO getMenuItemById(Integer id);
    List<MenuDTO> getMenuItemsByCategory(String category);
}
