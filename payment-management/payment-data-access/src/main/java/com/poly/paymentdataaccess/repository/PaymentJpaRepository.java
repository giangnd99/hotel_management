package com.poly.paymentdataaccess.repository;

import com.poly.paymentdataaccess.entity.PaymentEntity;
import com.poly.paymentdataaccess.share.PaymentTransactionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, UUID> {
//    @Query(value = "SELECT * FROM PaymentEntity p WHERE p.booking_id = UUID_TO_BIN(:bookingId) AND p.payment_transaction_type = :paymentTransactionType", nativeQuery = true)
//    Optional<PaymentEntity> findByBookingIdAndPaymentTransactionType(@Param("bookingId") UUID bookingId, @Param("paymentTransactionType") PaymentTransactionTypeEntity paymentTransactionType);

    Optional<PaymentEntity> findByBookingIdAndPaymentTransactionTypeEntity(UUID bookingId,PaymentTransactionTypeEntity paymentTransactionType);


    Optional<PaymentEntity> findByReferenceCode(String referenceCode);

    @Query(value = "select * from payment p where p.payment_status = 'PENDING' ", nativeQuery = true)
    List<PaymentEntity> findExpiredDepositPayments();


}
