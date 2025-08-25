package com.poly.promotion.domain.application.service;

import com.poly.promotion.domain.core.entity.VoucherPack;
import com.poly.promotion.domain.core.valueobject.VoucherPackStatus;

import java.util.List;

/**
 * <h2>VoucherPackService Interface</h2>
 * 
 * <p>Service interface for managing voucher packs in the promotion system.
 * This service provides business operations for creating, updating, and managing
 * the lifecycle of voucher packs, which serve as templates for individual vouchers.</p>
 * 
 * <p><strong>Service Responsibilities:</strong></p>
 * <ul>
 *   <li>Voucher pack creation and configuration</li>
 *   <li>Status management and transitions</li>
 *   <li>Stock management and validation</li>
 *   <li>Business rule enforcement</li>
 *   <li>Expiration management</li>
 * </ul>
 * 
 * <p><strong>Business Rules Enforced:</strong></p>
 * <ul>
 *   <li>Only PENDING packs can be modified</li>
 *   <li>Only PUBLISHED packs can have vouchers redeemed</li>
 *   <li>Packs are automatically closed when stock reaches zero</li>
 *   <li>Expired packs cannot be used for new redemptions</li>
 * </ul>
 * 
 * <p><strong>Usage Context:</strong></p>
 * <p>This service is typically used by:</p>
 * <ul>
 *   <li>Administrative interfaces for managing voucher packs</li>
 *   <li>Voucher redemption workflows</li>
 *   <li>System maintenance and expiration processes</li>
 *   <li>Reporting and analytics systems</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherPack
 * @see VoucherPackStatus
 */
public interface VoucherPackService {
    
    /**
     * Retrieves a voucher pack by its unique identifier.
     * 
     * <p>This method performs validation to ensure the voucher pack exists
     * before returning it. If the pack doesn't exist, a domain exception is thrown.</p>
     * 
     * @param voucherPackId the unique identifier of the voucher pack to retrieve
     * @return the voucher pack with the specified ID
     * @throws com.poly.promotion.domain.core.exception.PromotionDomainException if the voucher pack doesn't exist
     */
    VoucherPack getVoucherPackById(Long voucherPackId);
    
    /**
     * Retrieves all voucher packs that match the specified status criteria.
     * 
     * <p>This method supports filtering by one or more statuses, allowing
     * callers to retrieve packs in specific states (e.g., all active packs,
     * all expired packs, etc.).</p>
     * 
     * @param status optional status values to filter by (if none provided, returns all packs)
     * @return a list of voucher packs matching the status criteria
     */
    List<VoucherPack> getAllVoucherPacksWithStatus(VoucherPackStatus... status);
    
    /**
     * Creates a new voucher pack in the system.
     * 
     * <p>This method validates the voucher pack data, sets appropriate default values,
     * and persists the pack to the system. The created pack will have PENDING status
     * and can be modified before publication.</p>
     * 
     * @param voucherPack the voucher pack to create (must not have an ID set)
     * @param createdBy the identifier of the user creating the voucher pack
     * @return the created voucher pack with generated ID and timestamps
     * @throws com.poly.promotion.domain.core.exception.PromotionDomainException if the voucher pack data is invalid
     * @throws IllegalArgumentException if createdBy is null or empty
     */
    VoucherPack createVoucherPack(VoucherPack voucherPack, String createdBy);
    
    /**
     * Updates a voucher pack that is currently in PENDING status.
     * 
     * <p>This method allows modification of voucher pack properties before publication.
     * Only packs in PENDING status can be updated to maintain data integrity.</p>
     * 
     * @param voucherPackId the ID of the voucher pack to update
     * @param voucherPack the updated voucher pack data
     * @param updatedBy the identifier of the user updating the voucher pack
     * @return the updated voucher pack
     * @throws com.poly.promotion.domain.core.exception.PromotionDomainException if the pack doesn't exist or is not in PENDING status
     * @throws IllegalArgumentException if updatedBy is null or empty
     */
    VoucherPack updatePendingVoucherPack(Long voucherPackId, VoucherPack voucherPack, String updatedBy);

    /**
     * Closes a voucher pack by setting its status to CLOSED.
     * 
     * <p>This method is used when administrators want to manually close a voucher pack.
     * Only packs in PENDING or PUBLISHED status can be closed. Packs that are already
     * closed or expired cannot be closed again.</p>
     * 
     * <p><strong>Note:</strong> Expired or out-of-stock voucher packs will be closed
     * automatically by the system. This method is for intentional administrative closure.</p>
     * 
     * @param voucherPackId the ID of the voucher pack to close
     * @throws com.poly.promotion.domain.core.exception.PromotionDomainException if the pack doesn't exist or cannot be closed
     */
    void closeVoucherPack(Long voucherPackId);
    
    /**
     * Reduces the stock of a voucher pack after vouchers have been redeemed.
     * 
     * <p>This method is called after successful voucher redemption to update
     * the pack's available quantity. If the stock reaches zero, the pack is
     * automatically closed.</p>
     * 
     * @param voucherPackId the ID of the voucher pack
     * @param quantity the number of vouchers redeemed (must be positive)
     * @throws com.poly.promotion.domain.core.exception.PromotionDomainException if the pack doesn't exist, is not published, or has insufficient stock
     * @throws IllegalArgumentException if quantity is null or non-positive
     */
    void reduceVoucherPackStockAfterRedeem(Long voucherPackId, Integer quantity);

    /**
     * Marks voucher packs as expired that are past their validity date.
     * 
     * <p>This method identifies all voucher packs that are currently PUBLISHED or PENDING
     * and have passed their packValidTo date, then updates their status to EXPIRED.
     * This is typically called by scheduled tasks or system maintenance processes.</p>
     * 
     * @return the number of voucher packs that were marked as expired
     */
    int markExpiredVoucherPacks();

    /**
     * Gets all voucher packs that are eligible for expiration.
     * 
     * <p>This method returns voucher packs that are currently PUBLISHED or PENDING
     * and have passed their packValidTo date. These packs should be marked as
     * expired by the system.</p>
     * 
     * @return a list of voucher packs eligible for expiration
     */
    List<VoucherPack> getVoucherPacksEligibleForExpiration();

    /**
     * Marks voucher packs as closed when they have zero quantity.
     * 
     * <p>This method identifies all voucher packs that are currently PUBLISHED or PENDING
     * and have zero quantity, then updates their status to CLOSED.
     * This is typically called by scheduled tasks or system maintenance processes.</p>
     * 
     * @return the number of voucher packs that were marked as closed
     */
    int markClosedVoucherPacks();

    /**
     * Gets all voucher packs that should be automatically closed due to zero quantity.
     * 
     * <p>This method returns voucher packs that are currently PUBLISHED or PENDING
     * and have zero quantity. These packs should be automatically closed by the system.</p>
     * 
     * @return a list of voucher packs eligible for automatic closure
     */
    List<VoucherPack> getVoucherPacksEligibleForClosure();

    /**
     * Updates the status of a voucher pack.
     * 
     * <p>This method should be used with caution and typically only for system operations
     * like expiration. It includes validation to ensure the status transition is valid
     * according to business rules.</p>
     * 
     * @param voucherPackId the ID of the voucher pack to update
     * @param newStatus the new status to set
     * @throws com.poly.promotion.domain.core.exception.PromotionDomainException if the pack doesn't exist or the status transition is invalid
     */
    void updateVoucherPackStatus(Long voucherPackId, VoucherPackStatus newStatus);

    /**
     * Publishes a PENDING voucher pack, making it available for customer redemption.
     * 
     * <p>This method allows administrators to manually activate voucher packs
     * that were created with future start dates or were kept in PENDING status.
     * The pack will be immediately available regardless of its packValidFrom date.</p>
     * 
     * @param voucherPackId the ID of the voucher pack to publish
     * @return the published voucher pack
     * @throws com.poly.promotion.domain.core.exception.PromotionDomainException if the pack doesn't exist or is not in PENDING status
     */
    VoucherPack publishVoucherPack(Long voucherPackId);
}
