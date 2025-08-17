package com.poly.promotion.data.access.adapter;

import com.poly.promotion.data.access.jpaentity.VoucherJpaEntity;
import com.poly.promotion.data.access.jparepository.VoucherJpaRepository;
import com.poly.promotion.data.access.transformer.VoucherTransformer;
import com.poly.promotion.domain.application.spi.repository.VoucherRepository;
import com.poly.promotion.domain.core.entity.Voucher;
import com.poly.promotion.domain.core.valueobject.VoucherStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * <h2>VoucherRepositoryAdapter Class</h2>
 * 
 * <p>Repository adapter that implements the VoucherRepository interface from the domain layer.
 * This class acts as a bridge between the domain layer and the data access layer, providing
 * persistence operations for Voucher entities.</p>
 * 
 * <p><strong>Responsibilities:</strong></p>
 * <ul>
 *   <li>Implements domain repository interface</li>
 *   <li>Coordinates between JPA repository and transformers</li>
 *   <li>Handles transaction management</li>
 *   <li>Provides business logic validation</li>
 *   <li>Ensures data consistency and integrity</li>
 * </ul>
 * 
 * <p><strong>Architecture Role:</strong></p>
 * <ul>
 *   <li>Part of the Hexagonal Architecture's adapter layer</li>
 *   <li>Implements the repository port defined in the domain</li>
 *   <li>Uses Spring Data JPA for data persistence</li>
 *   <li>Leverages MapStruct transformers for entity conversion</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherRepository
 * @see VoucherJpaRepository
 * @see VoucherTransformer
 */
@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VoucherRepositoryAdapter implements VoucherRepository {

    private final VoucherJpaRepository jpaRepository;
    private final VoucherTransformer transformer;

    /**
     * Checks if a voucher exists with the specified ID.
     * 
     * @param voucherId the unique identifier of the voucher to check
     * @return true if a voucher with the specified ID exists, false otherwise
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String voucherId) {
        log.debug("Checking existence of voucher with ID: {}", voucherId);
        
        try {
            boolean exists = jpaRepository.existsById(voucherId);
            log.debug("Voucher exists: {}", exists);
            return exists;
            
        } catch (Exception e) {
            log.error("Error checking existence of voucher with ID: {}", voucherId, e);
            throw new RuntimeException("Failed to check voucher existence", e);
        }
    }

    /**
     * Retrieves a voucher by its unique identifier.
     * 
     * @param voucherId the unique identifier of the voucher to retrieve
     * @return the voucher with the specified ID, or null if not found
     */
    @Override
    @Transactional(readOnly = true)
    public Voucher getVoucherById(String voucherId) {
        log.debug("Getting voucher by ID: {}", voucherId);
        
        try {
            Optional<VoucherJpaEntity> jpaEntity = jpaRepository.findById(voucherId);
            if (jpaEntity.isEmpty()) {
                return null;
            }
            
            Voucher voucher = transformer.toDomainEntity(jpaEntity.get());
            log.debug("Voucher found: {}", voucher != null);
            return voucher;
            
        } catch (Exception e) {
            log.error("Error getting voucher by ID: {}", voucherId, e);
            throw new RuntimeException("Failed to get voucher by ID", e);
        }
    }

    /**
     * Retrieves a voucher by its unique voucher code.
     * 
     * @param voucherCode the unique voucher code to retrieve
     * @return the voucher with the specified code, or null if not found
     */
    @Override
    @Transactional(readOnly = true)
    public Voucher getVoucherByCode(String voucherCode) {
        log.debug("Getting voucher by code: {}", voucherCode);
        
        try {
            Optional<VoucherJpaEntity> jpaEntity = jpaRepository.findByVoucherCode(voucherCode);
            if (jpaEntity.isEmpty()) {
                return null;
            }
            
            Voucher voucher = transformer.toDomainEntity(jpaEntity.get());
            log.debug("Voucher found by code: {}", voucher != null);
            return voucher;
            
        } catch (Exception e) {
            log.error("Error getting voucher by code: {}", voucherCode, e);
            throw new RuntimeException("Failed to get voucher by code", e);
        }
    }

    /**
     * Retrieves all vouchers for a specific customer with optional status filtering.
     * 
     * @param customerId the unique identifier of the customer
     * @param statuses optional status values to filter by (if none provided, returns all vouchers)
     * @return a list of vouchers belonging to the customer, optionally filtered by status
     */
    @Override
    @Transactional(readOnly = true)
    public List<Voucher> getAllVouchersWithStatus(String customerId, VoucherStatus... statuses) {
        log.debug("Getting vouchers for customer: {} with statuses: {}", customerId, (Object) statuses);
        
        try {
            if (statuses == null || statuses.length == 0) {
                List<VoucherJpaEntity> jpaEntities = jpaRepository.findByCustomerId(customerId);
                List<Voucher> vouchers = transformer.toDomainEntities(jpaEntities);
                log.debug("Found {} total vouchers for customer: {}", vouchers.size(), customerId);
                return vouchers;
            }
            
            List<Voucher> result = new java.util.ArrayList<>();
            for (VoucherStatus status : statuses) {
                VoucherJpaEntity.VoucherStatus jpaStatus = mapDomainStatusToJpaStatus(status);
                List<VoucherJpaEntity> jpaEntities = jpaRepository.findByCustomerIdAndStatus(customerId, jpaStatus);
                result.addAll(transformer.toDomainEntities(jpaEntities));
            }
            
            log.debug("Found {} vouchers for customer: {} with specified statuses", result.size(), customerId);
            return result;
            
        } catch (Exception e) {
            log.error("Error getting vouchers for customer: {} with statuses: {}", customerId, (Object) statuses, e);
            throw new RuntimeException("Failed to get vouchers for customer", e);
        }
    }

    /**
     * Retrieves all vouchers from a specific voucher pack.
     * 
     * @param voucherPackId the voucher pack identifier to filter by
     * @return a list of vouchers from the specified pack
     */
    @Override
    @Transactional(readOnly = true)
    public List<Voucher> getVouchersByPackId(Long voucherPackId) {
        log.debug("Getting vouchers by pack ID: {}", voucherPackId);
        
        try {
            List<VoucherJpaEntity> jpaEntities = jpaRepository.findByVoucherPackId(voucherPackId);
            List<Voucher> vouchers = transformer.toDomainEntities(jpaEntities);
            
            log.debug("Found {} vouchers for pack ID: {}", vouchers.size(), voucherPackId);
            return vouchers;
            
        } catch (Exception e) {
            log.error("Error getting vouchers by pack ID: {}", voucherPackId, e);
            throw new RuntimeException("Failed to get vouchers by pack ID", e);
        }
    }

    /**
     * Creates a new voucher in the data store.
     * 
     * @param voucher the voucher to create
     * @return the created voucher with generated ID and any other system-assigned values
     */
    @Override
    @Transactional
    public Voucher createVoucher(Voucher voucher) {
        log.debug("Creating new voucher");
        
        try {
            VoucherJpaEntity jpaEntity = transformer.toJpaEntity(voucher);
            VoucherJpaEntity savedEntity = jpaRepository.save(jpaEntity);
            Voucher savedVoucher = transformer.toDomainEntity(savedEntity);
            
            log.debug("Successfully created voucher with ID: {}", savedVoucher.getId());
            return savedVoucher;
            
        } catch (Exception e) {
            log.error("Error creating voucher", e);
            throw new RuntimeException("Failed to create voucher", e);
        }
    }

    /**
     * Updates an existing voucher in the data store.
     * 
     * @param voucher the voucher to update
     * @return the updated voucher
     */
    @Override
    @Transactional
    public Voucher updateVoucher(Voucher voucher) {
        log.debug("Updating voucher with ID: {}", voucher.getId());
        
        if (voucher.getId() == null) {
            throw new IllegalArgumentException("Cannot update voucher without ID");
        }
        
        try {
            VoucherJpaEntity jpaEntity = transformer.toJpaEntity(voucher);
            VoucherJpaEntity savedEntity = jpaRepository.save(jpaEntity);
            Voucher updatedVoucher = transformer.toDomainEntity(savedEntity);
            
            log.debug("Successfully updated voucher with ID: {}", updatedVoucher.getId());
            return updatedVoucher;
            
        } catch (Exception e) {
            log.error("Error updating voucher with ID: {}", voucher.getId(), e);
            throw new RuntimeException("Failed to update voucher", e);
        }
    }

    /**
     * Updates the status of a voucher.
     * 
     * @param voucherId the unique identifier of the voucher to update
     * @param newStatus the new status to set
     * @throws IllegalArgumentException if the voucher doesn't exist
     */
    @Override
    @Transactional
    public void updateVoucherStatus(String voucherId, VoucherStatus newStatus) {
        log.debug("Updating voucher status to {} for ID: {}", newStatus, voucherId);
        
        try {
            VoucherJpaEntity.VoucherStatus jpaStatus = mapDomainStatusToJpaStatus(newStatus);
            int updatedCount = jpaRepository.updateVoucherStatus(voucherId, jpaStatus);
            if (updatedCount == 0) {
                throw new IllegalArgumentException("Voucher not found: " + voucherId);
            }
            
            log.debug("Successfully updated status to {} for voucher ID: {}", newStatus, voucherId);
            
        } catch (Exception e) {
            log.error("Error updating voucher status to {} for ID: {}", newStatus, voucherId, e);
            throw new RuntimeException("Failed to update voucher status", e);
        }
    }

    /**
     * Checks if a voucher code already exists in the system.
     * 
     * @param voucherCode the voucher code to check
     * @return true if the voucher code exists, false otherwise
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isVoucherCodeExists(String voucherCode) {
        log.debug("Checking if voucher code exists: {}", voucherCode);
        
        try {
            boolean exists = jpaRepository.existsByVoucherCode(voucherCode);
            log.debug("Voucher code exists: {}", exists);
            return exists;
            
        } catch (Exception e) {
            log.error("Error checking if voucher code exists: {}", voucherCode, e);
            throw new RuntimeException("Failed to check voucher code existence", e);
        }
    }

    /**
     * Marks expired vouchers as expired.
     * 
     * @return the number of vouchers updated
     */
    @Override
    @Transactional
    public int markExpiredVouchers() {
        log.debug("Marking expired vouchers");
        
        try {
            int updatedCount = jpaRepository.markExpiredVouchers();
            log.debug("Successfully marked {} vouchers as expired", updatedCount);
            return updatedCount;
            
        } catch (Exception e) {
            log.error("Error marking expired vouchers", e);
            throw new RuntimeException("Failed to mark expired vouchers", e);
        }
    }

    /**
     * Gets all vouchers that are eligible for expiration.
     * 
     * @return a list of vouchers eligible for expiration
     */
    @Override
    @Transactional(readOnly = true)
    public List<Voucher> getVouchersEligibleForExpiration() {
        log.debug("Finding vouchers eligible for expiration");
        
        try {
            List<VoucherJpaEntity> jpaEntities = jpaRepository.findVouchersEligibleForExpiration();
            List<Voucher> vouchers = transformer.toDomainEntities(jpaEntities);
            
            log.debug("Found {} vouchers eligible for expiration", vouchers.size());
            return vouchers;
            
        } catch (Exception e) {
            log.error("Error finding vouchers eligible for expiration", e);
            throw new RuntimeException("Failed to find vouchers eligible for expiration", e);
        }
    }

    /**
     * Updates the status of multiple vouchers in a batch operation.
     * 
     * @param voucherIds list of voucher IDs to update
     * @param newStatus the new status to set for all vouchers
     * @return the number of vouchers that were successfully updated
     */
    @Override
    @Transactional
    public int updateVoucherStatusBatch(List<String> voucherIds, VoucherStatus newStatus) {
        log.debug("Updating status to {} for {} vouchers", newStatus, voucherIds.size());
        
        try {
            VoucherJpaEntity.VoucherStatus jpaStatus = mapDomainStatusToJpaStatus(newStatus);
            int updatedCount = jpaRepository.updateVoucherStatusBatch(voucherIds, jpaStatus);
            
            log.debug("Successfully updated status for {} vouchers", updatedCount);
            return updatedCount;
            
        } catch (Exception e) {
            log.error("Error updating status for {} vouchers", voucherIds.size(), e);
            throw new RuntimeException("Failed to update voucher status batch", e);
        }
    }

    /**
     * Maps a domain VoucherStatus to its JPA equivalent.
     * 
     * @param domainStatus the domain status to map
     * @return the corresponding JPA status
     */
    private VoucherJpaEntity.VoucherStatus mapDomainStatusToJpaStatus(VoucherStatus domainStatus) {
        if (domainStatus == null) {
            return null;
        }
        
        switch (domainStatus) {
            case PENDING:
                return VoucherJpaEntity.VoucherStatus.PENDING;
            case REDEEMED:
                return VoucherJpaEntity.VoucherStatus.REDEEMED;
            case USED:
                return VoucherJpaEntity.VoucherStatus.USED;
            case EXPIRED:
                return VoucherJpaEntity.VoucherStatus.EXPIRED;
            default:
                throw new IllegalArgumentException("Unknown domain status: " + domainStatus);
        }
    }
}
