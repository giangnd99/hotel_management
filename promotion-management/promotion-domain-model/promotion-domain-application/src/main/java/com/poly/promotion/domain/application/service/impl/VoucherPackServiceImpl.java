package com.poly.promotion.domain.application.service.impl;

import com.poly.promotion.domain.application.service.VoucherPackService;
import com.poly.promotion.domain.application.spi.repository.VoucherPackRepository;
import com.poly.promotion.domain.core.entity.VoucherPack;
import com.poly.promotion.domain.core.valueobject.VoucherPackStatus;
import lombok.NonNull;

import java.util.List;
import java.util.Objects;

public class VoucherPackServiceImpl implements VoucherPackService {

    VoucherPackRepository voucherPackRepository;

    @Override
    public VoucherPack getVoucherPackById(Long voucherPackId) {
        if(!voucherPackRepository.existsById(voucherPackId)){
            throw new IllegalArgumentException("Voucher pack with ID " + voucherPackId + " does not exist.");
        }
        return voucherPackRepository.getVoucherPackById(voucherPackId);
    }

    @Override
    public List<VoucherPack> getAllVoucherPacksWithStatus(@NonNull Integer status) {
        if(Objects.isNull(VoucherPackStatus.fromStatusCode(status))){
            throw new IllegalArgumentException("Invalid voucher pack status code");
        }
        return voucherPackRepository.getAllVoucherPacksWithStatus(status);
    }

    @Override
    public VoucherPack createVoucherPack(@NonNull VoucherPack voucherPack) {
        if (!Objects.isNull(voucherPack.getId())) {
            throw new IllegalArgumentException("Creating a new voucher pack should not have an ID.");
        }
        return voucherPackRepository.createVoucherPack(voucherPack);
    }

    @Override
    public VoucherPack updatePendingVoucherPack(Long voucherPackId, VoucherPack voucherPack) {
        if(!voucherPackRepository.existsById(voucherPackId)){
            throw new IllegalArgumentException("Voucher pack with ID " + voucherPackId + " does not exist.");
        }
        if(!voucherPackRepository.isOfStatus(voucherPackId, VoucherPackStatus.PENDING.getStatusCode())){
            throw new IllegalArgumentException("Voucher pack with ID " + voucherPackId + " is not in PENDING status.");
        }
        return voucherPackRepository.updatePendingVoucherPack(voucherPackId, voucherPack);
    }

    @Override
    public void closeVoucherPack(Long voucherPackId) {
        if(!voucherPackRepository.existsById(voucherPackId)){
            throw new IllegalArgumentException("Voucher pack with ID " + voucherPackId + " does not exist.");
        }
        if(voucherPackRepository.isOfStatus(voucherPackId, VoucherPackStatus.CLOSED.getStatusCode()) ||
        voucherPackRepository.isOfStatus(voucherPackId, VoucherPackStatus.EXPIRED.getStatusCode())){
            throw new IllegalArgumentException("Voucher pack with ID " + voucherPackId + " is already closed or expired.");
        }
        voucherPackRepository.closeVoucherPack(voucherPackId);
    }

    @Override
    public void reduceVoucherPackStockAfterRedeem(Long voucherPackId, Integer quantity) {
        if(!voucherPackRepository.existsById(voucherPackId)){
            throw new IllegalArgumentException("Voucher pack with ID " + voucherPackId + " does not exist.");
        }
        if(!voucherPackRepository.isOfStatus(voucherPackId, VoucherPackStatus.PUBLISHED.getStatusCode())){
            throw new IllegalArgumentException("Voucher pack with ID " + voucherPackId + " is not in PUBLISHED status.");
        }
        long currentQuantity = voucherPackRepository.getVoucherPackQuantity(voucherPackId);
        if (currentQuantity < quantity) {
            throw new IllegalArgumentException("Insufficient stock for voucher pack with ID " + voucherPackId + ". Current stock: " + currentQuantity);
        }
        voucherPackRepository.reduceVoucherPackStockAfterRedeem(voucherPackId, quantity);
    }
}
