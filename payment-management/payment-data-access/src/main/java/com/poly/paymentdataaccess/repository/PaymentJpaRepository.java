package com.poly.paymentdataaccess.repository;

import com.poly.paymentdataaccess.entity.PaymentEntity;
import com.poly.paymentdataaccess.share.PaymentTransactionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, UUID> {
    Optional<PaymentEntity> findByBookingIdAndPaymentTransactionTypeEntity(UUID bookingId, PaymentTransactionTypeEntity paymentTransactionType);
    Optional<PaymentEntity> findByReferenceCode(String referenceCode);

    @Query(value = "select * from payment p where p.payment_status = 'PENDING' AND p.payment_transaction_type = 'DEPOSIT'", nativeQuery = true)
    List<PaymentEntity> findExpiredDepositPayments();


}
