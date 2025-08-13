package com.poly.paymentdataaccess.repository;

import com.poly.paymentdataaccess.entity.InvoicePaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InvoicePaymentJpaRepository extends JpaRepository<InvoicePaymentEntity, UUID> {
    Optional<InvoicePaymentEntity> findByInvoiceId(UUID id);
}
