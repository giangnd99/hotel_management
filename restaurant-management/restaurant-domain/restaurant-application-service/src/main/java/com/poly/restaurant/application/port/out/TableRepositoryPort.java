package com.poly.restaurant.application.port.out.repo;

import com.poly.restaurant.domain.entity.Table;
import com.poly.restaurant.domain.entity.TableStatus;

import java.util.List;
import java.util.Optional;

public interface TableRepositoryPort extends RepositoryPort<Table, String> {

    /**
     * Tìm bàn theo số bàn
     */
    Optional<Table> findByNumber(int number);

    /**
     * Lấy tất cả bàn theo trạng thái
     */
    List<Table> findByStatus(TableStatus status);

    /**
     * Kiểm tra số bàn đã tồn tại chưa
     */
    boolean existsByNumber(int number);

    /**
     * Lấy bàn có sẵn (AVAILABLE)
     */
    List<Table> findAvailableTables();
}
