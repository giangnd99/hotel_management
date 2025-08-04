package com.poly.promotion.application.controller;


import com.poly.promotion.domain.application.dto.response.external.VoucherPackExternalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/voucher-packs")
public class VoucherPackController {

    @GetMapping
    public ResponseEntity<List<VoucherPackExternalResponse>> getVoucherPacks() {
        return null;
    }

    @GetMapping("/{voucherPackId}")
    public ResponseEntity<VoucherPackExternalResponse> getVoucherPackById(@PathVariable Long voucherPackId) {
        return null;
    }

    @PostMapping
    public ResponseEntity<VoucherPackExternalResponse> createVoucherPack(VoucherPackExternalResponse voucherPack) {
        return ResponseEntity.status(HttpStatus.CREATED).body(voucherPack);
    }

    @PutMapping("/{voucherPackId}")
    public ResponseEntity<VoucherPackExternalResponse> updateVoucherPack(@PathVariable Long voucherPackId, VoucherPackExternalResponse voucherPack) {
        return null;
    }

    @DeleteMapping("/{voucherPackId}")
    public ResponseEntity<Void> deleteVoucherPack(@PathVariable Integer voucherPackId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
