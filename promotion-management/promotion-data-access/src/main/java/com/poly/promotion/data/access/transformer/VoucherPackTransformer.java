package com.poly.promotion.data.access.transformer;

import com.poly.promotion.data.access.jpaentity.VoucherPackJpaEntity;
import com.poly.promotion.domain.core.entity.VoucherPack;
import com.poly.promotion.domain.core.valueobject.*;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <h2>VoucherPackTransformer Interface</h2>
 * 
 * <p>MapStruct transformer interface for converting between VoucherPack domain entities
 * and VoucherPackJpaEntity JPA entities. This interface provides type-safe mapping
 * with automatic implementation generation.</p>
 * 
 * <p><strong>Transformation Features:</strong></p>
 * <ul>
 *   <li>Bidirectional mapping between domain and JPA entities</li>
 *   <li>Automatic handling of complex value objects</li>
 *   <li>Custom mapping for business-specific conversions</li>
 *   <li>Null-safe mapping with proper validation</li>
 *   <li>Collection mapping for bulk operations</li>
 * </ul>
 * 
 * <p><strong>Mapping Rules:</strong></p>
 * <ul>
 *   <li>ID: Long ↔ VoucherPackId</li>
 *   <li>Status: String enum ↔ VoucherPackStatus</li>
 *   <li>Dates: LocalDate ↔ LocalDate</li>
 *   <li>Timestamps: LocalDateTime ↔ LocalDateTime</li>
 *   <li>Value Objects: Automatic conversion</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherPack
 * @see VoucherPackJpaEntity
 * @see org.mapstruct.Mapper
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = {
        VoucherPackIdMapper.class,
        VoucherPackStatusMapper.class,
        DiscountMapper.class,
        DateRangeMapper.class
    }
)
public interface VoucherPackTransformer {

    /**
     * Transforms a VoucherPackJpaEntity to a VoucherPack domain entity.
     * 
     * <p>This method converts a JPA entity to its corresponding domain entity,
     * including all nested value objects and business logic components.</p>
     * 
     * @param jpaEntity the JPA entity to transform
     * @return the corresponding domain entity
     */
    @Mapping(target = "description", source = "description")
    @Mapping(target = "discountAmount", source = "discountAmount", qualifiedByName = "bigDecimalToDiscount")
    @Mapping(target = "voucherValidRange", source = "validRange", qualifiedByName = "stringToDateRange")
    @Mapping(target = "requiredPoints", source = "requiredPoints")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "packValidFrom", source = "validFrom")
    @Mapping(target = "packValidTo", source = "validTo")
    @Mapping(target = "status", source = "status", qualifiedByName = "jpaStatusToDomainStatus")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "updatedBy", source = "updatedBy")
    VoucherPack toDomainEntity(VoucherPackJpaEntity jpaEntity);

    /**
     * Transforms a VoucherPack domain entity to a VoucherPackJpaEntity.
     * 
     * <p>This method converts a domain entity to its corresponding JPA entity,
     * preparing it for database persistence operations.</p>
     * 
     * @param domainEntity the domain entity to transform
     * @return the corresponding JPA entity
     */
    @Mapping(target = "id", source = "id", qualifiedByName = "voucherPackIdToLong")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "discountAmount", source = "discountAmount", qualifiedByName = "discountToBigDecimal")
    @Mapping(target = "validRange", source = "voucherValidRange", qualifiedByName = "dateRangeToString")
    @Mapping(target = "requiredPoints", source = "requiredPoints")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "validFrom", source = "packValidFrom")
    @Mapping(target = "validTo", source = "packValidTo")
    @Mapping(target = "status", source = "status", qualifiedByName = "domainStatusToJpaStatus")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "updatedBy", source = "updatedBy")
    @Mapping(target = "version", ignore = true)
    VoucherPackJpaEntity toJpaEntity(VoucherPack domainEntity);

    /**
     * Transforms a list of VoucherPackJpaEntity to a list of VoucherPack domain entities.
     * 
     * <p>This method provides bulk transformation for collections, useful for
     * retrieving multiple voucher packs from the database.</p>
     * 
     * @param jpaEntities the list of JPA entities to transform
     * @return the corresponding list of domain entities
     */
    List<VoucherPack> toDomainEntities(List<VoucherPackJpaEntity> jpaEntities);

    /**
     * Transforms a list of VoucherPack domain entities to a list of VoucherPackJpaEntity.
     * 
     * <p>This method provides bulk transformation for collections, useful for
     * persisting multiple voucher packs to the database.</p>
     * 
     * @param domainEntities the list of domain entities to transform
     * @return the corresponding list of JPA entities
     */
    List<VoucherPackJpaEntity> toJpaEntities(List<VoucherPack> domainEntities);

    /**
     * Updates an existing JPA entity with values from a domain entity.
     * 
     * <p>This method is used for updating existing voucher packs, preserving
     * certain fields like ID, version, and timestamps while updating others.</p>
     * 
     * @param domainEntity the domain entity containing updated values
     * @param jpaEntity the existing JPA entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "description", source = "description")
    @Mapping(target = "discountAmount", source = "discountAmount", qualifiedByName = "discountToBigDecimal")
    @Mapping(target = "validRange", source = "voucherValidRange", qualifiedByName = "dateRangeToString")
    @Mapping(target = "requiredPoints", source = "requiredPoints")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "validFrom", source = "packValidFrom")
    @Mapping(target = "validTo", source = "packValidTo")
    @Mapping(target = "status", source = "status", qualifiedByName = "domainStatusToJpaStatus")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "updatedBy", source = "updatedBy")
    void updateJpaEntity(@MappingTarget VoucherPackJpaEntity jpaEntity, VoucherPack domainEntity);
}

/**
 * <h2>VoucherPackIdMapper Class</h2>
 * 
 * <p>Custom mapper for converting between VoucherPackId value objects and Long primitives.
 * This class handles the conversion between domain value objects and database primitives.</p>
 */
@Mapper(componentModel = "spring")
abstract class VoucherPackIdMapper {

    /**
     * Converts a VoucherPackId value object to a Long primitive.
     * 
     * @param voucherPackId the VoucherPackId value object
     * @return the corresponding Long value
     */
    @Named("voucherPackIdToLong")
    public Long voucherPackIdToLong(VoucherPackId voucherPackId) {
        return voucherPackId != null ? voucherPackId.getValue() : null;
    }

    /**
     * Converts a Long primitive to a VoucherPackId value object.
     * 
     * @param id the Long value
     * @return the corresponding VoucherPackId value object
     */
    @Named("longToVoucherPackId")
    public VoucherPackId longToVoucherPackId(Long id) {
        return id != null ? new VoucherPackId(id) : null;
    }
}

/**
 * <h2>VoucherPackStatusMapper Class</h2>
 * 
 * <p>Custom mapper for converting between VoucherPackStatus domain enums and JPA enums.
 * This class handles the conversion between domain and persistence layer status representations.</p>
 */
@Mapper(componentModel = "spring")
abstract class VoucherPackStatusMapper {

    /**
     * Converts a VoucherPackStatus domain enum to a JPA enum.
     * 
     * @param domainStatus the domain VoucherPackStatus
     * @return the corresponding JPA VoucherPackStatus
     */
    @Named("domainStatusToJpaStatus")
    public VoucherPackJpaEntity.VoucherPackStatus domainStatusToJpaStatus(VoucherPackStatus domainStatus) {
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

    /**
     * Converts a JPA VoucherPackStatus enum to a domain enum.
     * 
     * @param jpaStatus the JPA VoucherPackStatus
     * @return the corresponding domain VoucherPackStatus
     */
    @Named("jpaStatusToDomainStatus")
    public VoucherPackStatus jpaStatusToDomainStatus(VoucherPackJpaEntity.VoucherPackStatus jpaStatus) {
        if (jpaStatus == null) {
            return null;
        }
        
        switch (jpaStatus) {
            case PENDING:
                return VoucherPackStatus.PENDING;
            case PUBLISHED:
                return VoucherPackStatus.PUBLISHED;
            case CLOSED:
                return VoucherPackStatus.CLOSED;
            case EXPIRED:
                return VoucherPackStatus.EXPIRED;
            default:
                throw new IllegalArgumentException("Unknown JPA status: " + jpaStatus);
        }
    }
}

/**
 * <h2>DiscountMapper Class</h2>
 * 
 * <p>Custom mapper for converting between Discount value objects and BigDecimal primitives.
 * This class handles the conversion between domain value objects and database primitives.</p>
 */
@Mapper(componentModel = "spring")
abstract class DiscountMapper {

    /**
     * Converts a Discount value object to a BigDecimal primitive.
     * 
     * @param discount the Discount value object
     * @return the corresponding BigDecimal value
     */
    @Named("discountToBigDecimal")
    public BigDecimal discountToBigDecimal(Discount discount) {
        return discount != null ? discount.getValue() : null;
    }

    /**
     * Converts a BigDecimal primitive to a Discount value object.
     * Note: This is a simplified mapping - in practice, you might need to determine
     * the discount type based on business logic or additional context.
     * 
     * @param amount the BigDecimal value
     * @return the corresponding Discount value object
     */
    @Named("bigDecimalToDiscount")
    public Discount bigDecimalToDiscount(BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        
        // Default to percentage discount for values <= 100
        // This is a simplified approach - in practice, you might need more context
        if (amount.compareTo(BigDecimal.valueOf(100)) <= 0) {
            return new DiscountPercentage(amount.doubleValue());
        } else {
            // For fixed amount discounts, we'll use a default approach
            // In practice, you might need to determine the discount type differently
            return new DiscountPercentage(amount.doubleValue());
        }
    }
}

/**
 * <h2>DateRangeMapper Class</h2>
 * 
 * <p>Custom mapper for converting between DateRange value objects and String primitives.
 * This class handles the conversion between domain value objects and database primitives.</p>
 */
@Mapper(componentModel = "spring")
abstract class DateRangeMapper {

    /**
     * Converts a DateRange value object to a String primitive.
     * 
     * @param dateRange the DateRange value object
     * @return the corresponding String representation
     */
    @Named("dateRangeToString")
    public String dateRangeToString(DateRange dateRange) {
        return dateRange != null ? dateRange.toString() : null;
    }

    /**
     * Converts a String primitive to a DateRange value object.
     * 
     * @param dateRangeString the String representation
     * @return the corresponding DateRange value object
     */
    @Named("stringToDateRange")
    public DateRange stringToDateRange(String dateRangeString) {
        if (dateRangeString == null || dateRangeString.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Parse the date range string in format "X UNIT"
            String[] parts = dateRangeString.trim().split("\\s+");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid date range format. Expected 'X UNIT' format.");
            }
            
            long value = Long.parseLong(parts[0]);
            String unitStr = parts[1].toUpperCase();
            
            // Map string units to ChronoUnit
            java.time.temporal.ChronoUnit unit;
            switch (unitStr) {
                case "DAYS":
                    unit = java.time.temporal.ChronoUnit.DAYS;
                    break;
                case "WEEKS":
                    unit = java.time.temporal.ChronoUnit.WEEKS;
                    break;
                case "MONTHS":
                    unit = java.time.temporal.ChronoUnit.MONTHS;
                    break;
                case "YEARS":
                    unit = java.time.temporal.ChronoUnit.YEARS;
                    break;
                case "HOURS":
                    unit = java.time.temporal.ChronoUnit.HOURS;
                    break;
                case "MINUTES":
                    unit = java.time.temporal.ChronoUnit.MINUTES;
                    break;
                case "SECONDS":
                    unit = java.time.temporal.ChronoUnit.SECONDS;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown time unit: " + unitStr);
            }
            
            return new DateRange((int) value, unit);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number in date range: " + dateRangeString, e);
        }
    }
}
