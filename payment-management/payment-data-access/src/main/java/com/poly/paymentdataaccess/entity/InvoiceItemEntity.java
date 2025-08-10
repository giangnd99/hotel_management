package com.poly.paymentdataaccess.entity;

import com.poly.paymentdataaccess.share.ServiceTypeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "invoice_item")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemEntity {

    @Id
    @Column(name = "invoice_item_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "invoice_id")
    private UUID invoiceId;

    @Column(name = "service_id")
    private UUID serviceId;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type")
    private ServiceTypeEntity serviceTypeEntity; // Loại dịch vụ dùng

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @Column(name = "note")
    private String note;
}
