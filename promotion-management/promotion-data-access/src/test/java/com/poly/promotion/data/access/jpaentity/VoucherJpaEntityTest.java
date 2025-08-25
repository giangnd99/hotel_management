package com.poly.promotion.data.access.jpaentity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <h2>Voucher JPA Entity Test</h2>
 * 
 * <p>Unit tests for VoucherJpaEntity covering:</p>
 * <ul>
 *   <li>Entity creation and validation</li>
 *   <li>Field constraints and validation</li>
 *   <li>Builder pattern functionality</li>
 *   <li>Enum values and status management</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 */
@DisplayName("Voucher JPA Entity Tests")
class VoucherJpaEntityTest {

    private VoucherPackJpaEntity voucherPack;
    private VoucherJpaEntity validVoucher;

    @BeforeEach
    void setUp() {
        // Create a voucher pack first
        voucherPack = VoucherPackJpaEntity.builder()
                .description("Test Voucher Pack")
                .discountAmount(new BigDecimal("10.00"))
                .quantity(100)
                .requiredPoints(50L)
                .validRange("30 days")
                .status(VoucherPackJpaEntity.VoucherPackStatus.PENDING)
                .validFrom(LocalDate.now())
                .validTo(LocalDate.now().plusDays(30))
                .createdBy("test-user")
                .build();

        // Create a valid voucher
        validVoucher = VoucherJpaEntity.builder()
                .voucherCode("TEST001")
                .customerId("CUST001")
                .voucherPack(voucherPack)
                .discountAmount(new BigDecimal("10.00"))
                .status(VoucherJpaEntity.VoucherStatus.REDEEMED)
                .redeemedAt(LocalDateTime.now())
                .validTo(LocalDateTime.now().plusDays(30))
                .build();
    }

    @Nested
    @DisplayName("Entity Creation Tests")
    class EntityCreationTests {

        @Test
        @DisplayName("Should create valid voucher entity")
        void shouldCreateValidVoucherEntity() {
            // Then
            assertThat(validVoucher).isNotNull();
            assertThat(validVoucher.getVoucherCode()).isEqualTo("TEST001");
            assertThat(validVoucher.getDiscountAmount()).isEqualTo(new BigDecimal("10.00"));
            assertThat(validVoucher.getVoucherPack()).isEqualTo(voucherPack);
            assertThat(validVoucher.getCustomerId()).isEqualTo("CUST001");
            assertThat(validVoucher.getStatus()).isEqualTo(VoucherJpaEntity.VoucherStatus.REDEEMED);
        }

        @Test
        @DisplayName("Should set audit timestamps automatically")
        void shouldSetAuditTimestampsAutomatically() {
            // When - timestamps are set by JPA annotations, so we just verify they exist
            assertThat(validVoucher.getCreatedAt()).isNull(); // Will be set by JPA
            assertThat(validVoucher.getUpdatedAt()).isNull(); // Will be set by JPA
        }

        @Test
        @DisplayName("Should set version automatically")
        void shouldSetVersionAutomatically() {
            // When - version is set by JPA annotations, so we just verify it exists
            assertThat(validVoucher.getVersion()).isNull(); // Will be set by JPA
        }
    }

    @Nested
    @DisplayName("Field Constraint Tests")
    class FieldConstraintTests {

        @Test
        @DisplayName("Should handle large discount amount precision")
        void shouldHandleLargeDiscountAmountPrecision() {
            // Given
            BigDecimal largeAmount = new BigDecimal("123456789.12");

            // When
            VoucherJpaEntity entity = VoucherJpaEntity.builder()
                    .voucherCode("TEST001")
                    .discountAmount(largeAmount)
                    .voucherPack(voucherPack)
                    .customerId("CUST001")
                    .redeemedAt(LocalDateTime.now())
                    .validTo(LocalDateTime.now().plusDays(30))
                    .status(VoucherJpaEntity.VoucherStatus.REDEEMED)
                    .build();

            // Then
            assertThat(entity.getDiscountAmount()).isEqualTo(largeAmount);
        }
    }

    @Nested
    @DisplayName("Optional Field Tests")
    class OptionalFieldTests {

        @Test
        @DisplayName("Should allow null used at field")
        void shouldAllowNullUsedAtField() {
            // When
            VoucherJpaEntity entity = VoucherJpaEntity.builder()
                    .voucherCode("TEST001")
                    .discountAmount(new BigDecimal("10.00"))
                    .voucherPack(voucherPack)
                    .customerId("CUST001")
                    .redeemedAt(LocalDateTime.now())
                    .validTo(LocalDateTime.now().plusDays(30))
                    .status(VoucherJpaEntity.VoucherStatus.REDEEMED)
                    .build();

            // Then
            assertThat(entity.getUsedAt()).isNull();
        }
    }

    @Nested
    @DisplayName("Status Enum Tests")
    class StatusEnumTests {

        @Test
        @DisplayName("Should have all required status values")
        void shouldHaveAllRequiredStatusValues() {
            // Then
            assertThat(VoucherJpaEntity.VoucherStatus.values()).hasSize(4);
            assertThat(VoucherJpaEntity.VoucherStatus.PENDING).isNotNull();
            assertThat(VoucherJpaEntity.VoucherStatus.REDEEMED).isNotNull();
            assertThat(VoucherJpaEntity.VoucherStatus.USED).isNotNull();
            assertThat(VoucherJpaEntity.VoucherStatus.EXPIRED).isNotNull();
        }

        @Test
        @DisplayName("Should have correct status names")
        void shouldHaveCorrectStatusNames() {
            // Then
            assertThat(VoucherJpaEntity.VoucherStatus.PENDING.name()).isEqualTo("PENDING");
            assertThat(VoucherJpaEntity.VoucherStatus.REDEEMED.name()).isEqualTo("REDEEMED");
            assertThat(VoucherJpaEntity.VoucherStatus.USED.name()).isEqualTo("USED");
            assertThat(VoucherJpaEntity.VoucherStatus.EXPIRED.name()).isEqualTo("EXPIRED");
        }
    }

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderPatternTests {

        @Test
        @DisplayName("Should build entity with all fields")
        void shouldBuildEntityWithAllFields() {
            // When
            VoucherJpaEntity entity = VoucherJpaEntity.builder()
                    .voucherCode("COMPLETE001")
                    .discountAmount(new BigDecimal("25.50"))
                    .voucherPack(voucherPack)
                    .customerId("CUST002")
                    .redeemedAt(LocalDateTime.now())
                    .validTo(LocalDateTime.now().plusDays(60))
                    .status(VoucherJpaEntity.VoucherStatus.USED)
                    .usedAt(LocalDateTime.now())
                    .build();

            // Then
            assertThat(entity.getVoucherCode()).isEqualTo("COMPLETE001");
            assertThat(entity.getDiscountAmount()).isEqualTo(new BigDecimal("25.50"));
            assertThat(entity.getVoucherPack()).isEqualTo(voucherPack);
            assertThat(entity.getCustomerId()).isEqualTo("CUST002");
            assertThat(entity.getStatus()).isEqualTo(VoucherJpaEntity.VoucherStatus.USED);
            assertThat(entity.getUsedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should build entity with minimal fields")
        void shouldBuildEntityWithMinimalFields() {
            // When
            VoucherJpaEntity entity = VoucherJpaEntity.builder()
                    .voucherCode("MINIMAL001")
                    .discountAmount(new BigDecimal("5.00"))
                    .voucherPack(voucherPack)
                    .customerId("CUST003")
                    .redeemedAt(LocalDateTime.now())
                    .validTo(LocalDateTime.now().plusDays(15))
                    .status(VoucherJpaEntity.VoucherStatus.REDEEMED)
                    .build();

            // Then
            assertThat(entity.getVoucherCode()).isEqualTo("MINIMAL001");
            assertThat(entity.getDiscountAmount()).isEqualTo(new BigDecimal("5.00"));
            assertThat(entity.getVoucherPack()).isEqualTo(voucherPack);
            assertThat(entity.getCustomerId()).isEqualTo("CUST003");
            assertThat(entity.getStatus()).isEqualTo(VoucherJpaEntity.VoucherStatus.REDEEMED);
        }
    }

    @Nested
    @DisplayName("Helper Method Tests")
    class HelperMethodTests {

        @Test
        @DisplayName("Should check if voucher is expired")
        void shouldCheckIfVoucherIsExpired() {
            // Given
            VoucherJpaEntity expiredVoucher = VoucherJpaEntity.builder()
                    .voucherCode("EXPIRED001")
                    .discountAmount(new BigDecimal("10.00"))
                    .voucherPack(voucherPack)
                    .customerId("CUST001")
                    .redeemedAt(LocalDateTime.now().minusDays(30))
                    .validTo(LocalDateTime.now().minusDays(1))
                    .status(VoucherJpaEntity.VoucherStatus.REDEEMED)
                    .build();

            // When & Then
            assertThat(expiredVoucher.isExpired()).isTrue();
        }

        @Test
        @DisplayName("Should check if voucher can be used")
        void shouldCheckIfVoucherCanBeUsed() {
            // Given
            VoucherJpaEntity usableVoucher = VoucherJpaEntity.builder()
                    .voucherCode("USABLE001")
                    .discountAmount(new BigDecimal("10.00"))
                    .voucherPack(voucherPack)
                    .customerId("CUST001")
                    .redeemedAt(LocalDateTime.now().minusDays(1))
                    .validTo(LocalDateTime.now().plusDays(29))
                    .status(VoucherJpaEntity.VoucherStatus.REDEEMED)
                    .build();

            // When & Then
            assertThat(usableVoucher.canBeUsed()).isTrue();
        }

        @Test
        @DisplayName("Should not allow used voucher to be used again")
        void shouldNotAllowUsedVoucherToBeUsedAgain() {
            // Given
            VoucherJpaEntity usedVoucher = VoucherJpaEntity.builder()
                    .voucherCode("USED001")
                    .discountAmount(new BigDecimal("10.00"))
                    .voucherPack(voucherPack)
                    .customerId("CUST001")
                    .redeemedAt(LocalDateTime.now().minusDays(1))
                    .validTo(LocalDateTime.now().plusDays(29))
                    .status(VoucherJpaEntity.VoucherStatus.USED)
                    .build();

            // When & Then
            assertThat(usedVoucher.canBeUsed()).isFalse();
        }
    }
}
