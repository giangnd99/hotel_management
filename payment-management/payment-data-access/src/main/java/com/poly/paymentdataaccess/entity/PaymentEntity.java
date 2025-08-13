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

    @Column(name = "reference_id", columnDefinition = "BINARY(16)")
    private UUID referenceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatusEntity status; // Trạng thái thanh toán

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "method")
    private PaymentMethodEntity method; // CASH, PAYOS

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "order_code")
    private long orderCode;

    @Column(name = "link")
    private String paymentLink;

    @Column(name = "description")
    private String description; // Loại dịch vụ chi trả

}
