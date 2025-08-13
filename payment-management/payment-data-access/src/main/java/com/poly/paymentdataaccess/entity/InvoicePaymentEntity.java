package com.poly.paymentdataaccess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "invoice_payment")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoicePaymentEntity {

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "invoice_id", columnDefinition = "BINARY(16)")
    private UUID invoiceId;

    @Column(name = "payment_id", columnDefinition = "BINARY(16)")
    private UUID paymentId;
}
