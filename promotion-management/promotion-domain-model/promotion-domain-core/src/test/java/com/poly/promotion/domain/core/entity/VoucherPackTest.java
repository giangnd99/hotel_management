package com.poly.promotion.domain.core.entity;

import com.poly.promotion.domain.core.exception.VoucherDomainException;
import com.poly.promotion.domain.core.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@DisplayName("VoucherPack Entity Tests")
class VoucherPackTest {

    @Mock
    private Discount mockDiscount;

    @Mock
    private DateRange mockDateRange;

    @Mock
    private VoucherPackId mockVoucherPackId;

    private static final BigDecimal VALID_DISCOUNT_AMOUNT = BigDecimal.valueOf(50.0);
    private static final Integer VALID_QUANTITY = 100;
    private static final Long VALID_REQUIRED_POINTS = 1000L;
    private static final LocalDate VALID_FROM_DATE = LocalDate.now().plusDays(1);
    private static final LocalDate VALID_TO_DATE = LocalDate.now().plusDays(31);

    @BeforeEach
    void setUp() {
        lenient().when(mockDiscount.getValue()).thenReturn(VALID_DISCOUNT_AMOUNT);
        lenient().when(mockDateRange.getValue()).thenReturn(30);
        lenient().when(mockDateRange.getUnit()).thenReturn(ChronoUnit.DAYS);
        lenient().when(mockVoucherPackId.getValue()).thenReturn(1L);
    }

    @Nested
    @DisplayName("calculateInitialStatus Method Tests")
    class CalculateInitialStatusTests {

        @Test
        @DisplayName("Should return PENDING when packValidFrom is in the future")
        void shouldReturnPendingWhenPackValidFromIsInTheFuture() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .packValidFrom(LocalDate.now().plusDays(1))
                    .status(VoucherPackStatus.PENDING)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When
            VoucherPackStatus status = voucherPack.calculateInitialStatus();

            // Then
            assertEquals(VoucherPackStatus.PENDING, status);
        }

        @Test
        @DisplayName("Should return PUBLISHED when packValidFrom is today")
        void shouldReturnPublishedWhenPackValidFromIsToday() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .packValidFrom(LocalDate.now())
                    .status(VoucherPackStatus.PENDING)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When
            VoucherPackStatus status = voucherPack.calculateInitialStatus();

            // Then
            assertEquals(VoucherPackStatus.PUBLISHED, status);
        }

        @Test
        @DisplayName("Should return PUBLISHED when packValidFrom is in the past")
        void shouldReturnPublishedWhenPackValidFromIsInThePast() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .packValidFrom(LocalDate.now().minusDays(1))
                    .status(VoucherPackStatus.PENDING)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When
            VoucherPackStatus status = voucherPack.calculateInitialStatus();

            // Then
            assertEquals(VoucherPackStatus.PUBLISHED, status);
        }

        @Test
        @DisplayName("Should return PUBLISHED when packValidFrom is null")
        void shouldReturnPublishedWhenPackValidFromIsNull() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .packValidFrom(null)
                    .status(VoucherPackStatus.PENDING)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When
            VoucherPackStatus status = voucherPack.calculateInitialStatus();

            // Then
            assertEquals(VoucherPackStatus.PUBLISHED, status);
        }
    }

    @Nested
    @DisplayName("shouldBePublished Method Tests")
    class ShouldBePublishedTests {

        @Test
        @DisplayName("Should return true when packValidFrom is today")
        void shouldReturnTrueWhenPackValidFromIsToday() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .packValidFrom(LocalDate.now())
                    .status(VoucherPackStatus.PENDING)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertTrue(voucherPack.shouldBePublished());
        }

        @Test
        @DisplayName("Should return true when packValidFrom is in the past")
        void shouldReturnTrueWhenPackValidFromIsInThePast() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .packValidFrom(LocalDate.now().minusDays(1))
                    .status(VoucherPackStatus.PENDING)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertTrue(voucherPack.shouldBePublished());
        }

        @Test
        @DisplayName("Should return false when packValidFrom is in the future")
        void shouldReturnFalseWhenPackValidFromIsInTheFuture() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .packValidFrom(LocalDate.now().plusDays(1))
                    .status(VoucherPackStatus.PENDING)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertFalse(voucherPack.shouldBePublished());
        }

        @Test
        @DisplayName("Should return false when already PUBLISHED")
        void shouldReturnFalseWhenAlreadyPublished() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .packValidFrom(LocalDate.now().minusDays(1))
                    .status(VoucherPackStatus.PUBLISHED)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertFalse(voucherPack.shouldBePublished());
        }
    }

    @Nested
    @DisplayName("shouldBeExpired Method Tests")
    class ShouldBeExpiredTests {

        @Test
        @DisplayName("Should return true when packValidTo is in the past")
        void shouldReturnTrueWhenPackValidToIsInThePast() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .packValidTo(LocalDate.now().minusDays(1))
                    .status(VoucherPackStatus.PUBLISHED)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertTrue(voucherPack.shouldBeExpired());
        }

        @Test
        @DisplayName("Should return false when packValidTo is today")
        void shouldReturnFalseWhenPackValidToIsToday() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .packValidTo(LocalDate.now())
                    .status(VoucherPackStatus.PUBLISHED)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertFalse(voucherPack.shouldBeExpired());
        }

        @Test
        @DisplayName("Should return false when packValidTo is in the future")
        void shouldReturnFalseWhenPackValidToIsInTheFuture() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .packValidTo(LocalDate.now().plusDays(1))
                    .status(VoucherPackStatus.PUBLISHED)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertFalse(voucherPack.shouldBeExpired());
        }

        @Test
        @DisplayName("Should return false when already EXPIRED")
        void shouldReturnFalseWhenAlreadyExpired() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .packValidTo(LocalDate.now().minusDays(1))
                    .status(VoucherPackStatus.EXPIRED)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertFalse(voucherPack.shouldBeExpired());
        }

        @Test
        @DisplayName("Should return false when packValidTo is null")
        void shouldReturnFalseWhenPackValidToIsNull() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .packValidTo(null)
                    .status(VoucherPackStatus.PUBLISHED)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertFalse(voucherPack.shouldBeExpired());
        }
    }

    @Nested
    @DisplayName("validatePackDates Method Tests")
    class ValidatePackDatesTests {

        @Test
        @DisplayName("Should not throw exception for valid date range")
        void shouldNotThrowExceptionForValidDateRange() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .packValidFrom(VALID_FROM_DATE)
                    .packValidTo(VALID_TO_DATE)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertDoesNotThrow(() -> voucherPack.validatePackDates());
        }

        @Test
        @DisplayName("Should not throw exception when packValidFrom is null")
        void shouldNotThrowExceptionWhenPackValidFromIsNull() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .packValidFrom(null)
                    .packValidTo(VALID_TO_DATE)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertDoesNotThrow(() -> voucherPack.validatePackDates());
        }

        @Test
        @DisplayName("Should not throw exception when packValidTo is null")
        void shouldNotThrowExceptionWhenPackValidToIsNull() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .packValidFrom(VALID_FROM_DATE)
                    .packValidTo(null)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertDoesNotThrow(() -> voucherPack.validatePackDates());
        }

        @Test
        @DisplayName("Should throw exception when packValidFrom is after packValidTo")
        void shouldThrowExceptionWhenPackValidFromIsAfterPackValidTo() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .packValidFrom(VALID_TO_DATE)
                    .packValidTo(VALID_FROM_DATE)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertThrows(VoucherDomainException.class, () -> voucherPack.validatePackDates());
        }
    }

    @Nested
    @DisplayName("validateQuantity Method Tests")
    class ValidateQuantityTests {

        @Test
        @DisplayName("Should not throw exception for valid quantity")
        void shouldNotThrowExceptionForValidQuantity() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .quantity(VALID_QUANTITY)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertDoesNotThrow(() -> voucherPack.validateQuantity());
        }

        @Test
        @DisplayName("Should throw exception for null quantity")
        void shouldThrowExceptionForNullQuantity() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .quantity(null)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertThrows(VoucherDomainException.class, () -> voucherPack.validateQuantity());
        }

        @Test
        @DisplayName("Should throw exception for zero quantity")
        void shouldThrowExceptionForZeroQuantity() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .quantity(0)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertThrows(VoucherDomainException.class, () -> voucherPack.validateQuantity());
        }

        @Test
        @DisplayName("Should throw exception for negative quantity")
        void shouldThrowExceptionForNegativeQuantity() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .quantity(-1)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertThrows(VoucherDomainException.class, () -> voucherPack.validateQuantity());
        }
    }

    @Nested
    @DisplayName("validateRequiredPoints Method Tests")
    class ValidateRequiredPointsTests {

        @Test
        @DisplayName("Should not throw exception for valid required points")
        void shouldNotThrowExceptionForValidRequiredPoints() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .requiredPoints(VALID_REQUIRED_POINTS)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertDoesNotThrow(() -> voucherPack.validateRequiredPoints());
        }

        @Test
        @DisplayName("Should throw exception for null required points")
        void shouldThrowExceptionForNullRequiredPoints() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .requiredPoints(null)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertThrows(VoucherDomainException.class, () -> voucherPack.validateRequiredPoints());
        }

        @Test
        @DisplayName("Should throw exception for zero required points")
        void shouldThrowExceptionForZeroRequiredPoints() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .requiredPoints(0L)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertThrows(VoucherDomainException.class, () -> voucherPack.validateRequiredPoints());
        }

        @Test
        @DisplayName("Should throw exception for negative required points")
        void shouldThrowExceptionForNegativeRequiredPoints() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .requiredPoints(-1L)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertThrows(VoucherDomainException.class, () -> voucherPack.validateRequiredPoints());
        }
    }

    @Nested
    @DisplayName("isActive Method Tests")
    class IsActiveTests {

        @Test
        @DisplayName("Should return true for active PUBLISHED pack")
        void shouldReturnTrueForActivePublishedPack() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .status(VoucherPackStatus.PUBLISHED)
                    .packValidFrom(LocalDate.now().minusDays(1))
                    .packValidTo(LocalDate.now().plusDays(1))
                    .quantity(10)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertTrue(voucherPack.isActive());
        }

        @Test
        @DisplayName("Should return false for PENDING status")
        void shouldReturnFalseForPendingStatus() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .status(VoucherPackStatus.PENDING)
                    .quantity(10)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertFalse(voucherPack.isActive());
        }

        @Test
        @DisplayName("Should return false for CLOSED status")
        void shouldReturnFalseForClosedStatus() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .status(VoucherPackStatus.CLOSED)
                    .quantity(10)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertFalse(voucherPack.isActive());
        }

        @Test
        @DisplayName("Should return false for EXPIRED status")
        void shouldReturnFalseForExpiredStatus() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .status(VoucherPackStatus.EXPIRED)
                    .quantity(10)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertFalse(voucherPack.isActive());
        }

        @Test
        @DisplayName("Should return false when packValidFrom is in the future")
        void shouldReturnFalseWhenPackValidFromIsInTheFuture() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .status(VoucherPackStatus.PUBLISHED)
                    .packValidFrom(LocalDate.now().plusDays(1))
                    .quantity(10)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertFalse(voucherPack.isActive());
        }

        @Test
        @DisplayName("Should return false when packValidTo is in the past")
        void shouldReturnFalseWhenPackValidToIsInThePast() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .status(VoucherPackStatus.PUBLISHED)
                    .packValidTo(LocalDate.now().minusDays(1))
                    .quantity(10)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertFalse(voucherPack.isActive());
        }

        @Test
        @DisplayName("Should return false when quantity is zero")
        void shouldReturnFalseWhenQuantityIsZero() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .status(VoucherPackStatus.PUBLISHED)
                    .quantity(0)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertFalse(voucherPack.isActive());
        }

        @Test
        @DisplayName("Should return true when packValidFrom is null")
        void shouldReturnTrueWhenPackValidFromIsNull() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .status(VoucherPackStatus.PUBLISHED)
                    .packValidFrom(null)
                    .packValidTo(LocalDate.now().plusDays(1))
                    .quantity(10)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertTrue(voucherPack.isActive());
        }

        @Test
        @DisplayName("Should return true when packValidTo is null")
        void shouldReturnTrueWhenPackValidToIsNull() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .status(VoucherPackStatus.PUBLISHED)
                    .packValidFrom(LocalDate.now().minusDays(1))
                    .packValidTo(null)
                    .quantity(10)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertTrue(voucherPack.isActive());
        }
    }

    @Nested
    @DisplayName("canRedeem Method Tests")
    class CanRedeemTests {

        @Test
        @DisplayName("Should return true for valid redemption request")
        void shouldReturnTrueForValidRedemptionRequest() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .status(VoucherPackStatus.PUBLISHED)
                    .quantity(10)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertTrue(voucherPack.canRedeem(5));
        }

        @Test
        @DisplayName("Should return false for inactive pack")
        void shouldReturnFalseForInactivePack() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .status(VoucherPackStatus.PENDING)
                    .quantity(10)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertFalse(voucherPack.canRedeem(5));
        }

        @Test
        @DisplayName("Should return false for zero quantity request")
        void shouldReturnFalseForZeroQuantityRequest() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .status(VoucherPackStatus.PUBLISHED)
                    .quantity(10)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertFalse(voucherPack.canRedeem(0));
        }

        @Test
        @DisplayName("Should return false for negative quantity request")
        void shouldReturnFalseForNegativeQuantityRequest() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .status(VoucherPackStatus.PUBLISHED)
                    .quantity(10)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertFalse(voucherPack.canRedeem(-1));
        }

        @Test
        @DisplayName("Should return false when requested quantity exceeds available")
        void shouldReturnFalseWhenRequestedQuantityExceedsAvailable() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .status(VoucherPackStatus.PUBLISHED)
                    .quantity(10)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertFalse(voucherPack.canRedeem(15));
        }

        @Test
        @DisplayName("Should return true when requested quantity equals available")
        void shouldReturnTrueWhenRequestedQuantityEqualsAvailable() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .status(VoucherPackStatus.PUBLISHED)
                    .quantity(10)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertTrue(voucherPack.canRedeem(10));
        }
    }

    @Nested
    @DisplayName("redeem Method Tests")
    class RedeemTests {

        @Test
        @DisplayName("Should reduce quantity after successful redemption")
        void shouldReduceQuantityAfterSuccessfulRedemption() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .status(VoucherPackStatus.PUBLISHED)
                    .quantity(10)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When
            voucherPack.redeem(3);

            // Then
            assertEquals(7, voucherPack.getQuantity());
        }

        @Test
        @DisplayName("Should close pack when quantity reaches zero")
        void shouldClosePackWhenQuantityReachesZero() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .status(VoucherPackStatus.PUBLISHED)
                    .quantity(5)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When
            voucherPack.redeem(5);

            // Then
            assertEquals(0, voucherPack.getQuantity());
            assertEquals(VoucherPackStatus.CLOSED, voucherPack.getStatus());
        }

        @Test
        @DisplayName("Should throw exception when trying to redeem more than available")
        void shouldThrowExceptionWhenTryingToRedeemMoreThanAvailable() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .status(VoucherPackStatus.PUBLISHED)
                    .quantity(5)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertThrows(VoucherDomainException.class, () -> voucherPack.redeem(6));
        }

        @Test
        @DisplayName("Should throw exception when pack cannot provide requested quantity")
        void shouldThrowExceptionWhenPackCannotProvideRequestedQuantity() {
            // Given
            VoucherPack voucherPack = VoucherPack.builder()
                    .status(VoucherPackStatus.PENDING)
                    .quantity(10)
                    .build();
            voucherPack.setId(mockVoucherPackId);

            // When & Then
            assertThrows(VoucherDomainException.class, () -> voucherPack.redeem(5));
        }
    }
}
