package com.poly.restaurant.application.handler;

import com.poly.restaurant.domain.entity.MenuItem;
import com.poly.restaurant.domain.entity.MenuItemStatus;

import java.util.List;

public interface MenuItemHandler extends GenericHandler<MenuItem, Integer> {
    
    /**
     * Tìm kiếm món ăn theo tên
     */
    List<MenuItem> searchByName(String name);
    
    /**
     * Lấy món ăn theo danh mục
     */
    List<MenuItem> getByCategory(String category);
    
    /**
     * Lấy món ăn có sẵn (available)
     */
    List<MenuItem> getAvailableItems();
    
    /**
     * Lấy món ăn hết hàng (out of stock)
     */
    List<MenuItem> getOutOfStockItems();
    
    /**
     * Cập nhật số lượng món ăn
     */
    MenuItem updateQuantity(Integer menuItemId, int newQuantity);
    
    /**
     * Giảm số lượng món ăn (khi đặt hàng)
     */
    MenuItem reduceQuantity(Integer menuItemId, int amount);
    
    /**
     * Tăng số lượng món ăn (khi nhập hàng)
     */
    MenuItem addQuantity(Integer menuItemId, int amount);
    
    /**
     * Cập nhật trạng thái món ăn
     */
    MenuItem updateStatus(Integer menuItemId, MenuItemStatus status);
    
    /**
     * Cập nhật giá món ăn
     */
    MenuItem updatePrice(Integer menuItemId, java.math.BigDecimal newPrice);
    
    /**
     * Kiểm tra món ăn có sẵn không
     */
    boolean isItemAvailable(Integer menuItemId);
    
    /**
     * Kiểm tra số lượng đủ để đặt hàng
     */
    boolean hasSufficientQuantity(Integer menuItemId, int requestedQuantity);
}
