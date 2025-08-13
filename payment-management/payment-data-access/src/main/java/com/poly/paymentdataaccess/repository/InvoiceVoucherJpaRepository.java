package com.poly.paymentdataaccess.repository;

import com.poly.paymentdataaccess.entity.InvoiceVoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceVoucherJpaRepository extends JpaRepository<InvoiceVoucherEntity, UUID> {
}
