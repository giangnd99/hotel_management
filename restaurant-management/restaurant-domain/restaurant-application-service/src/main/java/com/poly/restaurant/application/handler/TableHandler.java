package com.poly.restaurant.application.handler;

import com.poly.restaurant.domain.entity.Table;
import com.poly.restaurant.domain.entity.TableStatus;

import java.util.List;

public interface TableHandler extends GenericHandler<Table, String> {

    /**
     * Tạo bàn mới với validation
     */
    Table createTable(Table table);

    /**
     * Cập nhật trạng thái bàn
     */
    Table updateTableStatus(String tableId, TableStatus newStatus);

    /**
     * Lấy bàn theo trạng thái
     */
    List<Table> getTablesByStatus(TableStatus status);

    /**
     * Lấy bàn theo số bàn
     */
    Table getTableByNumber(int number);

    /**
     * Kiểm tra bàn có sẵn không
     */
    boolean isTableAvailable(String tableId);

    /**
     * Đặt bàn (chuyển từ AVAILABLE sang RESERVED)
     */
    Table reserveTable(String tableId);

    /**
     * Sử dụng bàn (chuyển từ AVAILABLE/RESERVED sang OCCUPIED)
     */
    Table occupyTable(String tableId);

    /**
     * Giải phóng bàn (chuyển từ OCCUPIED sang AVAILABLE)
     */
    Table freeTable(String tableId);
}
