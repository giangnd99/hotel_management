package com.poly.restaurant.dataaccess.adapter;

import com.poly.restaurant.application.port.out.OrderRepositoryPort;
import com.poly.restaurant.dataaccess.entity.OrderJpaEntity;
import com.poly.restaurant.dataaccess.jpa.JpaMenuItemRepository;
import com.poly.restaurant.dataaccess.jpa.JpaOrderRepository;
import com.poly.restaurant.dataaccess.mapper.OrderEntityMapper;
import com.poly.restaurant.domain.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final JpaOrderRepository jpaOrderRepository;
    private final JpaMenuItemRepository jpaMenuItemRepository;

    @Override
    @Transactional
    public Order save(Order order) {
        OrderJpaEntity entity = OrderEntityMapper.toEntity(order, jpaMenuItemRepository);
        OrderJpaEntity savedEntity = jpaOrderRepository.save(entity);
        return OrderEntityMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(String id) {
        return jpaOrderRepository.findByIdWithItems(id)
                .map(OrderEntityMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return jpaOrderRepository.findAllWithItems().stream()
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

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByCustomerId(String customerId) {
        return jpaOrderRepository.findByCustomerIdWithItems(customerId).stream()
                .map(OrderEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByTableId(String tableId) {
        return jpaOrderRepository.findByTableIdWithItems(tableId).stream()
                .map(OrderEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByStatus(com.poly.restaurant.domain.entity.OrderStatus status) {
        return jpaOrderRepository.findByStatusWithItems(status).stream()
                .map(OrderEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
