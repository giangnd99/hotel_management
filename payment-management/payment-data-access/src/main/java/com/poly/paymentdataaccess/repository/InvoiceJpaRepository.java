package com.poly.paymentdataaccess.repository;

import com.poly.paymentdataaccess.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceJpaRepository extends JpaRepository<InvoiceEntity, UUID> {
    List<InvoiceEntity> findAllByCustomerId(UUID customerId);

    Optional<InvoiceEntity> findByBookingId(UUID bookingId);
}
