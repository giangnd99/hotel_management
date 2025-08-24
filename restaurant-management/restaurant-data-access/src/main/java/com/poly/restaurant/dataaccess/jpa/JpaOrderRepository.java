package com.poly.restaurant.dataaccess.jpa;

import com.poly.restaurant.dataaccess.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOrderRepository extends JpaRepository<OrderJpaEntity, String> {
}
