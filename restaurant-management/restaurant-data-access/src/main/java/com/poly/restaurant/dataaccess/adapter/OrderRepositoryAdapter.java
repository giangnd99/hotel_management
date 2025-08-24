package com.poly.restaurant.dataaccess.adapter;

import com.poly.restaurant.application.port.out.OrderRepositoryPort;
import com.poly.restaurant.dataaccess.entity.OrderJpaEntity;
import com.poly.restaurant.dataaccess.jpa.JpaMenuItemRepository;
import com.poly.restaurant.dataaccess.jpa.JpaOrderRepository;
import com.poly.restaurant.dataaccess.mapper.OrderEntityMapper;
import com.poly.restaurant.domain.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final JpaOrderRepository jpaOrderRepository;
    private final JpaMenuItemRepository jpaMenuItemRepository;

    @Override
    public Order save(Order order) {
        OrderJpaEntity entity = OrderEntityMapper.toEntity(order, jpaMenuItemRepository);
        OrderJpaEntity savedEntity = jpaOrderRepository.save(entity);
        return OrderEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Order> findById(String id) {
        return jpaOrderRepository.findById(id)
                .map(OrderEntityMapper::toDomain);
    }

    @Override
    public List<Order> findAll() {
        return jpaOrderRepository.findAll().stream()
                .map(OrderEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(String id) {
        return jpaOrderRepository.existsById(id);
    }

    @Override
    public void deleteById(String id) {
        jpaOrderRepository.deleteById(id);
    }
}
