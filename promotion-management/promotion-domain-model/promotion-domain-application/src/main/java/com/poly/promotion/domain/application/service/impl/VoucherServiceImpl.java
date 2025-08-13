package com.poly.promotion.domain.application.service.impl;

import com.poly.promotion.domain.application.service.VoucherPackService;
import com.poly.promotion.domain.application.service.VoucherService;
import com.poly.promotion.domain.application.spi.repository.VoucherRepository;
import com.poly.promotion.domain.core.entity.Voucher;
import com.poly.promotion.domain.core.entity.VoucherPack;
import com.poly.promotion.domain.core.exception.PromotionDomainException;
import com.poly.promotion.domain.core.valueobject.VoucherStatus;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherServiceImpl implements VoucherService {

    VoucherPackService voucherPackService;
    VoucherRepository voucherRepository;

    public VoucherServiceImpl(VoucherPackService voucherPackService, VoucherRepository voucherRepository) {
        this.voucherPackService = voucherPackService;
        this.voucherRepository = voucherRepository;
    }

    @Override
    public List<Voucher> getAllVouchersWithStatus(String customerId, VoucherStatus... voucherStatus) {
        if (customerId == null || customerId.isEmpty()) {
            throw new PromotionDomainException("Customer ID cannot be null or empty");
        }
        
        try {
            // Validate customer ID format
            UUID.fromString(customerId);
        } catch (IllegalArgumentException e) {
            throw new PromotionDomainException("Invalid customer ID format: " + customerId);
        }
        
        return voucherRepository.getAllVouchersWithStatus(customerId, voucherStatus);
    }

    @Override
    public Voucher getVoucherById(String voucherId) {
        if (voucherId == null || voucherId.isEmpty()) {
            throw new PromotionDomainException("Voucher ID cannot be null or empty");
        }
        
        try {
            // Validate voucher ID format
            UUID.fromString(voucherId);
        } catch (IllegalArgumentException e) {
            throw new PromotionDomainException("Invalid voucher ID format: " + voucherId);
        }
        
        Voucher voucher = voucherRepository.getVoucherById(voucherId);
        if (voucher == null) {
            throw new PromotionDomainException("Voucher with ID " + voucherId + " not found");
        }
        
        return voucher;
    }

    @Override
    public Voucher redeemVoucherFromPack(Long voucherPackId, String customerId, Integer quantity) {
        // Validate input parameters
        if (voucherPackId == null) {
            throw new PromotionDomainException("Voucher pack ID cannot be null");
        }
        if (customerId == null || customerId.isEmpty()) {
            throw new PromotionDomainException("Customer ID cannot be null or empty");
        }
        if (quantity == null || quantity <= 0) {
            throw new PromotionDomainException("Quantity must be positive");
        }
        
        try {
            // Validate customer ID format
            UUID.fromString(customerId);
        } catch (IllegalArgumentException e) {
            throw new PromotionDomainException("Invalid customer ID format: " + customerId);
        }
        
        // Get the voucher pack
        VoucherPack voucherPack = voucherPackService.getVoucherPackById(voucherPackId);
        
        // Validate that the pack can provide the requested quantity
        if (!voucherPack.canRedeem(quantity)) {
            throw new PromotionDomainException("Cannot redeem " + quantity + " vouchers from pack " + voucherPackId);
        }
        
        // Create vouchers for the requested quantity
        Voucher redeemedVoucher = null;
        for (int i = 0; i < quantity; i++) {
            Voucher voucher = Voucher.initRedeem(
                customerId, 
                voucherPackId, 
                voucherPack.getDiscountAmount(), 
                voucherPack.getVoucherValidRange()
            );
            
            // Ensure unique voucher code
            String voucherCode;
            do {
                voucherCode = generateUniqueVoucherCode();
            } while (voucherRepository.isVoucherCodeExists(voucherCode));
            
            voucher.setVoucherCode(voucherCode);
            
            // Save the voucher
            Voucher savedVoucher = voucherRepository.createVoucher(voucher);
            
            // Set the first voucher as the return value (for single voucher redemption)
            if (i == 0) {
                redeemedVoucher = savedVoucher;
            }
        }
        
        // Reduce the voucher pack stock
        voucherPackService.reduceVoucherPackStockAfterRedeem(voucherPackId, quantity);
        
        return redeemedVoucher;
    }

    @Override
    public Voucher applyVoucher(String voucherCode, String customerId) {
        // Validate input parameters
        if (voucherCode == null || voucherCode.isEmpty()) {
            throw new PromotionDomainException("Voucher code cannot be null or empty");
        }
        if (customerId == null || customerId.isEmpty()) {
            throw new PromotionDomainException("Customer ID cannot be null or empty");
        }
        
        try {
            // Validate customer ID format
            UUID.fromString(customerId);
        } catch (IllegalArgumentException e) {
            throw new PromotionDomainException("Invalid customer ID format: " + customerId);
        }
        
        // Find the voucher by code
        Voucher voucher = voucherRepository.getVoucherByCode(voucherCode);
        if (voucher == null) {
            throw new PromotionDomainException("Voucher with code " + voucherCode + " not found");
        }
        
        // Validate that the voucher belongs to the customer
        if (!Objects.equals(voucher.getCustomerId().getValue().toString(), customerId)) {
            throw new PromotionDomainException("Voucher " + voucherCode + " does not belong to customer " + customerId);
        }
        
        // Check if voucher can be used
        if (!voucher.canUse()) {
            throw new PromotionDomainException("Voucher " + voucherCode + " cannot be used. Status: " + voucher.getVoucherStatus());
        }
        
        // Mark voucher as used
        voucher.use();
        
        // Update voucher in repository
        return voucherRepository.updateVoucher(voucher);
    }
    
    /**
     * Generates a unique voucher code.
     * This is a simple implementation - in production, you might want to use a more sophisticated approach.
     */
    private String generateUniqueVoucherCode() {
        return "VOUCHER-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public int expireExpiredVouchers() {
        // Get all vouchers eligible for expiration
        List<Voucher> eligibleVouchers = voucherRepository.getVouchersEligibleForExpiration();
        
        int expiredCount = 0;
        for (Voucher voucher : eligibleVouchers) {
            try {
                // Update status to EXPIRED
                voucherRepository.updateVoucherStatus(voucher.getId().getValue().toString(), VoucherStatus.EXPIRED);
                expiredCount++;
            } catch (Exception e) {
                // Log the error but continue with other vouchers
                // In a production environment, you might want to use a proper logging framework
                System.err.println("Failed to expire voucher " + voucher.getId().getValue() + ": " + e.getMessage());
            }
        }
        
        return expiredCount;
    }

    @Override
    public List<Voucher> getVouchersEligibleForExpiration() {
        return voucherRepository.getVouchersEligibleForExpiration();
    }

    @Override
    public void updateVoucherStatus(String voucherId, VoucherStatus newStatus) {
        // Validate voucher ID format
        try {
            UUID.fromString(voucherId);
        } catch (IllegalArgumentException e) {
            throw new PromotionDomainException("Invalid voucher ID format: " + voucherId);
        }
        
        // Validate that the voucher exists
        Voucher voucher = voucherRepository.getVoucherById(voucherId);
        if (voucher == null) {
            throw new PromotionDomainException("Voucher with ID " + voucherId + " not found");
        }
        
        // Validate status transition (basic validation)
        if (!isValidStatusTransition(voucher.getVoucherStatus(), newStatus)) {
            throw new PromotionDomainException("Invalid status transition from " + voucher.getVoucherStatus() + " to " + newStatus);
        }
        
        voucherRepository.updateVoucherStatus(voucherId, newStatus);
    }

    /**
     * Checks if a voucher is eligible for expiration.
     * A voucher is eligible if it's in REDEEMED status
     * and has passed its validTo date.
     */
    private boolean isVoucherEligibleForExpiration(Voucher voucher) {
        if (voucher.getVoucherStatus() != VoucherStatus.REDEEMED) {
            return false;
        }
        
        if (voucher.getValidTo() == null) {
            return false;
        }
        
        return voucher.getValidTo().isBefore(java.time.LocalDateTime.now());
    }

    /**
     * Validates if a status transition is allowed.
     * This is a basic validation - you might want to add more sophisticated rules.
     */
    private boolean isValidStatusTransition(VoucherStatus currentStatus, VoucherStatus newStatus) {
        // Allow transition to EXPIRED from any status (for system operations)
        if (newStatus == VoucherStatus.EXPIRED) {
            return true;
        }
        
        // Allow transition to USED from REDEEMED
        if (newStatus == VoucherStatus.USED) {
            return currentStatus == VoucherStatus.REDEEMED;
        }
        
        // Allow transition to REDEEMED from PENDING
        if (newStatus == VoucherStatus.REDEEMED) {
            return currentStatus == VoucherStatus.PENDING;
        }
        
        return false;
    }
}
