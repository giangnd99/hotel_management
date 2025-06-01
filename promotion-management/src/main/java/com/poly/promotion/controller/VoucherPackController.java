package com.poly.promotion.controller;

import com.poly.promotion.model.VoucherPackModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/voucher-packs")
public class VoucherPackController {

    @GetMapping
    public ResponseEntity<List<VoucherPackModel>> getVoucherPacks() {
        return ResponseEntity.status(HttpStatus.OK).body(List.of(
                VoucherPackModel.builder()
                        .id(1)
                        .description("Includes 5 vouchers")
                        .discountAmount(20.0)
                        .validRange("30 DAYS")
                        .requiredPoints(2000L)
                        .quantity(30)
                        .validFrom(LocalDate.of(2026,1,1))
                        .validTo(LocalDate.of(2026,1,31))
                        .createdAt(LocalDateTime.now())
                        .createdBy("admin")
                        .build(),
                VoucherPackModel.builder()
                        .id(2)
                        .description("Includes 5 vouchers")
                        .discountAmount(20.0)
                        .validRange("30 DAYS")
                        .requiredPoints(2000L)
                        .quantity(30)
                        .validFrom(LocalDate.of(2026,1,1))
                        .validTo(LocalDate.of(2026,1,31))
                        .createdAt(LocalDateTime.now())
                        .createdBy("admin")
                        .build()
        ));
    }

    @GetMapping("/{voucherPackId}")
    public ResponseEntity<VoucherPackModel> getVoucherPackById(@PathVariable Integer voucherPackId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                VoucherPackModel.builder()
                        .id(voucherPackId)
                        .description("Includes 5 vouchers")
                        .discountAmount(20.0)
                        .validRange("30 DAYS")
                        .requiredPoints(2000L)
                        .quantity(30)
                        .validFrom(LocalDate.of(2026,1,1))
                        .validTo(LocalDate.of(2026,1,31))
                        .createdAt(LocalDateTime.now())
                        .createdBy("admin")
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<VoucherPackModel> createVoucherPack(VoucherPackModel voucherPack) {
        return ResponseEntity.status(HttpStatus.CREATED).body(voucherPack);
    }

    @PutMapping("/{voucherPackId}")
    public ResponseEntity<VoucherPackModel> updateVoucherPack(@PathVariable Integer voucherPackId, VoucherPackModel voucherPack) {
        return ResponseEntity.status(HttpStatus.OK).body(
                VoucherPackModel.builder()
                        .id(voucherPackId)
                        .description(voucherPack.getDescription())
                        .discountAmount(voucherPack.getDiscountAmount())
                        .validRange(voucherPack.getValidRange())
                        .requiredPoints(voucherPack.getRequiredPoints())
                        .quantity(voucherPack.getQuantity())
                        .validFrom(voucherPack.getValidFrom())
                        .validTo(voucherPack.getValidTo())
                        .createdAt(LocalDateTime.now()) // Assuming updatedAt is set to now
                        .createdBy("admin") // Assuming createdBy remains the same
                        .build()
        );
    }

    @DeleteMapping("/{voucherPackId}")
    public ResponseEntity<Void> deleteVoucherPack(@PathVariable Integer voucherPackId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
