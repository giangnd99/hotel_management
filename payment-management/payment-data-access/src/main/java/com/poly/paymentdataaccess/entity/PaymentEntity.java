package com.poly.paymentdataaccess.entity;

import com.poly.paymentdataaccess.share.PaymentMethodEntity;
import com.poly.paymentdataaccess.share.PaymentStatusEntity;
import com.poly.paymentdataaccess.share.PaymentTransactionTypeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity {

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "booking_id")
    private UUID bookingId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatusEntity paymentStatusEntity; // Trạng thái thanh toán

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethodEntity paymentMethodEntity; // CASH, PAYOS

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "reference_code")
    private long referenceCode;

    @Column(name = "payment_link")
    private String paymentLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_transaction_type")
    private PaymentTransactionTypeEntity paymentTransactionTypeEntity; // Loại dịch vụ chi trả

}
