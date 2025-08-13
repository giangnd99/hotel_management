package com.poly.paymentdataaccess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "invoice_booking")
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceBookingEntity {

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "invoice_id", columnDefinition = "BINARY(16)")
    private UUID invoiceId;

    @Column(name = "booking_id", columnDefinition = "BINARY(16)")
    private UUID bookingId;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;
}
