package com.poly.promotion.data.access.jparepository;

import com.poly.promotion.data.access.jpaentity.VoucherJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * <h2>VoucherJpaRepository Interface</h2>
 * 
 * <p>Spring Data JPA repository interface for Voucher entities. This interface
 * provides data access methods for voucher operations including CRUD operations,
 * custom queries, and bulk operations.</p>
 * 
 * <p><strong>Repository Features:</strong></p>
 * <ul>
 *   <li>Standard CRUD operations inherited from JpaRepository</li>
 *   <li>Custom query methods for business-specific operations</li>
 *   <li>Bulk update operations for expiration management</li>
 *   <li>Customer-specific voucher queries</li>
 *   <li>Performance-optimized queries with proper indexing</li>
 * </ul>
 * 
 * <p><strong>Query Methods:</strong></p>
 * <ul>
 *   <li>Status-based queries for filtering</li>
 *   <li>Customer-specific voucher retrieval</li>
 *   <li>Date range queries for validity checks</li>
 *   <li>Bulk operations for system maintenance</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherJpaEntity
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
@Repository
public interface VoucherJpaRepository extends JpaRepository<VoucherJpaEntity, String> {

    /**
     * Finds vouchers by their current status.
     * 
     * <p>This method retrieves all vouchers that have the specified status.
     * It is commonly used for filtering vouchers by their lifecycle state.</p>
     * 
     * @param status the status to filter by
     * @return a list of vouchers with the specified status
     */
    List<VoucherJpaEntity> findByStatus(VoucherJpaEntity.VoucherStatus status);

    /**
     * Finds vouchers by status with pagination support.
     * 
     * <p>This method provides paginated results for vouchers with a specific
     * status, useful for handling large result sets in administrative interfaces.</p>
     * 
     * @param status the status to filter by
     * @param pageable pagination and sorting parameters
     * @return a page of vouchers with the specified status
     */
    Page<VoucherJpaEntity> findByStatus(VoucherJpaEntity.VoucherStatus status, Pageable pageable);

    /**
     * Finds vouchers belonging to a specific customer.
     * 
     * <p>This method retrieves all vouchers owned by a specific customer,
     * regardless of their status. It is used for customer portfolio views.</p>
     * 
     * @param customerId the customer ID to filter by
     * @return a list of vouchers belonging to the customer
     */
    List<VoucherJpaEntity> findByCustomerId(String customerId);

    /**
     * Finds vouchers by customer ID and status.
     * 
     * <p>This method provides filtered results for customer vouchers by status,
     * useful for showing specific types of vouchers to customers.</p>
     * 
     * @param customerId the customer ID to filter by
     * @param status the status to filter by
     * @return a list of vouchers matching both criteria
     */
    List<VoucherJpaEntity> findByCustomerIdAndStatus(String customerId, VoucherJpaEntity.VoucherStatus status);

    /**
     * Finds vouchers by customer ID with pagination support.
     * 
     * <p>This method provides paginated results for customer vouchers,
     * useful for handling large customer portfolios.</p>
     * 
     * @param customerId the customer ID to filter by
     * @param pageable pagination and sorting parameters
     * @return a page of vouchers belonging to the customer
     */
    Page<VoucherJpaEntity> findByCustomerId(String customerId, Pageable pageable);

    /**
     * Finds vouchers by voucher pack ID.
     * 
     * <p>This method retrieves all vouchers that were created from a specific
     * voucher pack, useful for administrative reporting and pack management.</p>
     * 
     * @param voucherPackId the voucher pack ID to filter by
     * @return a list of vouchers from the specified pack
     */
    @Query("SELECT v FROM VoucherJpaEntity v WHERE v.voucherPack.id = :voucherPackId")
    List<VoucherJpaEntity> findByVoucherPackId(@Param("voucherPackId") Long voucherPackId);

    /**
     * Finds vouchers by voucher pack ID and status.
     * 
     * <p>This method provides filtered results for vouchers from a specific pack
     * by status, useful for pack-specific status reporting.</p>
     * 
     * @param voucherPackId the voucher pack ID to filter by
     * @param status the status to filter by
     * @return a list of vouchers matching both criteria
     */
    @Query("SELECT v FROM VoucherJpaEntity v WHERE v.voucherPack.id = :voucherPackId AND v.status = :status")
    List<VoucherJpaEntity> findByVoucherPackIdAndStatus(
            @Param("voucherPackId") Long voucherPackId,
            @Param("status") VoucherJpaEntity.VoucherStatus status);

    /**
     * Finds vouchers that are eligible for expiration.
     * 
     * <p>This method retrieves vouchers that have passed their validity
     * end date and should be marked as expired. It is used for automated
     * expiration management.</p>
     * 
     * @return a list of vouchers eligible for expiration
     */
    @Query("SELECT v FROM VoucherJpaEntity v " +
           "WHERE v.status IN ('PENDING', 'REDEEMED') " +
           "AND v.validTo < CURRENT_TIMESTAMP")
    List<VoucherJpaEntity> findVouchersEligibleForExpiration();

    /**
     * Finds vouchers by customer ID that are eligible for expiration.
     * 
     * <p>This method retrieves vouchers for a specific customer that have
     * passed their validity end date, useful for customer-specific expiration
     * management.</p>
     * 
     * @param customerId the customer ID to filter by
     * @return a list of customer vouchers eligible for expiration
     */
    @Query("SELECT v FROM VoucherJpaEntity v " +
           "WHERE v.customerId = :customerId " +
           "AND v.status IN ('PENDING', 'REDEEMED') " +
           "AND v.validTo < CURRENT_TIMESTAMP")
    List<VoucherJpaEntity> findVouchersEligibleForExpirationByCustomerId(@Param("customerId") String customerId);

    /**
     * Finds vouchers by redemption date range.
     * 
     * <p>This method provides flexible filtering by redemption dates,
     * useful for reporting and analytics on voucher redemption patterns.</p>
     * 
     * @param fromDate the start date for redemption range (inclusive)
     * @param toDate the end date for redemption range (inclusive)
     * @return a list of vouchers redeemed within the date range
     */
    @Query("SELECT v FROM VoucherJpaEntity v " +
           "WHERE v.redeemedAt >= :fromDate AND v.redeemedAt <= :toDate")
    List<VoucherJpaEntity> findByRedemptionDateRange(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate);

    /**
     * Finds vouchers by validity end date range.
     * 
     * <p>This method provides flexible filtering by expiration dates,
     * useful for expiration management and reporting.</p>
     * 
     * @param fromDate the start date for validity range (inclusive)
     * @param toDate the end date for validity range (inclusive)
     * @return a list of vouchers expiring within the date range
     */
    @Query("SELECT v FROM VoucherJpaEntity v " +
           "WHERE v.validTo >= :fromDate AND v.validTo <= :toDate")
    List<VoucherJpaEntity> findByValidityDateRange(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate);

    /**
     * Finds vouchers by voucher code.
     * 
     * <p>This method retrieves a voucher by its unique code, used during
     * voucher application in transactions.</p>
     * 
     * @param voucherCode the voucher code to find
     * @return an Optional containing the voucher if found
     */
    Optional<VoucherJpaEntity> findByVoucherCode(String voucherCode);

    /**
     * Checks if a voucher exists by voucher code.
     * 
     * <p>This method provides a quick existence check for vouchers by code,
     * useful for validation operations during voucher application.</p>
     * 
     * @param voucherCode the voucher code to check
     * @return true if the voucher exists, false otherwise
     */
    boolean existsByVoucherCode(String voucherCode);

    /**
     * Counts vouchers by status.
     * 
     * <p>This method provides quick counting of vouchers by status,
     * useful for dashboard metrics and reporting.</p>
     * 
     * @param status the status to count
     * @return the count of vouchers with the specified status
     */
    long countByStatus(VoucherJpaEntity.VoucherStatus status);

    /**
     * Counts vouchers by customer ID.
     * 
     * <p>This method provides quick counting of vouchers for a specific customer,
     * useful for customer portfolio metrics.</p>
     * 
     * @param customerId the customer ID to count vouchers for
     * @return the count of vouchers belonging to the customer
     */
    long countByCustomerId(String customerId);

    /**
     * Counts vouchers by customer ID and status.
     * 
     * <p>This method provides quick counting of vouchers for a specific customer
     * with a specific status, useful for customer portfolio status metrics.</p>
     * 
     * @param customerId the customer ID to count vouchers for
     * @param status the status to count
     * @return the count of vouchers matching both criteria
     */
    long countByCustomerIdAndStatus(String customerId, VoucherJpaEntity.VoucherStatus status);

    /**
     * Bulk update to mark expired vouchers.
     * 
     * <p>This method performs a bulk update to mark vouchers as expired
     * when they have passed their validity end date. It is used for automated
     * expiration management.</p>
     * 
     * @return the number of vouchers updated
     */
    @Modifying
    @Query("UPDATE VoucherJpaEntity v " +
           "SET v.status = 'EXPIRED', v.updatedAt = CURRENT_TIMESTAMP " +
           "WHERE v.status IN ('PENDING', 'REDEEMED') " +
           "AND v.validTo < CURRENT_TIMESTAMP")
    int markExpiredVouchers();

    /**
     * Updates the status of a specific voucher.
     * 
     * <p>This method provides a way to update the status of a voucher
     * with proper timestamp management.</p>
     * 
     * @param id the voucher ID to update
     * @param newStatus the new status to set
     * @return the number of vouchers updated
     */
    @Modifying
    @Query("UPDATE VoucherJpaEntity v " +
           "SET v.status = :newStatus, v.updatedAt = CURRENT_TIMESTAMP " +
           "WHERE v.id = :id")
    int updateVoucherStatus(
            @Param("id") String id,
            @Param("newStatus") VoucherJpaEntity.VoucherStatus newStatus);

    /**
     * Marks a voucher as used with timestamp.
     * 
     * <p>This method is used when a voucher is applied to a transaction,
     * updating its status to USED and recording the usage timestamp.</p>
     * 
     * @param id the voucher ID to mark as used
     * @return the number of vouchers updated
     */
    @Modifying
    @Query("UPDATE VoucherJpaEntity v " +
           "SET v.status = 'USED', v.usedAt = CURRENT_TIMESTAMP, v.updatedAt = CURRENT_TIMESTAMP " +
           "WHERE v.id = :id AND v.status = 'REDEEMED'")
    int markVoucherAsUsed(@Param("id") String id);

    /**
     * Batch update for multiple voucher statuses.
     * 
     * <p>This method provides efficient batch updating of voucher statuses,
     * useful for bulk operations like expiration management.</p>
     * 
     * @param ids the list of voucher IDs to update
     * @param newStatus the new status to set
     * @return the number of vouchers updated
     */
    @Modifying
    @Query("UPDATE VoucherJpaEntity v " +
           "SET v.status = :newStatus, v.updatedAt = CURRENT_TIMESTAMP " +
           "WHERE v.id IN :ids")
    int updateVoucherStatusBatch(
            @Param("ids") List<String> ids,
            @Param("newStatus") VoucherJpaEntity.VoucherStatus newStatus);
}

