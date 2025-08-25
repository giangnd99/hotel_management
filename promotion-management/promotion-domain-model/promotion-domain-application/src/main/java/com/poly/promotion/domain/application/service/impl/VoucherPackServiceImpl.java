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

    private final VoucherPackRepository voucherPackRepository;

    public VoucherPackServiceImpl(VoucherPackRepository voucherPackRepository) {
        this.voucherPackRepository = voucherPackRepository;
    }

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
        
        // Set audit fields
        voucherPack.setCreatedBy(createdBy);
        voucherPack.setCreatedAt(LocalDateTime.now());
        
        // Calculate status based on validity dates
        VoucherPackStatus calculatedStatus = voucherPack.calculateInitialStatus();
        voucherPack.setStatus(calculatedStatus);
        
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
    public int markClosedVoucherPacks() {
        // Get all voucher packs eligible for closure due to zero quantity
        List<VoucherPack> eligiblePacks = voucherPackRepository.getVoucherPacksEligibleForClosure();
        
        int closedCount = 0;
        for (VoucherPack pack : eligiblePacks) {
            try {
                // Update status to CLOSED
                voucherPackRepository.updateVoucherPackStatus(pack.getId().getValue(), VoucherPackStatus.CLOSED);
                closedCount++;
            } catch (Exception e) {
                // Log the error but continue with other packs
                // In a production environment, you might want to use a proper logging framework
                System.err.println("Failed to close voucher pack " + pack.getId().getValue() + ": " + e.getMessage());
            }
        }
        
        return closedCount;
    }

    @Override
    public List<VoucherPack> getVoucherPacksEligibleForClosure() {
        return voucherPackRepository.getVoucherPacksEligibleForClosure();
    }

    @Override
    public void updateVoucherPackStatus(Long voucherPackId, VoucherPackStatus newStatus) {
        // Validate that the voucher pack exists
        if (!voucherPackRepository.existsById(voucherPackId)) {
            throw new VoucherDomainException("Voucher pack with ID " + voucherPackId + " does not exist.");
        }
        
        // Validate the status transition
        VoucherPack existingPack = voucherPackRepository.getVoucherPackById(voucherPackId);
        if (!existingPack.getStatus().canTransitionTo(newStatus)) {
            throw new VoucherDomainException("Cannot transition voucher pack from " + existingPack.getStatus() + " to " + newStatus);
        }
        
        // Update the status
        voucherPackRepository.updateVoucherPackStatus(voucherPackId, newStatus);
    }

    @Override
    public VoucherPack publishVoucherPack(Long voucherPackId) {
        // Validate that the voucher pack exists
        if (!voucherPackRepository.existsById(voucherPackId)) {
            throw new VoucherDomainException("Voucher pack with ID " + voucherPackId + " does not exist.");
        }
        
        // Get the existing voucher pack
        VoucherPack existingPack = voucherPackRepository.getVoucherPackById(voucherPackId);
        
        // Validate that the pack is in PENDING status
        if (existingPack.getStatus() != VoucherPackStatus.PENDING) {
            throw new VoucherDomainException("Cannot publish voucher pack with status " + existingPack.getStatus() + ". Only PENDING packs can be published.");
        }
        
        // Update the status to PUBLISHED
        voucherPackRepository.updateVoucherPackStatus(voucherPackId, VoucherPackStatus.PUBLISHED);
        
        // Return the updated pack
        return voucherPackRepository.getVoucherPackById(voucherPackId);
    }
}
