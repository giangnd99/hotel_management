package com.poly.promotion.domain.application.service.impl;

import com.poly.promotion.domain.application.service.VoucherPackService;
import com.poly.promotion.domain.application.spi.repository.VoucherPackRepository;
import com.poly.promotion.domain.core.entity.VoucherPack;
import com.poly.promotion.domain.core.exception.VoucherDomainException;
import com.poly.promotion.domain.core.valueobject.VoucherPackStatus;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class VoucherPackServiceImpl implements VoucherPackService {

    VoucherPackRepository voucherPackRepository;

    @Override
    public VoucherPack getVoucherPackById(Long voucherPackId) {
        if(!voucherPackRepository.existsById(voucherPackId)){
            throw new VoucherDomainException("Voucher pack with ID " + voucherPackId + " does not exist.");
        }
        return voucherPackRepository.getVoucherPackById(voucherPackId);
    }

    @Override
    public List<VoucherPack> getAllVoucherPacksWithStatus(VoucherPackStatus... status) {
        return voucherPackRepository.getAllVoucherPacksWithStatus(status);
    }

    @Override
    public VoucherPack createVoucherPack(@NonNull VoucherPack voucherPack, String createdBy) {
        if (!Objects.isNull(voucherPack.getId())) {
            throw new VoucherDomainException("Creating a new voucher pack should not have an ID.");
        }
        if (createdBy == null || createdBy.isEmpty()) {
            throw new VoucherDomainException("Created by field cannot be null or empty.");
        }
        voucherPack.setCreatedBy(createdBy);
        voucherPack.setCreatedAt(LocalDateTime.now());
        voucherPack.setStatus(VoucherPackStatus.PENDING);
        return voucherPackRepository.createVoucherPack(voucherPack);
    }

    @Override
    public VoucherPack updatePendingVoucherPack(Long voucherPackId, VoucherPack updatingVoucherPack, String updatedBy) {
        if(!voucherPackRepository.existsById(voucherPackId)){
            throw new VoucherDomainException("Voucher pack with ID " + voucherPackId + " does not exist.");
        }
        if(!voucherPackRepository.isOfStatus(voucherPackId, VoucherPackStatus.PENDING)){
            throw new VoucherDomainException("Voucher pack with ID " + voucherPackId + " is not in PENDING status.");
        }
        VoucherPack updatedVoucherPack = voucherPackRepository.getVoucherPackById(voucherPackId);
        if(updatingVoucherPack.getDescription() != null) updatedVoucherPack.setDescription(updatingVoucherPack.getDescription());
        if(updatingVoucherPack.getQuantity() != null) updatedVoucherPack.setQuantity(updatingVoucherPack.getQuantity());
        if(updatingVoucherPack.getDiscountAmount() != null) updatedVoucherPack.setDiscountAmount(updatingVoucherPack.getDiscountAmount());
        if(updatingVoucherPack.getRequiredPoints() != null) updatedVoucherPack.setRequiredPoints(updatingVoucherPack.getRequiredPoints());
        if(updatingVoucherPack.getVoucherValidRange() != null) updatedVoucherPack.setVoucherValidRange(updatingVoucherPack.getVoucherValidRange());
        if(updatingVoucherPack.getPackValidFrom() != null) updatedVoucherPack.setPackValidFrom(updatingVoucherPack.getPackValidFrom());
        if(updatingVoucherPack.getPackValidTo() != null) updatedVoucherPack.setPackValidTo(updatingVoucherPack.getPackValidTo());
        updatedVoucherPack.setUpdatedAt(LocalDateTime.now());
        updatedVoucherPack.setUpdatedBy(updatedBy);

        return voucherPackRepository.updatePendingVoucherPack(updatedVoucherPack);
    }

    @Override
    public void closeVoucherPack(Long voucherPackId) {
        if(!voucherPackRepository.existsById(voucherPackId)){
            throw new VoucherDomainException("Voucher pack with ID " + voucherPackId + " does not exist.");
        }
        if(voucherPackRepository.isOfStatus(voucherPackId, VoucherPackStatus.CLOSED) ||
        voucherPackRepository.isOfStatus(voucherPackId, VoucherPackStatus.EXPIRED)){
            throw new VoucherDomainException("Voucher pack with ID " + voucherPackId + " is already closed or expired.");
        }
        voucherPackRepository.closeVoucherPack(voucherPackId);
    }

    @Override
    public void reduceVoucherPackStockAfterRedeem(Long voucherPackId, Integer quantity) {
        if(!voucherPackRepository.existsById(voucherPackId)){
            throw new VoucherDomainException("Voucher pack with ID " + voucherPackId + " does not exist.");
        }
        if(!voucherPackRepository.isOfStatus(voucherPackId, VoucherPackStatus.PUBLISHED)){
            throw new VoucherDomainException("Voucher pack with ID " + voucherPackId + " is not yet published.");
        }
        long currentQuantity = voucherPackRepository.getVoucherPackQuantity(voucherPackId);
        if (currentQuantity < quantity) {
            throw new VoucherDomainException("Insufficient stock for voucher pack with ID " + voucherPackId + ". Current stock: " + currentQuantity);
        }
        voucherPackRepository.reduceVoucherPackStockAfterRedeem(voucherPackId, quantity);
        currentQuantity = voucherPackRepository.getVoucherPackQuantity(voucherPackId);
        if (currentQuantity <= 0) {
            voucherPackRepository.closeVoucherPack(voucherPackId);
        }
    }

    @Override
    public int markExpiredVoucherPacks() {
        // Get all voucher packs eligible for expiration
        List<VoucherPack> eligiblePacks = voucherPackRepository.getVoucherPacksEligibleForExpiration();
        
        int expiredCount = 0;
        for (VoucherPack pack : eligiblePacks) {
            try {
                // Update status to EXPIRED
                voucherPackRepository.updateVoucherPackStatus(pack.getId().getValue(), VoucherPackStatus.EXPIRED);
                expiredCount++;
            } catch (Exception e) {
                // Log the error but continue with other packs
                // In a production environment, you might want to use a proper logging framework
                System.err.println("Failed to expire voucher pack " + pack.getId().getValue() + ": " + e.getMessage());
            }
        }
        
        return expiredCount;
    }

    @Override
    public List<VoucherPack> getVoucherPacksEligibleForExpiration() {
        return voucherPackRepository.getVoucherPacksEligibleForExpiration();
    }

    @Override
    public void updateVoucherPackStatus(Long voucherPackId, VoucherPackStatus newStatus) {
        // Validate that the voucher pack exists
        if (!voucherPackRepository.existsById(voucherPackId)) {
            throw new VoucherDomainException("Voucher pack with ID " + voucherPackId + " does not exist.");
        }
        
        // Validate status transition (basic validation)
        VoucherPack pack = voucherPackRepository.getVoucherPackById(voucherPackId);
        if (!isValidStatusTransition(pack.getStatus(), newStatus)) {
            throw new VoucherDomainException("Invalid status transition from " + pack.getStatus() + " to " + newStatus);
        }
        
        voucherPackRepository.updateVoucherPackStatus(voucherPackId, newStatus);
    }

    /**
     * Validates if a status transition is allowed.
     * This is a basic validation - you might want to add more sophisticated rules.
     */
    private boolean isValidStatusTransition(VoucherPackStatus currentStatus, VoucherPackStatus newStatus) {
        // Allow transition to EXPIRED from any status (for system operations)
        if (newStatus == VoucherPackStatus.EXPIRED) {
            return true;
        }
        
        // Allow transition to CLOSED from PENDING or PUBLISHED
        if (newStatus == VoucherPackStatus.CLOSED) {
            return currentStatus == VoucherPackStatus.PENDING || currentStatus == VoucherPackStatus.PUBLISHED;
        }
        
        // Allow transition to PUBLISHED from PENDING
        if (newStatus == VoucherPackStatus.PUBLISHED) {
            return currentStatus == VoucherPackStatus.PENDING;
        }
        
        return false;
    }
}
