package com.poly.paymentdataaccess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "invoice_service")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceServiceEntity {
    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "service_id", columnDefinition = "BINARY(16")
    private UUID serviceId;

    @Column(name = "invoice_booking_id", columnDefinition = "BINARY(16)")
    private UUID invoiceBookingId;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;
}
