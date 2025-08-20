package com.poly.payment.management.data.access.repository;

import com.poly.payment.management.data.access.entity.PaymentEntity;
import com.poly.payment.management.domain.value_object.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, UUID> {

    Optional<PaymentEntity> findByReferenceId(UUID referenceId);

    Optional<PaymentEntity> findByOrderCode(long orderCode);

    List<PaymentEntity> findAllByStatusAndCreatedAtBefore(PaymentStatus status, LocalDateTime beforeTime);
}
