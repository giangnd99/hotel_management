package com.poly.promotion.data.access.adapter;

import com.poly.promotion.data.access.jpaentity.VoucherPackJpaEntity;
import com.poly.promotion.data.access.jparepository.VoucherPackJpaRepository;
import com.poly.promotion.data.access.transformer.VoucherPackTransformer;
import com.poly.promotion.domain.application.spi.repository.VoucherPackRepository;
import com.poly.promotion.domain.core.entity.VoucherPack;
import com.poly.promotion.domain.core.valueobject.VoucherPackStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * <h2>VoucherPackRepositoryAdapter Class</h2>
 * 
 * <p>Repository adapter that implements the VoucherPackRepository interface from the domain layer.
 * This class acts as a bridge between the domain layer and the data access layer, providing
 * persistence operations for VoucherPack entities.</p>
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
 * @see VoucherPackRepository
 * @see VoucherPackJpaRepository
 * @see VoucherPackTransformer
 */
@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VoucherPackRepositoryAdapter implements VoucherPackRepository {

    private final VoucherPackJpaRepository jpaRepository;
    private final VoucherPackTransformer transformer;

    /**
     * Checks if a voucher pack exists with the specified ID.
     * 
     * @param voucherPackId the unique identifier of the voucher pack to check
     * @return true if a voucher pack with the specified ID exists, false otherwise
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long voucherPackId) {
        log.debug("Checking existence of voucher pack with ID: {}", voucherPackId);
        
        try {
            boolean exists = jpaRepository.existsById(voucherPackId);
            log.debug("Voucher pack exists: {}", exists);
            return exists;
            
        } catch (Exception e) {
            log.error("Error checking existence of voucher pack with ID: {}", voucherPackId, e);
            throw new RuntimeException("Failed to check voucher pack existence", e);
        }
    }

    /**
     * Checks if a voucher pack has a specific status.
     * 
     * @param voucherPackId the unique identifier of the voucher pack to check
     * @param status the status to check against
     * @return true if the voucher pack has the specified status, false otherwise
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isOfStatus(Long voucherPackId, VoucherPackStatus status) {
        log.debug("Checking if voucher pack {} has status: {}", voucherPackId, status);
        
        try {
            Optional<VoucherPackJpaEntity> jpaEntity = jpaRepository.findById(voucherPackId);
            if (jpaEntity.isEmpty()) {
                return false;
            }
            
            VoucherPackJpaEntity.VoucherPackStatus jpaStatus = mapDomainStatusToJpaStatus(status);
            boolean hasStatus = jpaEntity.get().getStatus() == jpaStatus;
            
            log.debug("Voucher pack {} has status {}: {}", voucherPackId, status, hasStatus);
            return hasStatus;
            
        } catch (Exception e) {
            log.error("Error checking status for voucher pack: {}", voucherPackId, e);
            throw new RuntimeException("Failed to check voucher pack status", e);
        }
    }

    /**
     * Gets the current available quantity of a voucher pack.
     * 
     * @param voucherPackId the unique identifier of the voucher pack
     * @return the current available quantity (must be non-negative)
     */
    @Override
    @Transactional(readOnly = true)
    public long getVoucherPackQuantity(Long voucherPackId) {
        log.debug("Getting quantity for voucher pack: {}", voucherPackId);
        
        try {
            Optional<VoucherPackJpaEntity> jpaEntity = jpaRepository.findById(voucherPackId);
            long quantity = jpaEntity.map(VoucherPackJpaEntity::getQuantity).orElse(0);
            
            log.debug("Voucher pack {} has quantity: {}", voucherPackId, quantity);
            return quantity;
            
        } catch (Exception e) {
            log.error("Error getting quantity for voucher pack: {}", voucherPackId, e);
            throw new RuntimeException("Failed to get voucher pack quantity", e);
        }
    }

    /**
     * Retrieves a voucher pack by its unique identifier.
     * 
     * @param voucherPackId the unique identifier of the voucher pack to retrieve
     * @return the voucher pack with the specified ID, or null if not found
     */
    @Override
    @Transactional(readOnly = true)
    public VoucherPack getVoucherPackById(Long voucherPackId) {
        log.debug("Getting voucher pack by ID: {}", voucherPackId);
        
        try {
            Optional<VoucherPackJpaEntity> jpaEntity = jpaRepository.findById(voucherPackId);
            if (jpaEntity.isEmpty()) {
                return null;
            }
            
            VoucherPack voucherPack = transformer.toDomainEntity(jpaEntity.get());
            log.debug("Voucher pack found: {}", voucherPack != null);
            return voucherPack;
            
        } catch (Exception e) {
            log.error("Error getting voucher pack by ID: {}", voucherPackId, e);
            throw new RuntimeException("Failed to get voucher pack by ID", e);
        }
    }

    /**
     * Retrieves all voucher packs with a specific status or statuses.
     * 
     * @param status optional status values to filter by (if none provided, returns all packs)
     * @return a list of voucher packs matching the status criteria
     */
    @Override
    @Transactional(readOnly = true)
    public List<VoucherPack> getAllVoucherPacksWithStatus(VoucherPackStatus... status) {
        log.debug("Getting voucher packs with statuses: {}", (Object) status);
        
        try {
            if (status == null || status.length == 0) {
                List<VoucherPackJpaEntity> allEntities = jpaRepository.findAll();
                List<VoucherPack> allVoucherPacks = transformer.toDomainEntities(allEntities);
                log.debug("Found {} total voucher packs", allVoucherPacks.size());
                return allVoucherPacks;
            }
            
            List<VoucherPack> result = new java.util.ArrayList<>();
            for (VoucherPackStatus s : status) {
                VoucherPackJpaEntity.VoucherPackStatus jpaStatus = mapDomainStatusToJpaStatus(s);
                List<VoucherPackJpaEntity> jpaEntities = jpaRepository.findByStatus(jpaStatus);
                result.addAll(transformer.toDomainEntities(jpaEntities));
            }
            
            log.debug("Found {} voucher packs with specified statuses", result.size());
            return result;
            
        } catch (Exception e) {
            log.error("Error getting voucher packs with statuses: {}", (Object) status, e);
            throw new RuntimeException("Failed to get voucher packs with statuses", e);
        }
    }

    /**
     * Creates a new voucher pack in the data store.
     * 
     * @param voucherPack the voucher pack to create (must not have an ID set)
     * @return the created voucher pack with generated ID and any other system-assigned values
     * @throws IllegalArgumentException if the voucher pack has an ID set or is invalid
     */
    @Override
    @Transactional
    public VoucherPack createVoucherPack(VoucherPack voucherPack) {
        log.debug("Creating new voucher pack");
        
        if (voucherPack.getId() != null) {
            throw new IllegalArgumentException("Cannot create voucher pack with existing ID");
        }
        
        try {
            VoucherPackJpaEntity jpaEntity = transformer.toJpaEntity(voucherPack);
            VoucherPackJpaEntity savedEntity = jpaRepository.save(jpaEntity);
            VoucherPack savedVoucherPack = transformer.toDomainEntity(savedEntity);
            
            log.debug("Successfully created voucher pack with ID: {}", savedVoucherPack.getId());
            return savedVoucherPack;
            
        } catch (Exception e) {
            log.error("Error creating voucher pack", e);
            throw new RuntimeException("Failed to create voucher pack", e);
        }
    }

    /**
     * Updates a pending voucher pack in the data store.
     * 
     * @param updatingVoucherPack the updated voucher pack data
     * @return the updated voucher pack
     * @throws IllegalArgumentException if the voucher pack doesn't exist or is not in PENDING status
     */
    @Override
    @Transactional
    public VoucherPack updatePendingVoucherPack(VoucherPack updatingVoucherPack) {
        log.debug("Updating pending voucher pack with ID: {}", updatingVoucherPack.getId());
        
        if (updatingVoucherPack.getId() == null) {
            throw new IllegalArgumentException("Cannot update voucher pack without ID");
        }
        
        try {
            Optional<VoucherPackJpaEntity> existingJpaEntity = jpaRepository.findById(updatingVoucherPack.getId().getValue());
            if (existingJpaEntity.isEmpty()) {
                throw new IllegalArgumentException("Voucher pack not found: " + updatingVoucherPack.getId());
            }
            
            VoucherPackJpaEntity existing = existingJpaEntity.get();
            if (existing.getStatus() != VoucherPackJpaEntity.VoucherPackStatus.PENDING) {
                throw new IllegalArgumentException("Cannot update voucher pack not in PENDING status: " + existing.getStatus());
            }
            
            VoucherPackJpaEntity jpaEntity = transformer.toJpaEntity(updatingVoucherPack);
            VoucherPackJpaEntity savedEntity = jpaRepository.save(jpaEntity);
            VoucherPack updatedVoucherPack = transformer.toDomainEntity(savedEntity);
            
            log.debug("Successfully updated pending voucher pack with ID: {}", updatedVoucherPack.getId());
            return updatedVoucherPack;
            
        } catch (Exception e) {
            log.error("Error updating pending voucher pack with ID: {}", updatingVoucherPack.getId(), e);
            throw new RuntimeException("Failed to update pending voucher pack", e);
        }
    }

    /**
     * Closes a voucher pack by setting its status to CLOSED.
     * 
     * @param voucherPackId the unique identifier of the voucher pack to close
     * @throws IllegalArgumentException if the voucher pack doesn't exist
     */
    @Override
    @Transactional
    public void closeVoucherPack(Long voucherPackId) {
        log.debug("Closing voucher pack with ID: {}", voucherPackId);
        
        try {
            int updatedCount = jpaRepository.updateVoucherPackStatus(
                voucherPackId, 
                VoucherPackJpaEntity.VoucherPackStatus.CLOSED, 
                "SYSTEM"
            );
            if (updatedCount == 0) {
                throw new IllegalArgumentException("Voucher pack not found: " + voucherPackId);
            }
            
            log.debug("Successfully closed voucher pack with ID: {}", voucherPackId);
            
        } catch (Exception e) {
            log.error("Error closing voucher pack with ID: {}", voucherPackId, e);
            throw new RuntimeException("Failed to close voucher pack", e);
        }
    }

    /**
     * Reduces the stock of a voucher pack after redemption.
     * 
     * @param voucherPackId the unique identifier of the voucher pack
     * @param quantity the quantity to reduce (must be positive)
     * @throws IllegalArgumentException if the voucher pack doesn't exist or quantity is invalid
     */
    @Override
    @Transactional
    public void reduceVoucherPackStockAfterRedeem(Long voucherPackId, Integer quantity) {
        log.debug("Reducing stock by {} for voucher pack ID: {}", quantity, voucherPackId);
        
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive: " + quantity);
        }
        
        try {
            int updatedCount = jpaRepository.decreaseVoucherPackQuantity(voucherPackId, quantity);
            if (updatedCount == 0) {
                throw new IllegalArgumentException("Failed to reduce stock for voucher pack: " + voucherPackId);
            }
            
            log.debug("Successfully reduced stock by {} for voucher pack ID: {}", quantity, voucherPackId);
            
        } catch (Exception e) {
            log.error("Error reducing stock by {} for voucher pack ID: {}", quantity, voucherPackId, e);
            throw new RuntimeException("Failed to reduce voucher pack stock", e);
        }
    }

    /**
     * Marks voucher packs as expired that are past their validity date.
     * 
     * @return the number of voucher packs that were marked as expired
     */
    @Override
    @Transactional
    public int markExpiredVoucherPacks() {
        log.debug("Marking expired voucher packs");
        
        try {
            int updatedCount = jpaRepository.markExpiredVoucherPacks();
            log.debug("Successfully marked {} voucher packs as expired", updatedCount);
            return updatedCount;
            
        } catch (Exception e) {
            log.error("Error marking expired voucher packs", e);
            throw new RuntimeException("Failed to mark expired voucher packs", e);
        }
    }

    /**
     * Updates the status of a voucher pack.
     * 
     * @param voucherPackId the unique identifier of the voucher pack to update
     * @param newStatus the new status to set
     * @throws IllegalArgumentException if the voucher pack doesn't exist
     */
    @Override
    @Transactional
    public void updateVoucherPackStatus(Long voucherPackId, VoucherPackStatus newStatus) {
        log.debug("Updating voucher pack status to {} for ID: {}", newStatus, voucherPackId);
        
        try {
            VoucherPackJpaEntity.VoucherPackStatus jpaStatus = mapDomainStatusToJpaStatus(newStatus);
            int updatedCount = jpaRepository.updateVoucherPackStatus(voucherPackId, jpaStatus, "SYSTEM");
            if (updatedCount == 0) {
                throw new IllegalArgumentException("Voucher pack not found: " + voucherPackId);
            }
            
            log.debug("Successfully updated status to {} for voucher pack ID: {}", newStatus, voucherPackId);
            
        } catch (Exception e) {
            log.error("Error updating status to {} for voucher pack ID: {}", newStatus, voucherPackId, e);
            throw new RuntimeException("Failed to update voucher pack status", e);
        }
    }

    /**
     * Gets all voucher packs that are eligible for expiration.
     * 
     * @return a list of voucher packs eligible for expiration
     */
    @Override
    @Transactional(readOnly = true)
    public List<VoucherPack> getVoucherPacksEligibleForExpiration() {
        log.debug("Finding voucher packs eligible for expiration");
        
        try {
            List<VoucherPackJpaEntity> jpaEntities = jpaRepository.findVoucherPacksEligibleForExpiration();
            List<VoucherPack> voucherPacks = transformer.toDomainEntities(jpaEntities);
            
            log.debug("Found {} voucher packs eligible for expiration", voucherPacks.size());
            return voucherPacks;
            
        } catch (Exception e) {
            log.error("Error finding voucher packs eligible for expiration", e);
            throw new RuntimeException("Failed to find voucher packs eligible for expiration", e);
        }
    }

    /**
     * Gets all voucher packs that should be automatically closed due to zero quantity.
     * 
     * @return a list of voucher packs eligible for automatic closure
     */
    @Override
    @Transactional(readOnly = true)
    public List<VoucherPack> getVoucherPacksEligibleForClosure() {
        log.debug("Finding voucher packs eligible for automatic closure due to zero quantity");
        
        try {
            List<VoucherPackJpaEntity> jpaEntities = jpaRepository.findVoucherPacksEligibleForClosure();
            List<VoucherPack> voucherPacks = transformer.toDomainEntities(jpaEntities);
            
            log.debug("Found {} voucher packs eligible for automatic closure", voucherPacks.size());
            return voucherPacks;
            
        } catch (Exception e) {
            log.error("Error finding voucher packs eligible for automatic closure", e);
            throw new RuntimeException("Failed to find voucher packs eligible for automatic closure", e);
        }
    }

    /**
     * Maps a domain VoucherPackStatus to its JPA equivalent.
     * 
     * @param domainStatus the domain status to map
     * @return the corresponding JPA status
     */
    private VoucherPackJpaEntity.VoucherPackStatus mapDomainStatusToJpaStatus(VoucherPackStatus domainStatus) {
        if (domainStatus == null) {
            return null;
        }
        
        switch (domainStatus) {
            case PENDING:
                return VoucherPackJpaEntity.VoucherPackStatus.PENDING;
            case PUBLISHED:
                return VoucherPackJpaEntity.VoucherPackStatus.PUBLISHED;
            case CLOSED:
                return VoucherPackJpaEntity.VoucherPackStatus.CLOSED;
            case EXPIRED:
                return VoucherPackJpaEntity.VoucherPackStatus.EXPIRED;
            default:
                throw new IllegalArgumentException("Unknown domain status: " + domainStatus);
        }
    }
}

