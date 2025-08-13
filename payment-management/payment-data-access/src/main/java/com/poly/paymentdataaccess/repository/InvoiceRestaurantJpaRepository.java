package com.poly.paymentdataaccess.repository;

import com.poly.paymentdataaccess.entity.InvoiceRestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InvoiceRestaurantJpaRepository extends JpaRepository<InvoiceRestaurantEntity, UUID> {
    List<InvoiceRestaurantEntity> findAllByInvoiceBookingId(UUID bookingId);
}
