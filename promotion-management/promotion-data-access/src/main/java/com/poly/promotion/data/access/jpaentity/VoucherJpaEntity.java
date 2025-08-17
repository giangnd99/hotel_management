package com.poly.promotion.data.access.jpaentity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <h2>VoucherJpaEntity Class</h2>
 * 
 * <p>JPA entity representing an individual voucher in the database. This entity maps
 * the domain Voucher to the database schema and provides persistence capabilities
 * for voucher data.</p>
 * 
 * <p><strong>Database Mapping:</strong></p>
 * <ul>
 *   <li>Primary key: UUID-based identifier</li>
 *   <li>Business fields: Voucher code, discount, validity, status</li>
 *   <li>Relationship fields: Voucher pack and customer references</li>
 *   <li>Audit fields: Creation and modification timestamps</li>
 * </ul>
 * 
 * <p><strong>JPA Features:</strong></p>
 * <ul>
 *   <li>Table mapping with proper column definitions</li>
 *   <li>Indexes for performance optimization</li>
 *   <li>Audit timestamps with automatic management</li>
 *   <li>Foreign key relationships for data integrity</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see com.poly.promotion.domain.core.entity.Voucher
 */
@Entity
@Table(
    name = "vouchers",
    indexes = {
        @Index(name = "idx_vouchers_code", columnList = "voucher_code", unique = true),
        @Index(name = "idx_vouchers_status", columnList = "status"),
        @Index(name = "idx_vouchers_customer_id", columnList = "customer_id"),
        @Index(name = "idx_vouchers_pack_id", columnList = "voucher_pack_id"),
        @Index(name = "idx_vouchers_redeemed_at", columnList = "redeemed_at"),
        @Index(name = "idx_vouchers_valid_to", columnList = "valid_to")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherJpaEntity {

    /**
     * Primary key identifier for the voucher.
     * 
     * <p>This field serves as the unique identifier for the voucher in the database.
     * It uses UUID to ensure global uniqueness and is generated when the voucher
     * is created.</p>
     */
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    /**
     * Unique voucher code that customers use to redeem their discount.
     * 
     * <p>This field provides the primary identifier that customers use to apply
     * their voucher during transactions. It is unique across the entire system
     * and serves as the customer's proof of voucher ownership.</p>
     */
    @Column(name = "voucher_code", nullable = false, unique = true, length = 20)
    private String voucherCode;

    /**
     * The discount amount that will be applied when this voucher is used.
     * 
     * <p>This field represents the monetary value or percentage of the discount
     * that customers will receive when they use this voucher. The actual discount
     * type is determined by the business logic.</p>
     */
    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount;

    /**
     * Reference to the voucher pack from which this voucher was created.
     * 
     * <p>This field establishes the relationship between the voucher and its
     * source voucher pack. It is required and cannot be null.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_pack_id", nullable = false, foreignKey = @ForeignKey(name = "fk_vouchers_voucher_pack"))
    private VoucherPackJpaEntity voucherPack;

    /**
     * Identifier of the customer who owns this voucher.
     * 
     * <p>This field links the voucher to a specific customer. It is required
     * and cannot be null, ensuring proper ownership tracking.</p>
     */
    @Column(name = "customer_id", nullable = false, length = 100)
    private String customerId;

    /**
     * Timestamp when this voucher was redeemed by the customer.
     * 
     * <p>This field records the exact date and time when the customer successfully
     * redeemed this voucher from a voucher pack using their loyalty points. It
     * marks the beginning of the voucher's validity period.</p>
     */
    @Column(name = "redeemed_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime redeemedAt;

    /**
     * Expiration date and time when this voucher becomes invalid.
     * 
     * <p>This field specifies the exact moment when the voucher expires and can
     * no longer be used. After this timestamp, the voucher status should
     * automatically transition to EXPIRED.</p>
     */
    @Column(name = "valid_to", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime validTo;

    /**
     * Current status of the voucher in the system.
     * 
     * <p>This field indicates the current lifecycle state of the voucher,
     * determining whether it can be used, has already been used, or has expired.</p>
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private VoucherStatus status;

    /**
     * Timestamp when this voucher was first used in a transaction.
     * 
     * <p>This field records when the voucher was first applied to a transaction.
     * It is null until the voucher is used and helps track voucher usage history.</p>
     */
    @Column(name = "used_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime usedAt;

    /**
     * Timestamp when this voucher was created in the system.
     * 
     <p>This field is automatically managed by Hibernate and records the exact
     * date and time when the voucher was first created.</p>
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    /**
     * Timestamp when this voucher was last modified.
     * 
     * <p>This field is automatically updated by Hibernate whenever any aspect
     * of the voucher is modified.</p>
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    /**
     * Version field for optimistic locking.
     * 
     * <p>This field is automatically managed by Hibernate and is used to implement
     * optimistic locking to prevent concurrent modification conflicts.</p>
     */
    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    /**
     * Enum representing the possible statuses of a voucher.
     * 
     * <p>This enum defines the lifecycle states that a voucher can have
     * throughout its existence in the system.</p>
     */
    public enum VoucherStatus {
        PENDING,    // Voucher is created but not yet available for use
        REDEEMED,   // Voucher has been redeemed and is available for use
        USED,       // Voucher has been applied to a transaction
        EXPIRED     // Voucher has expired and cannot be used
    }

    /**
     * Pre-persist method to set default values before saving.
     * 
     * <p>This method is automatically called by JPA before persisting the entity.
     * It sets default values for required fields and initializes the entity state.</p>
     */
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        if (status == null) {
            status = VoucherStatus.PENDING;
        }
        if (version == null) {
            version = 0L;
        }
        if (redeemedAt == null) {
            redeemedAt = LocalDateTime.now();
        }
    }

    /**
     * Pre-update method to handle updates before saving.
     * 
     * <p>This method is automatically called by JPA before updating the entity.
     * It can be used to perform validation or set update-specific values.</p>
     */
    @PreUpdate
    protected void onUpdate() {
        // Update timestamp is automatically managed by @UpdateTimestamp
        // Additional update logic can be added here if needed
    }

    /**
     * Helper method to check if the voucher is expired.
     * 
     * <p>This method provides a convenient way to check if the voucher has
     * expired based on the current system time.</p>
     * 
     * @return true if the voucher is expired, false otherwise
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(validTo);
    }

    /**
     * Helper method to check if the voucher can be used.
     * 
     * <p>This method checks if the voucher is in a state where it can be
     * used in a transaction.</p>
     * 
     * @return true if the voucher can be used, false otherwise
     */
    public boolean canBeUsed() {
        return status == VoucherStatus.REDEEMED && !isExpired();
    }
}

