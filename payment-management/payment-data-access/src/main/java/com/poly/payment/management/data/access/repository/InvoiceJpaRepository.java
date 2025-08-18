package com.poly.payment.management.data.access.repository;

import com.poly.payment.management.data.access.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InvoiceJpaRepository extends JpaRepository<InvoiceEntity, UUID> {
    List<InvoiceEntity> findAllByCustomerId(UUID customerId);
}
