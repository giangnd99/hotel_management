package com.poly.restaurant.dataaccess.jpa;

import com.poly.restaurant.dataaccess.entity.TableJpaEntity;
import com.poly.restaurant.domain.entity.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaTableRepository extends JpaRepository<TableJpaEntity, String> {

    /**
     * Tìm bàn theo số bàn
     */
    Optional<TableJpaEntity> findByNumber(Integer number);

    /**
     * Lấy tất cả bàn theo trạng thái
     */
    List<TableJpaEntity> findByStatus(TableStatus status);

    /**
     * Kiểm tra số bàn đã tồn tại chưa
     */
    boolean existsByNumber(Integer number);
}