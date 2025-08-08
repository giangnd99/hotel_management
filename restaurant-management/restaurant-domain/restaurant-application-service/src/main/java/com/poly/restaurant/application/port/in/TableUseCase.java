package com.poly.restaurant.application.port.in;

import com.poly.restaurant.application.dto.TableDTO;

import java.util.List;

public interface TableUseCase {

    /**
     * Lấy tất cả bàn
     */
    List<TableDTO> getAllTables();

    /**
     * Lấy bàn theo ID
     */
    TableDTO getTableById(String id);

    /**
     * Tạo bàn mới
     */
    TableDTO createTable(TableDTO request);

    /**
     * Cập nhật bàn
     */
    TableDTO updateTable(String id, TableDTO request);

    /**
     * Xóa bàn
     */
    void deleteTable(String id);

    /**
     * Lấy bàn theo trạng thái
     */
    List<TableDTO> getTablesByStatus(String status);

    /**
     * Lấy bàn theo số bàn
     */
    TableDTO getTableByNumber(int number);

    /**
     * Đặt bàn
     */
    TableDTO reserveTable(String tableId);

    /**
     * Sử dụng bàn
     */
    TableDTO occupyTable(String tableId);

    /**
     * Giải phóng bàn
     */
    TableDTO freeTable(String tableId);

    /**
     * Cập nhật trạng thái bàn
     */
    TableDTO updateTableStatus(String tableId, String status);
}
