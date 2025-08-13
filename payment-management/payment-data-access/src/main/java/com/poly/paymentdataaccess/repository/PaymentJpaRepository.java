package com.poly.paymentdataaccess.repository;

import com.poly.paymentdataaccess.entity.PaymentEntity;
import com.poly.paymentdataaccess.share.PaymentMethodEntity;
import com.poly.paymentdataaccess.share.PaymentTransactionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, UUID> {

    Optional<PaymentEntity> findByBookingIdAndPaymentTransactionTypeEntity(UUID bookingId, PaymentTransactionTypeEntity paymentTransactionTypeEntity);

    @Query(value = "select * from payment p where p.payment_status = 'PENDING' ", nativeQuery = true)
    List<PaymentEntity> findExpiredDepositPayments();

    Optional<PaymentEntity> findByBookingId (UUID bookingId);

    PaymentEntity findByReferenceCode (long referenceCode);

    List<PaymentMethodEntity> findAllById(UUID id);
}
