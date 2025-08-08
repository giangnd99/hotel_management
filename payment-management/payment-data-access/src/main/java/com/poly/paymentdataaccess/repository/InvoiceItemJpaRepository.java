package com.poly.paymentdataaccess.repository;

import com.poly.paymentdataaccess.entity.InvoiceItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InvoiceItemJpaRepository extends JpaRepository<InvoiceItemEntity, Integer> {
    List<InvoiceItemEntity> findByInvoiceId(UUID InvoiceId);
}
