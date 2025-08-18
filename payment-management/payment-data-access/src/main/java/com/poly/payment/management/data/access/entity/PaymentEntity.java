package com.poly.payment.management.data.access.entity;

import com.poly.payment.management.domain.value_object.PaymentMethod;
import com.poly.payment.management.domain.value_object.PaymentStatus;
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
    private PaymentStatus status; // Trạng thái thanh toán

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "method")
    private PaymentMethod method; // CASH, PAYOS

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
