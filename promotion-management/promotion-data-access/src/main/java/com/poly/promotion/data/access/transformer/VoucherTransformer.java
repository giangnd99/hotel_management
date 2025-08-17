package com.poly.promotion.data.access.transformer;

import com.poly.promotion.data.access.jpaentity.VoucherJpaEntity;
import com.poly.promotion.domain.core.entity.Voucher;
import com.poly.promotion.domain.core.valueobject.*;
import com.poly.domain.valueobject.CustomerId;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * <h2>VoucherTransformer Interface</h2>
 * 
 * <p>MapStruct transformer interface for converting between Voucher domain entities
 * and VoucherJpaEntity JPA entities. This interface provides type-safe mapping
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
 *   <li>ID: String ↔ VoucherId</li>
 *   <li>Status: String enum ↔ VoucherStatus</li>
 *   <li>Timestamps: LocalDateTime ↔ LocalDateTime</li>
 *   <li>Value Objects: Automatic conversion</li>
 *   <li>Relationships: VoucherPack reference</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see Voucher
 * @see VoucherJpaEntity
 * @see org.mapstruct.Mapper
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = {
        VoucherIdMapper.class,
        VoucherStatusMapper.class,
        DiscountMapper.class,
        VoucherPackIdMapper.class,
        CustomerIdMapper.class
    }
)
public interface VoucherTransformer {

    /**
     * Transforms a VoucherJpaEntity to a Voucher domain entity.
     * 
     * <p>This method converts a JPA entity to its corresponding domain entity,
     * including all nested value objects and business logic components.</p>
     * 
     * @param jpaEntity the JPA entity to transform
     * @return the corresponding domain entity
     */
    @Mapping(target = "voucherCode", source = "voucherCode")
    @Mapping(target = "discount", source = "discountAmount", qualifiedByName = "bigDecimalToDiscount")
    @Mapping(target = "voucherPackId", source = "voucherPack.id", qualifiedByName = "longToVoucherPackId")
    @Mapping(target = "customerId", source = "customerId", qualifiedByName = "stringToCustomerId")
    @Mapping(target = "redeemedAt", source = "redeemedAt")
    @Mapping(target = "validTo", source = "validTo")
    @Mapping(target = "voucherStatus", source = "status", qualifiedByName = "jpaStatusToDomainStatus")
    Voucher toDomainEntity(VoucherJpaEntity jpaEntity);

    /**
     * Transforms a Voucher domain entity to a VoucherJpaEntity.
     * 
     * <p>This method converts a domain entity to its corresponding JPA entity,
     * preparing it for database persistence operations.</p>
     * 
     * @param domainEntity the domain entity to transform
     * @return the corresponding JPA entity
     */
    @Mapping(target = "id", source = "id", qualifiedByName = "voucherIdToString")
    @Mapping(target = "voucherCode", source = "voucherCode")
    @Mapping(target = "discountAmount", source = "discount", qualifiedByName = "discountToBigDecimal")
    @Mapping(target = "voucherPack", ignore = true) // Will be set separately
    @Mapping(target = "customerId", source = "customerId", qualifiedByName = "customerIdToString")
    @Mapping(target = "redeemedAt", source = "redeemedAt")
    @Mapping(target = "validTo", source = "validTo")
    @Mapping(target = "status", source = "voucherStatus", qualifiedByName = "domainStatusToJpaStatus")
    @Mapping(target = "usedAt", ignore = true) // Voucher entity doesn't have usedAt
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    VoucherJpaEntity toJpaEntity(Voucher domainEntity);

    /**
     * Transforms a list of VoucherJpaEntity to a list of Voucher domain entities.
     * 
     * <p>This method provides bulk transformation for collections, useful for
     * retrieving multiple vouchers from the database.</p>
     * 
     * @param jpaEntities the list of JPA entities to transform
     * @return the corresponding list of domain entities
     */
    List<Voucher> toDomainEntities(List<VoucherJpaEntity> jpaEntities);

    /**
     * Transforms a list of Voucher domain entities to a list of VoucherJpaEntity.
     * 
     * <p>This method provides bulk transformation for collections, useful for
     * persisting multiple vouchers to the database.</p>
     * 
     * @param domainEntities the list of domain entities to transform
     * @return the corresponding list of JPA entities
     */
    List<VoucherJpaEntity> toJpaEntities(List<Voucher> domainEntities);

    /**
     * Updates an existing JPA entity with values from a domain entity.
     * 
     * <p>This method is used for updating existing vouchers, preserving
     * certain fields like ID, version, and timestamps while updating others.</p>
     * 
     * @param domainEntity the domain entity containing updated values
     * @param jpaEntity the existing JPA entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "voucherPack", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "voucherCode", source = "voucherCode")
    @Mapping(target = "discountAmount", source = "discount", qualifiedByName = "discountToBigDecimal")
    @Mapping(target = "customerId", source = "customerId", qualifiedByName = "customerIdToString")
    @Mapping(target = "redeemedAt", source = "redeemedAt")
    @Mapping(target = "validTo", source = "validTo")
    @Mapping(target = "status", source = "voucherStatus", qualifiedByName = "domainStatusToJpaStatus")
    void updateJpaEntity(@MappingTarget VoucherJpaEntity jpaEntity, Voucher domainEntity);
}

/**
 * <h2>VoucherIdMapper Class</h2>
 * 
 * <p>Custom mapper for converting between VoucherId value objects and String primitives.
 * This class handles the conversion between domain value objects and database primitives.</p>
 */
@Mapper(componentModel = "spring")
abstract class VoucherIdMapper {

    /**
     * Converts a VoucherId value object to a String primitive.
     * 
     * @param voucherId the VoucherId value object
     * @return the corresponding String value
     */
    @Named("voucherIdToString")
    public String voucherIdToString(VoucherId voucherId) {
        return voucherId != null ? voucherId.getValue().toString() : null;
    }

    /**
     * Converts a String primitive to a VoucherId value object.
     * 
     * @param id the String value
     * @return the corresponding VoucherId value object
     */
    @Named("stringToVoucherId")
    public VoucherId stringToVoucherId(String id) {
        return id != null ? new VoucherId(UUID.fromString(id)) : null;
    }
}

/**
 * <h2>VoucherStatusMapper Class</h2>
 * 
 * <p>Custom mapper for converting between VoucherStatus domain enums and JPA enums.
 * This class handles the conversion between domain and persistence layer status representations.</p>
 */
@Mapper(componentModel = "spring")
abstract class VoucherStatusMapper {

    /**
     * Converts a VoucherStatus domain enum to a JPA enum.
     * 
     * @param domainStatus the domain VoucherStatus
     * @return the corresponding JPA VoucherStatus
     */
    @Named("domainStatusToJpaStatus")
    public VoucherJpaEntity.VoucherStatus domainStatusToJpaStatus(VoucherStatus domainStatus) {
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

    /**
     * Converts a JPA VoucherStatus enum to a domain enum.
     * 
     * @param jpaStatus the JPA VoucherStatus
     * @return the corresponding domain VoucherStatus
     */
    @Named("jpaStatusToDomainStatus")
    public VoucherStatus jpaStatusToDomainStatus(VoucherJpaEntity.VoucherStatus jpaStatus) {
        if (jpaStatus == null) {
            return null;
        }
        
        switch (jpaStatus) {
            case PENDING:
                return VoucherStatus.PENDING;
            case REDEEMED:
                return VoucherStatus.REDEEMED;
            case USED:
                return VoucherStatus.USED;
            case EXPIRED:
                return VoucherStatus.EXPIRED;
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
 * <h2>CustomerIdMapper Class</h2>
 * 
 * <p>Custom mapper for converting between CustomerId value objects and String primitives.
 * This class handles the conversion between domain value objects and database primitives.</p>
 */
@Mapper(componentModel = "spring")
abstract class CustomerIdMapper {

    /**
     * Converts a CustomerId value object to a String primitive.
     * 
     * @param customerId the CustomerId value object
     * @return the corresponding String value
     */
    @Named("customerIdToString")
    public String customerIdToString(CustomerId customerId) {
        return customerId != null ? customerId.getValue().toString() : null;
    }

    /**
     * Converts a String primitive to a CustomerId value object.
     * 
     * @param id the String value
     * @return the corresponding CustomerId value object
     */
    @Named("stringToCustomerId")
    public CustomerId stringToCustomerId(String id) {
        return id != null ? new CustomerId(UUID.fromString(id)) : null;
    }
}
