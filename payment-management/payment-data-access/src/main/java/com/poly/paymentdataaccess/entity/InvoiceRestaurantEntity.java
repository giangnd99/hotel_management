package com.poly.paymentdataaccess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "invoice_restaurant")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRestaurantEntity {

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "restaurant_id", columnDefinition = "BINARY(16")
    private UUID restaurantId;

    @Column(name = "invoice_booking_id", columnDefinition = "BINARY(16)")
    private UUID invoiceBookingId;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;
}
