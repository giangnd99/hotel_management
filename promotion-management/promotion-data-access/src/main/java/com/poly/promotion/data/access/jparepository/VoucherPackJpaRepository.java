package com.poly.promotion.data.access.jparepository;

import com.poly.promotion.data.access.jpaentity.VoucherPackJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * <h2>VoucherPackJpaRepository Interface</h2>
 * 
 * <p>Spring Data JPA repository interface for VoucherPack entities. This interface
 * provides data access methods for voucher pack operations including CRUD operations,
 * custom queries, and bulk operations.</p>
 * 
 * <p><strong>Repository Features:</strong></p>
 * <ul>
 *   <li>Standard CRUD operations inherited from JpaRepository</li>
 *   <li>Custom query methods for business-specific operations</li>
 *   <li>Bulk update operations for expiration management</li>
 *   <li>Pagination support for large result sets</li>
 *   <li>Performance-optimized queries with proper indexing</li>
 * </ul>
 * 
 * <p><strong>Query Methods:</strong></p>
 * <ul>
 *   <li>Status-based queries for filtering</li>
 *   <li>Date range queries for validity checks</li>
 *   <li>Quantity-based queries for availability</li>
 *   <li>Bulk operations for system maintenance</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherPackJpaEntity
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
@Repository
public interface VoucherPackJpaRepository extends JpaRepository<VoucherPackJpaEntity, Long> {

    /**
     * Finds voucher packs by their current status.
     * 
     * <p>This method retrieves all voucher packs that have the specified status.
     * It is commonly used for filtering packs by their lifecycle state.</p>
     * 
     * @param status the status to filter by
     * @return a list of voucher packs with the specified status
     */
    List<VoucherPackJpaEntity> findByStatus(VoucherPackJpaEntity.VoucherPackStatus status);

    /**
     * Finds voucher packs by status with pagination support.
     * 
     * <p>This method provides paginated results for voucher packs with a specific
     * status, useful for handling large result sets in administrative interfaces.</p>
     * 
     * @param status the status to filter by
     * @param pageable pagination and sorting parameters
     * @return a page of voucher packs with the specified status
     */
    Page<VoucherPackJpaEntity> findByStatus(VoucherPackJpaEntity.VoucherPackStatus status, Pageable pageable);

    /**
     * Finds voucher packs that are currently available for redemption.
     * 
     * <p>This method retrieves voucher packs that are published, have available
     * quantity, and are within their validity period. It is used for customer-facing
     * operations.</p>
     * 
     * @return a list of available voucher packs
     */
    @Query("SELECT vp FROM VoucherPackJpaEntity vp " +
           "WHERE vp.status = 'PUBLISHED' " +
           "AND vp.quantity > 0 " +
           "AND (vp.validFrom IS NULL OR vp.validFrom <= CURRENT_DATE) " +
           "AND (vp.validTo IS NULL OR vp.validTo >= CURRENT_DATE)")
    List<VoucherPackJpaEntity> findAvailableVoucherPacks();

    /**
     * Finds voucher packs that are eligible for expiration.
     * 
     * <p>This method retrieves voucher packs that have passed their validity
     * end date and should be marked as expired. It is used for automated
     * expiration management.</p>
     * 
     * @return a list of voucher packs eligible for expiration
     */
    @Query("SELECT vp FROM VoucherPackJpaEntity vp " +
           "WHERE vp.status IN ('PENDING', 'PUBLISHED') " +
           "AND vp.validTo IS NOT NULL " +
           "AND vp.validTo < CURRENT_DATE")
    List<VoucherPackJpaEntity> findVoucherPacksEligibleForExpiration();

    /**
     * Finds voucher packs by status and validity date range.
     * 
     * <p>This method provides flexible filtering by both status and validity
     * dates, useful for administrative reporting and management operations.</p>
     * 
     * @param status the status to filter by
     * @param fromDate the start date for validity range (inclusive)
     * @param toDate the end date for validity range (inclusive)
     * @return a list of voucher packs matching the criteria
     */
    @Query("SELECT vp FROM VoucherPackJpaEntity vp " +
           "WHERE vp.status = :status " +
           "AND (vp.validFrom IS NULL OR vp.validFrom >= :fromDate) " +
           "AND (vp.validTo IS NULL OR vp.validTo <= :toDate)")
    List<VoucherPackJpaEntity> findByStatusAndValidityDateRange(
            @Param("status") VoucherPackJpaEntity.VoucherPackStatus status,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate);

    /**
     * Finds voucher packs by required points range.
     * 
     * <p>This method retrieves voucher packs within a specific loyalty points
     * range, useful for customer filtering and administrative reporting.</p>
     * 
     * @param minPoints the minimum required points (inclusive)
     * @param maxPoints the maximum required points (inclusive)
     * @return a list of voucher packs within the points range
     */
    @Query("SELECT vp FROM VoucherPackJpaEntity vp " +
           "WHERE vp.requiredPoints >= :minPoints " +
           "AND vp.requiredPoints <= :maxPoints " +
           "AND vp.status = 'PUBLISHED'")
    List<VoucherPackJpaEntity> findByRequiredPointsRange(
            @Param("minPoints") Long minPoints,
            @Param("maxPoints") Long maxPoints);

    /**
     * Finds voucher packs by description containing the specified text.
     * 
     * <p>This method provides text search functionality for voucher pack
     * descriptions, useful for administrative search operations.</p>
     * 
     * @param description the text to search for in descriptions
     * @return a list of voucher packs with matching descriptions
     */
    List<VoucherPackJpaEntity> findByDescriptionContainingIgnoreCase(String description);

    /**
     * Counts voucher packs by status.
     * 
     * <p>This method provides quick counting of voucher packs by status,
     * useful for dashboard metrics and reporting.</p>
     * 
     * @param status the status to count
     * @return the count of voucher packs with the specified status
     */
    long countByStatus(VoucherPackJpaEntity.VoucherPackStatus status);

    /**
     * Checks if a voucher pack exists by ID.
     * 
     * <p>This method provides a quick existence check for voucher packs,
     * useful for validation operations.</p>
     * 
     * @param id the voucher pack ID to check
     * @return true if the voucher pack exists, false otherwise
     */
    boolean existsById(Long id);

    /**
     * Finds voucher pack by ID with optional result.
     * 
     * <p>This method provides a safe way to find voucher packs by ID,
     * returning an Optional to handle cases where the pack might not exist.</p>
     * 
     * @param id the voucher pack ID to find
     * @return an Optional containing the voucher pack if found
     */
    Optional<VoucherPackJpaEntity> findById(Long id);

    /**
     * Bulk update to mark expired voucher packs.
     * 
     * <p>This method performs a bulk update to mark voucher packs as expired
     * when they have passed their validity end date. It is used for automated
     * expiration management.</p>
     * 
     * @return the number of voucher packs updated
     */
    @Modifying
    @Query("UPDATE VoucherPackJpaEntity vp " +
           "SET vp.status = 'EXPIRED', vp.updatedAt = CURRENT_TIMESTAMP " +
           "WHERE vp.status IN ('PENDING', 'PUBLISHED') " +
           "AND vp.validTo IS NOT NULL " +
           "AND vp.validTo < CURRENT_DATE")
    int markExpiredVoucherPacks();

    /**
     * Finds voucher packs that should be automatically closed due to zero quantity.
     * 
     * <p>This method finds voucher packs that are currently PUBLISHED or PENDING
     * and have zero quantity. These packs should be automatically closed by the system.</p>
     * 
     * @return a list of voucher packs eligible for automatic closure
     */
    @Query("SELECT vp FROM VoucherPackJpaEntity vp " +
           "WHERE vp.status IN ('PENDING', 'PUBLISHED') " +
           "AND vp.quantity <= 0")
    List<VoucherPackJpaEntity> findVoucherPacksEligibleForClosure();

    /**
     * Updates the status of a specific voucher pack.
     * 
     * <p>This method provides a way to update the status of a voucher pack
     * with proper timestamp management.</p>
     * 
     * @param id the voucher pack ID to update
     * @param newStatus the new status to set
     * @param updatedBy the user performing the update
     * @return the number of voucher packs updated
     */
    @Modifying
    @Query("UPDATE VoucherPackJpaEntity vp " +
           "SET vp.status = :newStatus, vp.updatedAt = CURRENT_TIMESTAMP, vp.updatedBy = :updatedBy " +
           "WHERE vp.id = :id")
    int updateVoucherPackStatus(
            @Param("id") Long id,
            @Param("newStatus") VoucherPackJpaEntity.VoucherPackStatus newStatus,
            @Param("updatedBy") String updatedBy);

    /**
     * Decrements the quantity of a voucher pack.
     * 
     * <p>This method is used when a voucher is redeemed from a pack,
     * decreasing the available quantity by the specified amount.</p>
     * 
     * @param id the voucher pack ID to update
     * @param quantityToDecrease the quantity to decrease
     * @return the number of voucher packs updated
     */
    @Modifying
    @Query("UPDATE VoucherPackJpaEntity vp " +
           "SET vp.quantity = vp.quantity - :quantityToDecrease, vp.updatedAt = CURRENT_TIMESTAMP " +
           "WHERE vp.id = :id AND vp.quantity >= :quantityToDecrease")
    int decreaseVoucherPackQuantity(
            @Param("id") Long id,
            @Param("quantityToDecrease") Integer quantityToDecrease);
}

