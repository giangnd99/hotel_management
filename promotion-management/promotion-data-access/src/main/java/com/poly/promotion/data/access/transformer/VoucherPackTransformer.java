package com.poly.promotion.data.access.transformer;

import com.poly.promotion.data.access.jpaentity.VoucherPackJpaEntity;
import com.poly.promotion.domain.core.entity.VoucherPack;
import com.poly.promotion.domain.core.valueobject.VoucherPackId;
import com.poly.promotion.domain.core.valueobject.VoucherPackStatus;

import lombok.extern.slf4j.Slf4j;

import com.poly.promotion.domain.core.valueobject.DateRange;
import com.poly.promotion.domain.core.valueobject.Discount;
import com.poly.promotion.domain.core.valueobject.DiscountPercentage;
import com.poly.promotion.domain.core.valueobject.DiscountAmount;
import com.poly.domain.valueobject.Money;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <h2>VoucherPackTransformer Class</h2>
 * 
 * <p>Handles transformation between VoucherPack domain entities and VoucherPackJpaEntity.
 * This transformer ensures proper conversion of all fields including complex value objects.</p>
 * 
 * @author System
 * @version 1.0
 * @since 2024-01-01
 */
@Component
@Slf4j
public class VoucherPackTransformer {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Transforms a VoucherPackJpaEntity to a VoucherPack domain entity.
     * 
     * <p>This method converts a JPA entity to its corresponding domain entity,
     * handling all field mappings including the ID field.</p>
     * 
     * @param jpaEntity the JPA entity to transform
     * @return the corresponding domain entity
     */
    public VoucherPack toDomainEntity(VoucherPackJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }

        VoucherPack domainEntity = new VoucherPack();
        
        // Map ID field - this is the key fix for the NullPointerException
        log.debug("Mapping JPA entity ID: {} to domain entity", jpaEntity.getId());
        if (jpaEntity.getId() != null) {
            VoucherPackId voucherPackId = new VoucherPackId(jpaEntity.getId());
            domainEntity.setId(voucherPackId);
            log.debug("Successfully mapped ID: {} to domain entity", voucherPackId.getValue());
        } else {
            log.warn("JPA entity ID is null - this will cause issues!");
        }
        
        // Map other fields
        domainEntity.setDescription(jpaEntity.getDescription());
        domainEntity.setDiscountAmount(convertBigDecimalToDiscount(jpaEntity.getDiscountAmount()));
        domainEntity.setVoucherValidRange(convertStringToDateRange(jpaEntity.getValidRange()));
        domainEntity.setRequiredPoints(jpaEntity.getRequiredPoints());
        domainEntity.setQuantity(jpaEntity.getQuantity());
        domainEntity.setPackValidFrom(jpaEntity.getValidFrom());
        domainEntity.setPackValidTo(jpaEntity.getValidTo());
        domainEntity.setStatus(convertJpaStatusToDomainStatus(jpaEntity.getStatus().name()));
        domainEntity.setCreatedAt(jpaEntity.getCreatedAt());
        domainEntity.setCreatedBy(jpaEntity.getCreatedBy());
        domainEntity.setUpdatedAt(jpaEntity.getUpdatedAt());
        domainEntity.setUpdatedBy(jpaEntity.getUpdatedBy());
        
        return domainEntity;
    }

    /**
     * Transforms a VoucherPack domain entity to a VoucherPackJpaEntity.
     * 
     * <p>This method converts a domain entity to its corresponding JPA entity,
     * preparing it for database persistence operations.</p>
     * 
     * @param domainEntity the domain entity to transform
     * @return the corresponding JPA entity
     */
    public VoucherPackJpaEntity toJpaEntity(VoucherPack domainEntity) {
        if (domainEntity == null) {
            return null;
        }

        VoucherPackJpaEntity jpaEntity = new VoucherPackJpaEntity();
        
        // Map ID field
        if (domainEntity.getId() != null) {
            jpaEntity.setId(domainEntity.getId().getValue());
        }
        
        // Map other fields
        jpaEntity.setDescription(domainEntity.getDescription());
        jpaEntity.setDiscountAmount(convertDiscountToBigDecimal(domainEntity.getDiscountAmount()));
        jpaEntity.setValidRange(convertDateRangeToString(domainEntity.getVoucherValidRange()));
        jpaEntity.setRequiredPoints(domainEntity.getRequiredPoints());
        jpaEntity.setQuantity(domainEntity.getQuantity());
        jpaEntity.setValidFrom(domainEntity.getPackValidFrom());
        jpaEntity.setValidTo(domainEntity.getPackValidTo());
        jpaEntity.setStatus(VoucherPackJpaEntity.VoucherPackStatus.valueOf(convertDomainStatusToJpaStatus(domainEntity.getStatus())));
        jpaEntity.setCreatedAt(domainEntity.getCreatedAt());
        jpaEntity.setCreatedBy(domainEntity.getCreatedBy());
        jpaEntity.setUpdatedAt(domainEntity.getUpdatedAt());
        jpaEntity.setUpdatedBy(domainEntity.getUpdatedBy());
        
        return jpaEntity;
    }

    /**
     * Transforms a list of VoucherPackJpaEntity to a list of VoucherPack domain entities.
     * 
     * <p>This method provides bulk transformation for collections, useful for
     * retrieving multiple voucher packs from the database.</p>
     * 
     * @param jpaEntities the list of JPA entities to transform
     * @return the corresponding list of domain entities
     */
    public List<VoucherPack> toDomainEntities(List<VoucherPackJpaEntity> jpaEntities) {
        if (jpaEntities == null) {
            return null;
        }
        return jpaEntities.stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    /**
     * Transforms a list of VoucherPack domain entities to a list of VoucherPackJpaEntity.
     * 
     * <p>This method provides bulk transformation for collections, useful for
     * persisting multiple voucher packs to the database.</p>
     * 
     * @param domainEntities the list of domain entities to transform
     * @return the corresponding list of JPA entities
     */
    public List<VoucherPackJpaEntity> toJpaEntities(List<VoucherPack> domainEntities) {
        if (domainEntities == null) {
            return null;
        }
        return domainEntities.stream()
                .map(this::toJpaEntity)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing JPA entity with values from a domain entity.
     * 
     * <p>This method is used for updating existing voucher packs, preserving
     * certain fields like ID, version, and timestamps while updating others.</p>
     * 
     * @param domainEntity the domain entity containing updated values
     * @param jpaEntity the existing JPA entity to update
     */
    public void updateJpaEntity(VoucherPackJpaEntity jpaEntity, VoucherPack domainEntity) {
        if (domainEntity == null || jpaEntity == null) {
            return;
        }

        // Don't update ID, version, createdAt, createdBy
        jpaEntity.setDescription(domainEntity.getDescription());
        jpaEntity.setDiscountAmount(convertDiscountToBigDecimal(domainEntity.getDiscountAmount()));
        jpaEntity.setValidRange(convertDateRangeToString(domainEntity.getVoucherValidRange()));
        jpaEntity.setRequiredPoints(domainEntity.getRequiredPoints());
        jpaEntity.setQuantity(domainEntity.getQuantity());
        jpaEntity.setValidFrom(domainEntity.getPackValidFrom());
        jpaEntity.setValidTo(domainEntity.getPackValidTo());
        jpaEntity.setStatus(VoucherPackJpaEntity.VoucherPackStatus.valueOf(convertDomainStatusToJpaStatus(domainEntity.getStatus())));
        jpaEntity.setUpdatedAt(domainEntity.getUpdatedAt());
        jpaEntity.setUpdatedBy(domainEntity.getUpdatedBy());
    }

    // Helper methods for value object conversions
    private Discount convertBigDecimalToDiscount(BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        
        // Default to percentage discount for values <= 100
        // This is a simplified approach - in practice, you might need more context
        if (amount.compareTo(BigDecimal.valueOf(100)) <= 0) {
            return new DiscountPercentage(amount.doubleValue());
        } else {
            // For fixed amount discounts, create DiscountAmount
            return new DiscountAmount(new Money(amount));
        }
    }

    private BigDecimal convertDiscountToBigDecimal(Discount discount) {
        if (discount == null) {
            return null;
        }
        
        if (discount instanceof DiscountPercentage) {
            return ((DiscountPercentage) discount).getValue();
        } else if (discount instanceof DiscountAmount) {
            return ((DiscountAmount) discount).getValue();
        }
        
        return BigDecimal.ZERO;
    }

    private DateRange convertStringToDateRange(String dateRangeStr) {
        if (dateRangeStr == null || dateRangeStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Parse string like "30 DAYS" into DateRange
            String[] parts = dateRangeStr.trim().split("\\s+");
            if (parts.length == 2) {
                int value = Integer.parseInt(parts[0]);
                ChronoUnit unit = ChronoUnit.valueOf(parts[1].toUpperCase());
                return new DateRange(value, unit);
            }
        } catch (Exception e) {
            // Log error or handle gracefully
        }
        
        return null;
    }

    private String convertDateRangeToString(DateRange dateRange) {
        if (dateRange == null) {
            return null;
        }
        
        // Convert DateRange to string like "30 DAYS"
        return dateRange.getValue() + " " + dateRange.getUnit().name();
    }

    private VoucherPackStatus convertJpaStatusToDomainStatus(String jpaStatus) {
        if (jpaStatus == null) {
            return null;
        }
        
        try {
            return VoucherPackStatus.valueOf(jpaStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            return VoucherPackStatus.PENDING; // Default fallback
        }
    }

    private String convertDomainStatusToJpaStatus(VoucherPackStatus domainStatus) {
        if (domainStatus == null) {
            return null;
        }
        
        return domainStatus.name();
    }
}




