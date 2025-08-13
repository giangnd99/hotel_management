package com.poly.promotion.domain.application.spi.repository;

import com.poly.promotion.domain.core.entity.Voucher;
import com.poly.promotion.domain.core.valueobject.VoucherStatus;

import java.util.List;

/**
 * <h2>VoucherRepository Interface</h2>
 * 
 * <p>Repository interface for data access operations on individual vouchers in the promotion system.
 * This interface defines the contract for persisting, retrieving, and managing voucher entities
 * in the underlying data store.</p>
 * 
 * <p><strong>Repository Responsibilities:</strong></p>
 * <ul>
 *   <li>CRUD operations for voucher entities</li>
 *   <li>Status-based queries and filtering</li>
 *   <li>Customer-specific voucher retrieval</li>
 *   <li>Expiration eligibility queries</li>
 *   <li>Bulk status updates and operations</li>
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
 *   <li>Implement proper voucher code uniqueness validation</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see Voucher
 * @see VoucherStatus
 */
public interface VoucherRepository {
    
    /**
     * Checks if a voucher exists with the specified ID.
     * 
     * <p>This method performs a lightweight existence check without retrieving
     * the full entity data. It's useful for validation purposes and can be
     * more efficient than retrieving the full entity when only existence
     * information is needed.</p>
     * 
     * @param voucherId the unique identifier of the voucher to check
     * @return true if a voucher with the specified ID exists, false otherwise
     */
    boolean existsById(String voucherId);

    /**
     * Retrieves a voucher by its unique identifier.
     * 
     * <p>This method returns the complete voucher entity if found, or null
     * if no voucher exists with the specified ID. The returned entity
     * includes all fields and relationships.</p>
     * 
     * @param voucherId the unique identifier of the voucher to retrieve
     * @return the voucher with the specified ID, or null if not found
     */
    Voucher getVoucherById(String voucherId);

    /**
     * Retrieves a voucher by its unique voucher code.
     * 
     * <p>This method is used when customers want to apply a voucher using
     * its human-readable code. It's the primary method for voucher lookup
     * during transaction processing.</p>
     * 
     * @param voucherCode the unique voucher code to search for
     * @return the voucher with the specified code, or null if not found
     */
    Voucher getVoucherByCode(String voucherCode);

    /**
     * Retrieves all vouchers for a specific customer with optional status filtering.
     * 
     * <p>This method allows customers to view their voucher collection and supports
     * filtering by status to show only active, used, or expired vouchers. If no
     * statuses are provided, all vouchers belonging to the customer are returned.</p>
     * 
     * @param customerId the unique identifier of the customer
     * @param statuses optional status values to filter by (if none provided, returns all vouchers)
     * @return a list of vouchers belonging to the customer, optionally filtered by status
     */
    List<Voucher> getAllVouchersWithStatus(String customerId, VoucherStatus... statuses);

    /**
     * Retrieves all vouchers from a specific voucher pack.
     * 
     * <p>This method is useful for administrative purposes, such as generating
     * reports on voucher pack usage or performing bulk operations on vouchers
     * from a specific pack.</p>
     * 
     * @param voucherPackId the unique identifier of the voucher pack
     * @return a list of all vouchers created from the specified pack
     */
    List<Voucher> getVouchersByPackId(Long voucherPackId);

    /**
     * Creates a new voucher in the data store.
     * 
     * <p>This method persists a new voucher entity to the underlying data store.
     * The entity should not have an ID set, as the implementation will generate
     * and assign one. The voucher code should be unique across the system.</p>
     * 
     * @param voucher the voucher to create (must not have an ID set)
     * @return the created voucher with generated ID and any other system-assigned values
     * @throws IllegalArgumentException if the voucher has an ID set or is invalid
     */
    Voucher createVoucher(Voucher voucher);

    /**
     * Updates an existing voucher in the data store.
     * 
     * <p>This method updates an existing voucher entity. The implementation should
     * validate that the voucher exists before performing the update. This method
     * is typically used when voucher status changes (e.g., from REDEEMED to USED).</p>
     * 
     * @param voucher the updated voucher data
     * @return the updated voucher
     * @throws IllegalArgumentException if the voucher doesn't exist
     */
    Voucher updateVoucher(Voucher voucher);

    /**
     * Updates the status of a voucher.
     * 
     * <p>This method provides a direct way to update a voucher's status without
     * retrieving the full entity. It's useful for bulk operations and system
     * processes like expiration.</p>
     * 
     * @param voucherId the unique identifier of the voucher to update
     * @param newStatus the new status to set
     * @throws IllegalArgumentException if the voucher doesn't exist
     */
    void updateVoucherStatus(String voucherId, VoucherStatus newStatus);

    /**
     * Checks if a voucher code is already in use.
     * 
     * <p>This method validates voucher code uniqueness before creating new vouchers.
     * It's essential for maintaining data integrity and preventing duplicate
     * voucher codes in the system.</p>
     * 
     * @param voucherCode the voucher code to check
     * @return true if the code is already in use, false otherwise
     */
    boolean isVoucherCodeExists(String voucherCode);

    /**
     * Marks vouchers as expired that are past their validity date.
     * 
     * <p>This method identifies all vouchers that are currently REDEEMED
     * and have passed their validTo date, then updates their status to EXPIRED.
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
     * @return the number of vouchers that were marked as expired
     */
    int markExpiredVouchers();

    /**
     * Gets all vouchers that are eligible for expiration.
     * 
     * <p>This method returns vouchers that are currently REDEEMED
     * and have passed their validTo date. These vouchers should be marked as
     * expired by the system.</p>
     * 
     * <p><strong>Query Criteria:</strong></p>
     * <ul>
     *   <li>Status is REDEEMED</li>
     *   <li>validTo date is in the past</li>
     *   <li>Ordered by expiration date (earliest first)</li>
     * </ul>
     * 
     * @return a list of vouchers eligible for expiration
     */
    List<Voucher> getVouchersEligibleForExpiration();

    /**
     * Updates the status of multiple vouchers in a batch operation.
     * 
     * <p>This method is useful for bulk status updates like expiration or
     * administrative operations. It should be implemented efficiently to handle
     * large numbers of vouchers without performance degradation.</p>
     * 
     * <p><strong>Implementation Notes:</strong></p>
     * <ul>
     *   <li>Should use efficient bulk update operations</li>
     *   <li>Should handle database transactions appropriately</li>
     *   <li>Should continue processing even if individual updates fail</li>
     *   <li>Should return accurate counts of processed items</li>
     * </ul>
     * 
     * @param voucherIds list of voucher IDs to update
     * @param newStatus the new status to set for all vouchers
     * @return the number of vouchers that were successfully updated
     */
    int updateVoucherStatusBatch(List<String> voucherIds, VoucherStatus newStatus);
}
