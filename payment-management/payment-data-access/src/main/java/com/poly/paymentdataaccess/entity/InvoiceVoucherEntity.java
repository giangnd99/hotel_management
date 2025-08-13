package com.poly.paymentdataaccess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "invoice_voucher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceVoucherEntity {

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "invoice_id", columnDefinition = "BINARY(16)")
    private UUID invoiceId;

    @Column(name = "voucher_id", columnDefinition = "BINARY(16)")
    private UUID voucherId;

    @Column(name = "amount")
    private BigDecimal amount;
}
