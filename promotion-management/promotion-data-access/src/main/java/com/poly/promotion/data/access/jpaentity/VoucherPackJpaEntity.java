package com.poly.promotion.data.access.jpaentity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <h2>VoucherPackJpaEntity Class</h2>
 * 
 * <p>JPA entity representing a voucher pack in the database. This entity maps
 * the domain VoucherPack to the database schema and provides persistence
 * capabilities for voucher pack data.</p>
 * 
 * <p><strong>Database Mapping:</strong></p>
 * <ul>
 *   <li>Primary key: Auto-generated Long ID</li>
 *   <li>Business fields: Description, discount amount, validity, points, quantity</li>
 *   <li>Status fields: Current status and validity dates</li>
 *   <li>Audit fields: Creation and modification timestamps</li>
 * </ul>
 * 
 * <p><strong>JPA Features:</strong></p>
 * <ul>
 *   <li>Table mapping with proper column definitions</li>
 *   <li>Indexes for performance optimization</li>
 *   <li>Audit timestamps with automatic management</li>
 *   <li>Validation constraints for data integrity</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see com.poly.promotion.domain.core.entity.VoucherPack
 */
@Entity
@Table(
    name = "voucher_packs",
    indexes = {
        @Index(name = "idx_voucher_packs_status", columnList = "status"),
        @Index(name = "idx_voucher_packs_valid_from", columnList = "valid_from"),
        @Index(name = "idx_voucher_packs_valid_to", columnList = "valid_to"),
        @Index(name = "idx_voucher_packs_created_at", columnList = "created_at")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherPackJpaEntity {

    /**
     * Primary key identifier for the voucher pack.
     * 
     * <p>This field serves as the unique identifier for the voucher pack
     * in the database. It is automatically generated and cannot be modified
     * after creation.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    /**
     * Human-readable description of the voucher pack.
     * 
     * <p>This field provides a clear explanation of what the vouchers in this
     * pack offer to customers. It is required and cannot be null.</p>
     */
    @Column(name = "description", nullable = false, length = 500)
    private String description;

    /**
     * The discount amount for vouchers in this pack.
     * 
     * <p>This field represents the monetary value or percentage of the discount.
     * Values ≤ 100 represent percentage discounts, while values > 100 represent
     * fixed amount discounts.</p>
     */
    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount;

    /**
     * The validity period for individual vouchers created from this pack.
     * 
     * <p>This field stores the validity period as a string in the format
     * "X UNIT" (e.g., "30 DAYS", "2 WEEKS"). It is required and cannot be null.</p>
     */
    @Column(name = "valid_range", nullable = false, length = 50)
    private String validRange;

    /**
     * Number of loyalty points required to redeem a voucher from this pack.
     * 
     * <p>This field specifies the "cost" in loyalty points that customers
     * must pay to redeem a voucher. It must be positive and non-zero.</p>
     */
    @Column(name = "required_points", nullable = false)
    private Long requiredPoints;

    /**
     * Available quantity of vouchers in this pack.
     * 
     * <p>This field indicates how many vouchers are currently available
     * for customer redemption. It decreases with each redemption and cannot
     * go below zero.</p>
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * Start date when this voucher pack becomes available for redemption.
     * 
     * <p>This field defines the beginning of the validity period for the
     * voucher pack itself. Null value means immediate availability.</p>
     */
    @Column(name = "valid_from")
    @Temporal(TemporalType.DATE)
    private LocalDate validFrom;

    /**
     * End date when this voucher pack expires and becomes unavailable.
     * 
     * <p>This field defines the end of the validity period for the voucher
     * pack. After this date, the pack status should automatically transition
     * to EXPIRED.</p>
     */
    @Column(name = "valid_to")
    @Temporal(TemporalType.DATE)
    private LocalDate validTo;

    /**
     * Current status of the voucher pack in the system.
     * 
     * <p>This field indicates the current lifecycle state of the voucher
     * pack. Valid values are: PENDING, PUBLISHED, CLOSED, EXPIRED.</p>
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private VoucherPackStatus status;

    /**
     * Timestamp when this voucher pack was created in the system.
     * 
     * <p>This field is automatically managed by Hibernate and records
     * the exact date and time when the voucher pack was first created.</p>
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    /**
     * Identifier of the user who created this voucher pack.
     * 
     * <p>This field records which administrator or system user created
     * the voucher pack for audit trail purposes.</p>
     */
    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    /**
     * Timestamp when this voucher pack was last modified.
     * 
     * <p>This field is automatically updated by Hibernate whenever any
     * aspect of the voucher pack is modified.</p>
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    /**
     * Identifier of the user who last modified this voucher pack.
     * 
     * <p>This field records which administrator or system user made
     * the most recent modification for audit trail purposes.</p>
     */
    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    /**
     * Version field for optimistic locking.
     * 
     * <p>This field is automatically managed by Hibernate and is used
     * to implement optimistic locking to prevent concurrent modification
     * conflicts.</p>
     */
    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    /**
     * Enum representing the possible statuses of a voucher pack.
     * 
     * <p>This enum defines the lifecycle states that a voucher pack
     * can have throughout its existence in the system.</p>
     */
    public enum VoucherPackStatus {
        PENDING,    // Created but not yet available for redemption
        PUBLISHED,  // Available for customer redemption
        CLOSED,     // Manually closed by administrators
        EXPIRED     // Automatically expired due to validity period
    }

    /**
     * Pre-persist method to set default values before saving.
     * 
     * <p>This method is automatically called by JPA before persisting
     * the entity. It sets default values for required fields and
     * initializes the entity state.</p>
     */
    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = VoucherPackStatus.PENDING;
        }
        if (version == null) {
            version = 0L;
        }
    }

    /**
     * Pre-update method to handle updates before saving.
     * 
     * <p>This method is automatically called by JPA before updating
     * the entity. It can be used to perform validation or set
     * update-specific values.</p>
     */
    @PreUpdate
    protected void onUpdate() {
        // Update timestamp is automatically managed by @UpdateTimestamp
        // Additional update logic can be added here if needed
    }
}

