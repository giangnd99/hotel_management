package com.poly.promotion.domain.application.spi.repository;

import com.poly.promotion.domain.core.entity.VoucherPack;
import com.poly.promotion.domain.core.valueobject.VoucherPackStatus;

import java.util.List;

/**
 * <h2>VoucherPackRepository Interface</h2>
 * 
 * <p>Repository interface for data access operations on voucher packs in the promotion system.
 * This interface defines the contract for persisting, retrieving, and managing voucher pack
 * entities in the underlying data store.</p>
 * 
 * <p><strong>Repository Responsibilities:</strong></p>
 * <ul>
 *   <li>CRUD operations for voucher pack entities</li>
 *   <li>Status-based queries and filtering</li>
 *   <li>Stock management operations</li>
 *   <li>Expiration eligibility queries</li>
 *   <li>Bulk status updates</li>
 * </ul>
 * 
 * <p><strong>Architecture Role:</strong></p>
 * <p>This interface is part of the SPI (Service Provider Interface) layer in the
 * hexagonal architecture. It defines the contract that data access implementations
 * must fulfill, allowing the domain to remain independent of specific data storage
 * technologies.</p>
 * 
 * <p><strong>Implementation Requirements:</strong></p>
 * <p>Implementations of this interface should:</p>
 * <ul>
 *   <li>Handle database transactions appropriately</li>
 *   <li>Implement proper error handling and logging</li>
 *   <li>Provide efficient querying and indexing</li>
 *   <li>Ensure data consistency and integrity</li>
 *   <li>Support concurrent access safely</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherPack
 * @see VoucherPackStatus
 */
public interface VoucherPackRepository {
    
    /**
     * Checks if a voucher pack exists with the specified ID.
     * 
     * <p>This method performs a lightweight existence check without retrieving
     * the full entity data. It's useful for validation purposes and can be
     * more efficient than retrieving the full entity when only existence
     * information is needed.</p>
     * 
     * @param voucherPackId the unique identifier of the voucher pack to check
     * @return true if a voucher pack with the specified ID exists, false otherwise
     */
    boolean existsById(Long voucherPackId);
    
    /**
     * Checks if a voucher pack has a specific status.
     * 
     * <p>This method provides a quick way to verify a voucher pack's current
     * status without retrieving the full entity. It's useful for business
     * rule validation and status-based decision making.</p>
     * 
     * @param voucherPackId the unique identifier of the voucher pack to check
     * @param status the status to check against
     * @return true if the voucher pack has the specified status, false otherwise
     */
    boolean isOfStatus(Long voucherPackId, VoucherPackStatus status);
    
    /**
     * Gets the current available quantity of a voucher pack.
     * 
     * <p>This method returns the number of vouchers that can still be redeemed
     * from the specified pack. It's used for stock validation and business
     * rule enforcement.</p>
     * 
     * @param voucherPackId the unique identifier of the voucher pack
     * @return the current available quantity (must be non-negative)
     */
    long getVoucherPackQuantity(Long voucherPackId);
    
    /**
     * Retrieves a voucher pack by its unique identifier.
     * 
     * <p>This method returns the complete voucher pack entity if found,
     * or null if no voucher pack exists with the specified ID. The returned
     * entity includes all fields and relationships.</p>
     * 
     * @param voucherPackId the unique identifier of the voucher pack to retrieve
     * @return the voucher pack with the specified ID, or null if not found
     */
    VoucherPack getVoucherPackById(Long voucherPackId);

    /**
     * Retrieves all voucher packs with a specific status or statuses.
     * 
     * <p>This method supports filtering by one or more statuses, allowing
     * callers to retrieve packs in specific states. If no statuses are provided,
     * all voucher packs are returned.</p>
     * 
     * @param status optional status values to filter by (if none provided, returns all packs)
     * @return a list of voucher packs matching the status criteria
     */
    List<VoucherPack> getAllVoucherPacksWithStatus(VoucherPackStatus... status);

    /**
     * Creates a new voucher pack in the data store.
     * 
     * <p>This method persists a new voucher pack entity to the underlying
     * data store. The entity should not have an ID set, as the implementation
     * will generate and assign one.</p>
     * 
     * @param voucherPack the voucher pack to create (must not have an ID set)
     * @return the created voucher pack with generated ID and any other system-assigned values
     * @throws IllegalArgumentException if the voucher pack has an ID set or is invalid
     */
    VoucherPack createVoucherPack(VoucherPack voucherPack);

    /**
     * Updates a pending voucher pack in the data store.
     * 
     * <p>This method updates an existing voucher pack that is currently in
     * PENDING status. The implementation should validate that the pack exists
     * and is in the correct status before performing the update.</p>
     * 
     * @param updatingVoucherPack the updated voucher pack data
     * @return the updated voucher pack
     * @throws IllegalArgumentException if the voucher pack doesn't exist or is not in PENDING status
     */
    VoucherPack updatePendingVoucherPack(VoucherPack updatingVoucherPack);

    /**
     * Closes a voucher pack by setting its status to CLOSED.
     * 
     * <p>This method updates the status of an existing voucher pack to CLOSED,
     * indicating that it can no longer have vouchers redeemed from it. The
     * implementation should validate that the pack exists before performing
     * the status update.</p>
     * 
     * @param voucherPackId the unique identifier of the voucher pack to close
     * @throws IllegalArgumentException if the voucher pack doesn't exist
     */
    void closeVoucherPack(Long voucherPackId);
    
    /**
     * Reduces the stock of a voucher pack after redemption.
     * 
     * <p>This method decreases the available quantity of a voucher pack by
     * the specified amount. It's called after successful voucher redemption
     * to maintain accurate stock levels.</p>
     * 
     * @param voucherPackId the unique identifier of the voucher pack
     * @param quantity the quantity to reduce (must be positive)
     * @throws IllegalArgumentException if the voucher pack doesn't exist or quantity is invalid
     */
    void reduceVoucherPackStockAfterRedeem(Long voucherPackId, Integer quantity);

    /**
     * Marks voucher packs as expired that are past their validity date.
     * 
     * <p>This method identifies all voucher packs that are currently PUBLISHED or PENDING
     * and have passed their packValidTo date, then updates their status to EXPIRED.
     * This is typically called by scheduled tasks or system maintenance processes.</p>
     * 
     * <p><strong>Implementation Notes:</strong></p>
     * <ul>
     *   <li>Should use efficient bulk update operations when possible</li>
     *   <li>Should handle database transactions appropriately</li>
     *   <li>Should continue processing even if individual updates fail</li>
     *   <li>Should return accurate counts of processed items</li>
     * </ul>
     * 
     * @return the number of voucher packs that were marked as expired
     */
    int markExpiredVoucherPacks();

    /**
     * Updates the status of a voucher pack.
     * 
     * <p>This method provides a general-purpose way to update a voucher pack's
     * status. It should be used with caution and typically only for system
     * operations like expiration or administrative actions.</p>
     * 
     * @param voucherPackId the unique identifier of the voucher pack to update
     * @param newStatus the new status to set
     * @throws IllegalArgumentException if the voucher pack doesn't exist
     */
    void updateVoucherPackStatus(Long voucherPackId, VoucherPackStatus newStatus);

    /**
     * Gets all voucher packs that are eligible for expiration.
     * 
     * <p>This method returns voucher packs that are currently PUBLISHED or PENDING
     * and have passed their packValidTo date. These packs should be marked as
     * expired by the system.</p>
     * 
     * <p><strong>Query Criteria:</strong></p>
     * <ul>
     *   <li>Status is PUBLISHED or PENDING</li>
     *   <li>packValidTo date is in the past</li>
     *   <li>Ordered by expiration date (earliest first)</li>
     * </ul>
     * 
     * @return a list of voucher packs eligible for expiration
     */
    List<VoucherPack> getVoucherPacksEligibleForExpiration();

    /**
     * Gets all voucher packs that should be automatically closed due to zero quantity.
     * 
     * <p>This method returns voucher packs that are currently PUBLISHED or PENDING
     * and have zero quantity. These packs should be automatically closed by the system.</p>
     * 
     * @return a list of voucher packs eligible for automatic closure
     */
    List<VoucherPack> getVoucherPacksEligibleForClosure();
}
