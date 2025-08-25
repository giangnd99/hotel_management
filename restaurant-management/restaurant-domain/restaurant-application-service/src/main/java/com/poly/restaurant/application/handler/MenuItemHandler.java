package com.poly.restaurant.application.handler;

import com.poly.restaurant.domain.entity.MenuItem;
import com.poly.restaurant.domain.entity.MenuItemStatus;

import java.util.List;

public interface MenuItemHandler extends GenericHandler<MenuItem, String> {
    
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
     * Cập nhật trạng thái món ăn
     */
    MenuItem updateStatus(String menuItemId, MenuItemStatus status);
    
    /**
     * Cập nhật giá món ăn
     */
    MenuItem updatePrice(String menuItemId, java.math.BigDecimal newPrice);
    
    /**
     * Kiểm tra món ăn có sẵn không
     */
    boolean isItemAvailable(String menuItemId);
}
