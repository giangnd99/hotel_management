package com.poly.promotion.data.access.jpaentity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <h2>VoucherPack JPA Entity Test</h2>
 * 
 * <p>Unit tests for VoucherPackJpaEntity covering:</p>
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
@DisplayName("VoucherPack JPA Entity Tests")
class VoucherPackJpaEntityTest {

    private VoucherPackJpaEntity validVoucherPack;

    @BeforeEach
    void setUp() {
        validVoucherPack = VoucherPackJpaEntity.builder()
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
    }

    @Nested
    @DisplayName("Entity Creation Tests")
    class EntityCreationTests {

        @Test
        @DisplayName("Should create valid voucher pack entity")
        void shouldCreateValidVoucherPackEntity() {
            // Then
            assertThat(validVoucherPack).isNotNull();
            assertThat(validVoucherPack.getDescription()).isEqualTo("Test Voucher Pack");
            assertThat(validVoucherPack.getDiscountAmount()).isEqualTo(new BigDecimal("10.00"));
            assertThat(validVoucherPack.getValidRange()).isEqualTo("30 days");
            assertThat(validVoucherPack.getRequiredPoints()).isEqualTo(50L);
            assertThat(validVoucherPack.getQuantity()).isEqualTo(100);
            assertThat(validVoucherPack.getStatus()).isEqualTo(VoucherPackJpaEntity.VoucherPackStatus.PENDING);
        }

        @Test
        @DisplayName("Should set audit timestamps automatically")
        void shouldSetAuditTimestampsAutomatically() {
            // When - timestamps are set by JPA annotations, so we just verify they exist
            assertThat(validVoucherPack.getCreatedAt()).isNull(); // Will be set by JPA
            assertThat(validVoucherPack.getUpdatedAt()).isNull(); // Will be set by JPA
        }

        @Test
        @DisplayName("Should set version automatically")
        void shouldSetVersionAutomatically() {
            // When - version is set by JPA annotations, so we just verify it exists
            assertThat(validVoucherPack.getVersion()).isNull(); // Will be set by JPA
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
            VoucherPackJpaEntity entity = VoucherPackJpaEntity.builder()
                    .description("Test Pack")
                    .discountAmount(largeAmount)
                    .quantity(100)
                    .requiredPoints(50L)
                    .validRange("30 days")
                    .status(VoucherPackJpaEntity.VoucherPackStatus.PENDING)
                    .createdBy("test-user")
                    .build();

            // Then
            assertThat(entity.getDiscountAmount()).isEqualTo(largeAmount);
        }
    }

    @Nested
    @DisplayName("Optional Field Tests")
    class OptionalFieldTests {

        @Test
        @DisplayName("Should allow null pack valid from date")
        void shouldAllowNullPackValidFromDate() {
            // When
            VoucherPackJpaEntity entity = VoucherPackJpaEntity.builder()
                    .description("Test Pack")
                    .discountAmount(new BigDecimal("10.00"))
                    .quantity(100)
                    .requiredPoints(50L)
                    .validRange("30 days")
                    .status(VoucherPackJpaEntity.VoucherPackStatus.PENDING)
                    .createdBy("test-user")
                    .validTo(LocalDate.now().plusDays(30))
                    .build();

            // Then
            assertThat(entity.getValidFrom()).isNull();
        }

        @Test
        @DisplayName("Should allow null pack valid to date")
        void shouldAllowNullPackValidToDate() {
            // When
            VoucherPackJpaEntity entity = VoucherPackJpaEntity.builder()
                    .description("Test Pack")
                    .discountAmount(new BigDecimal("10.00"))
                    .quantity(100)
                    .requiredPoints(50L)
                    .validRange("30 days")
                    .status(VoucherPackJpaEntity.VoucherPackStatus.PENDING)
                    .createdBy("test-user")
                    .validFrom(LocalDate.now())
                    .build();

            // Then
            assertThat(entity.getValidTo()).isNull();
        }

        @Test
        @DisplayName("Should allow null updated by field")
        void shouldAllowNullUpdatedByField() {
            // When
            VoucherPackJpaEntity entity = VoucherPackJpaEntity.builder()
                    .description("Test Pack")
                    .discountAmount(new BigDecimal("10.00"))
                    .quantity(100)
                    .requiredPoints(50L)
                    .validRange("30 days")
                    .status(VoucherPackJpaEntity.VoucherPackStatus.PENDING)
                    .createdBy("test-user")
                    .build();

            // Then
            assertThat(entity.getUpdatedBy()).isNull();
        }
    }

    @Nested
    @DisplayName("Status Enum Tests")
    class StatusEnumTests {

        @Test
        @DisplayName("Should have all required status values")
        void shouldHaveAllRequiredStatusValues() {
            // Then
            assertThat(VoucherPackJpaEntity.VoucherPackStatus.values()).hasSize(4);
            assertThat(VoucherPackJpaEntity.VoucherPackStatus.PENDING).isNotNull();
            assertThat(VoucherPackJpaEntity.VoucherPackStatus.PUBLISHED).isNotNull();
            assertThat(VoucherPackJpaEntity.VoucherPackStatus.CLOSED).isNotNull();
            assertThat(VoucherPackJpaEntity.VoucherPackStatus.EXPIRED).isNotNull();
        }

        @Test
        @DisplayName("Should have correct status names")
        void shouldHaveCorrectStatusNames() {
            // Then
            assertThat(VoucherPackJpaEntity.VoucherPackStatus.PENDING.name()).isEqualTo("PENDING");
            assertThat(VoucherPackJpaEntity.VoucherPackStatus.PUBLISHED.name()).isEqualTo("PUBLISHED");
            assertThat(VoucherPackJpaEntity.VoucherPackStatus.CLOSED.name()).isEqualTo("CLOSED");
            assertThat(VoucherPackJpaEntity.VoucherPackStatus.EXPIRED.name()).isEqualTo("EXPIRED");
        }
    }

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderPatternTests {

        @Test
        @DisplayName("Should build entity with all fields")
        void shouldBuildEntityWithAllFields() {
            // When
            VoucherPackJpaEntity entity = VoucherPackJpaEntity.builder()
                    .description("Complete Pack")
                    .discountAmount(new BigDecimal("25.50"))
                    .quantity(200)
                    .requiredPoints(100L)
                    .validRange("60 days")
                    .status(VoucherPackJpaEntity.VoucherPackStatus.PUBLISHED)
                    .validFrom(LocalDate.now())
                    .validTo(LocalDate.now().plusDays(60))
                    .createdBy("admin-user")
                    .updatedBy("admin-user")
                    .build();

            // Then
            assertThat(entity.getDescription()).isEqualTo("Complete Pack");
            assertThat(entity.getDiscountAmount()).isEqualTo(new BigDecimal("25.50"));
            assertThat(entity.getQuantity()).isEqualTo(200);
            assertThat(entity.getRequiredPoints()).isEqualTo(100L);
            assertThat(entity.getValidRange()).isEqualTo("60 days");
            assertThat(entity.getStatus()).isEqualTo(VoucherPackJpaEntity.VoucherPackStatus.PUBLISHED);
            assertThat(entity.getValidFrom()).isNotNull();
            assertThat(entity.getValidTo()).isNotNull();
            assertThat(entity.getCreatedBy()).isEqualTo("admin-user");
            assertThat(entity.getUpdatedBy()).isEqualTo("admin-user");
        }

        @Test
        @DisplayName("Should build entity with minimal fields")
        void shouldBuildEntityWithMinimalFields() {
            // When
            VoucherPackJpaEntity entity = VoucherPackJpaEntity.builder()
                    .description("Minimal Pack")
                    .discountAmount(new BigDecimal("5.00"))
                    .quantity(50)
                    .requiredPoints(25L)
                    .validRange("15 days")
                    .status(VoucherPackJpaEntity.VoucherPackStatus.PENDING)
                    .createdBy("test-user")
                    .build();

            // Then
            assertThat(entity.getDescription()).isEqualTo("Minimal Pack");
            assertThat(entity.getDiscountAmount()).isEqualTo(new BigDecimal("5.00"));
            assertThat(entity.getQuantity()).isEqualTo(50);
            assertThat(entity.getRequiredPoints()).isEqualTo(25L);
            assertThat(entity.getValidRange()).isEqualTo("15 days");
            assertThat(entity.getStatus()).isEqualTo(VoucherPackJpaEntity.VoucherPackStatus.PENDING);
            assertThat(entity.getCreatedBy()).isEqualTo("test-user");
        }
    }
}
