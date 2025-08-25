package com.poly.restaurant.dataaccess.jpa;

import com.poly.restaurant.dataaccess.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaOrderRepository extends JpaRepository<OrderJpaEntity, String> {

    @Query("select distinct o from OrderJpaEntity o left join fetch o.items i left join fetch i.menuItem")
    List<OrderJpaEntity> findAllWithItems();

    @Query("select o from OrderJpaEntity o left join fetch o.items i left join fetch i.menuItem where o.id = :id")
    Optional<OrderJpaEntity> findByIdWithItems(@Param("id") String id);

    @Query("select distinct o from OrderJpaEntity o left join fetch o.items i left join fetch i.menuItem where o.customerId = :customerId")
    List<OrderJpaEntity> findByCustomerIdWithItems(@Param("customerId") String customerId);

    @Query("select distinct o from OrderJpaEntity o left join fetch o.items i left join fetch i.menuItem where o.roomId = :tableId")
    List<OrderJpaEntity> findByTableIdWithItems(@Param("tableId") String tableId);

    @Query("select distinct o from OrderJpaEntity o left join fetch o.items i left join fetch i.menuItem where o.status = :status")
    List<OrderJpaEntity> findByStatusWithItems(@Param("status") com.poly.restaurant.domain.entity.OrderStatus status);
}
