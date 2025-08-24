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

    @Query("select distinct o from OrderJpaEntity o left join fetch o.items")
    List<OrderJpaEntity> findAllWithItems();

    @Query("select o from OrderJpaEntity o left join fetch o.items where o.id = :id")
    Optional<OrderJpaEntity> findByIdWithItems(@Param("id") String id);
}
