package com.poly.paymentdataaccess.repository;

import com.poly.domain.valueobject.PaymentStatus;
import com.poly.paymentdataaccess.entity.PaymentEntity;
import com.poly.paymentdataaccess.share.PaymentMethodEntity;
import com.poly.paymentdataaccess.share.PaymentStatusEntity;
import com.poly.paymentdataaccess.share.PaymentTransactionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, UUID> {
    Optional<PaymentEntity> findByReferenceId(UUID referenceId);
    Optional<PaymentEntity> findByOrderCode(long orderCode);
    List<PaymentEntity> findAllByStatusAndCreatedAtBefore(PaymentStatusEntity status, LocalDateTime beforeTime);
}
