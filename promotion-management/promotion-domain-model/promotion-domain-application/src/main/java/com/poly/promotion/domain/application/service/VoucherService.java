package com.poly.promotion.domain.application.service;

import com.poly.promotion.domain.core.entity.Voucher;
import com.poly.promotion.domain.core.valueobject.VoucherStatus;

import java.util.List;

/**
 * <h2>VoucherService Interface</h2>
 * 
 * <p>Service interface for managing individual vouchers in the promotion system.
 * This service provides business operations for voucher redemption, usage, and
 * lifecycle management, including automatic expiration handling.</p>
 * 
 * <p><strong>Service Responsibilities:</strong></p>
 * <ul>
 *   <li>Voucher redemption from voucher packs</li>
 *   <li>Voucher application to transactions</li>
 *   <li>Voucher status management and validation</li>
 *   <li>Automatic expiration processing</li>
 *   <li>Customer voucher retrieval and filtering</li>
 * </ul>
 * 
 * <p><strong>Business Rules Enforced:</strong></p>
 * <ul>
 *   <li>Only REDEEMED vouchers can be used in transactions</li>
 *   <li>Vouchers can only be used by their rightful owner</li>
 *   <li>Expired vouchers are automatically marked by the system</li>
 *   <li>Used vouchers cannot be used again</li>
 * </ul>
 * 
 * <p><strong>Usage Context:</strong></p>
 * <p>This service is typically used by:</p>
 * <ul>
 *   <li>Customer-facing applications for voucher redemption</li>
 *   <li>Point-of-sale systems for voucher application</li>
 *   <li>Customer account management interfaces</li>
 *   <li>System maintenance and expiration processes</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see Voucher
 * @see VoucherStatus
 */
public interface VoucherService {
    
    /**
     * Retrieves all vouchers for a specific customer, optionally filtered by status.
     * 
     * <p>This method allows customers to view their voucher collection and supports
     * filtering by status to show only active, used, or expired vouchers.</p>
     * 
     * @param customerId the unique identifier of the customer
     * @param voucherStatus optional status values to filter by (if none provided, returns all vouchers)
     * @return a list of vouchers belonging to the customer, optionally filtered by status
     * @throws com.poly.promotion.domain.core.exception.PromotionDomainException if customerId is invalid
     */
    List<Voucher> getAllVouchersWithStatus(String customerId, VoucherStatus... voucherStatus);
    
    /**
     * Retrieves a specific voucher by its unique identifier.
     * 
     * <p>This method performs validation to ensure the voucher exists before returning it.
     * If the voucher doesn't exist, a domain exception is thrown.</p>
     * 
     * @param voucherId the unique identifier of the voucher to retrieve
     * @return the voucher with the specified ID
     * @throws com.poly.promotion.domain.core.exception.PromotionDomainException if the voucher doesn't exist
     */
    Voucher getVoucherById(String voucherId);
    
    /**
     * Redeems vouchers from a voucher pack for a specific customer.
     * 
     * <p>This method creates new vouchers from an existing voucher pack when a customer
     * exchanges loyalty points. The method validates that the pack can provide the
     * requested quantity and creates unique voucher codes for each voucher.</p>
     * 
     * <p><strong>Process:</strong></p>
     * <ol>
     *   <li>Validates the voucher pack exists and can provide the requested quantity</li>
     *   <li>Creates individual vouchers with unique codes</li>
     *   <li>Reduces the pack's available stock</li>
     *   <li>Returns the first voucher created (for single voucher operations)</li>
     * </ol>
     * 
     * @param voucherPackId the ID of the voucher pack to redeem from
     * @param customerId the unique identifier of the customer redeeming vouchers
     * @param quantity the number of vouchers to redeem (must be positive)
     * @return the first voucher created (representing the redemption)
     * @throws com.poly.promotion.domain.core.exception.PromotionDomainException if the pack cannot provide the requested quantity
     * @throws IllegalArgumentException if any parameters are invalid
     */
    Voucher redeemVoucherFromPack(Long voucherPackId, String customerId, Integer quantity);
    
    /**
     * Applies a voucher to a transaction.
     * 
     * <p>This method validates that a voucher can be used and marks it as used
     * after successful application. The method ensures that only the rightful
     * owner can use the voucher and that it's in a valid state for use.</p>
     * 
     * <p><strong>Validation:</strong></p>
     * <ul>
     *   <li>Voucher exists and belongs to the specified customer</li>
     *   <li>Voucher is in REDEEMED status and within validity period</li>
     *   <li>Voucher has not been used before</li>
     * </ul>
     * 
     * @param voucherCode the unique code of the voucher to apply
     * @param customerId the unique identifier of the customer using the voucher
     * @return the updated voucher with USED status
     * @throws com.poly.promotion.domain.core.exception.PromotionDomainException if the voucher cannot be used
     * @throws IllegalArgumentException if any parameters are invalid
     */
    Voucher applyVoucher(String voucherCode, String customerId);

    /**
     * Marks vouchers as expired that are past their validity date.
     * 
     * <p>This method identifies all vouchers that are currently REDEEMED
     * and have passed their validTo date, then updates their status to EXPIRED.
     * This is typically called by scheduled tasks or system maintenance processes.</p>
     * 
     * @return the number of vouchers that were marked as expired
     */
    int expireExpiredVouchers();

    /**
     * Gets all vouchers that are eligible for expiration.
     * 
     * <p>This method returns vouchers that are currently REDEEMED
     * and have passed their validTo date. These vouchers should be marked as
     * expired by the system.</p>
     * 
     * @return a list of vouchers eligible for expiration
     */
    List<Voucher> getVouchersEligibleForExpiration();

    /**
     * Updates the status of a voucher.
     * 
     * <p>This method should be used with caution and typically only for system operations
     * like expiration. It includes validation to ensure the status transition is valid
     * according to business rules.</p>
     * 
     * @param voucherId the ID of the voucher to update
     * @param newStatus the new status to set
     * @throws com.poly.promotion.domain.core.exception.PromotionDomainException if the voucher doesn't exist or the status transition is invalid
     */
    void updateVoucherStatus(String voucherId, VoucherStatus newStatus);
}
