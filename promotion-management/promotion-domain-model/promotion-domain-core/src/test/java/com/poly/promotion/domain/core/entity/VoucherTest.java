package com.poly.promotion.domain.core.entity;

import com.poly.promotion.domain.core.exception.VoucherDomainException;
import com.poly.promotion.domain.core.valueobject.DateRange;
import com.poly.promotion.domain.core.valueobject.Discount;
import com.poly.promotion.domain.core.valueobject.VoucherStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@DisplayName("Voucher Entity Tests")
class VoucherTest {

    @Mock
    private Discount mockDiscount;

    @Mock
    private DateRange mockDateRange;

    private static final String VALID_CUSTOMER_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final Long VALID_VOUCHER_PACK_ID = 1L;
    private static final BigDecimal VALID_DISCOUNT_AMOUNT = BigDecimal.valueOf(50.0);

    @BeforeEach
    void setUp() {
        lenient().when(mockDiscount.getValue()).thenReturn(VALID_DISCOUNT_AMOUNT);
        lenient().when(mockDateRange.getValue()).thenReturn(30);
        lenient().when(mockDateRange.getUnit()).thenReturn(ChronoUnit.DAYS);
    }

    @Nested
    @DisplayName("initRedeem Factory Method Tests")
    class InitRedeemTests {

        @Test
        @DisplayName("Should create voucher with REDEEMED status")
        void shouldCreateVoucherWithRedeemedStatus() {
            // When
            Voucher voucher = Voucher.initRedeem(VALID_CUSTOMER_ID, VALID_VOUCHER_PACK_ID, mockDiscount, mockDateRange);

            // Then
            assertEquals(VoucherStatus.REDEEMED, voucher.getVoucherStatus());
        }

        @Test
        @DisplayName("Should set correct customer ID")
        void shouldSetCorrectCustomerId() {
            // When
            Voucher voucher = Voucher.initRedeem(VALID_CUSTOMER_ID, VALID_VOUCHER_PACK_ID, mockDiscount, mockDateRange);

            // Then
            assertEquals(VALID_CUSTOMER_ID, voucher.getCustomerId().getValue().toString());
        }

        @Test
        @DisplayName("Should set correct voucher pack ID")
        void shouldSetCorrectVoucherPackId() {
            // When
            Voucher voucher = Voucher.initRedeem(VALID_CUSTOMER_ID, VALID_VOUCHER_PACK_ID, mockDiscount, mockDateRange);

            // Then
            assertEquals(VALID_VOUCHER_PACK_ID, voucher.getVoucherPackId().getValue());
        }

        @Test
        @DisplayName("Should set redeemed timestamp to current time")
        void shouldSetRedeemedTimestampToCurrentTime() {
            // When
            LocalDateTime beforeCreation = LocalDateTime.now();
            Voucher voucher = Voucher.initRedeem(VALID_CUSTOMER_ID, VALID_VOUCHER_PACK_ID, mockDiscount, mockDateRange);
            LocalDateTime afterCreation = LocalDateTime.now();

            // Then
            assertTrue(voucher.getRedeemedAt().isAfter(beforeCreation) || voucher.getRedeemedAt().equals(beforeCreation));
            assertTrue(voucher.getRedeemedAt().isBefore(afterCreation) || voucher.getRedeemedAt().equals(afterCreation));
        }

        @Test
        @DisplayName("Should calculate validTo based on voucherValidRange")
        void shouldCalculateValidToBasedOnVoucherValidRange() {
            // When
            Voucher voucher = Voucher.initRedeem(VALID_CUSTOMER_ID, VALID_VOUCHER_PACK_ID, mockDiscount, mockDateRange);

            // Then
            LocalDateTime expectedValidTo = LocalDateTime.now().plus(30, ChronoUnit.DAYS);
            assertTrue(voucher.getValidTo().isAfter(expectedValidTo.minusMinutes(1)));
            assertTrue(voucher.getValidTo().isBefore(expectedValidTo.plusMinutes(1)));
        }

        @Test
        @DisplayName("Should generate unique voucher code")
        void shouldGenerateUniqueVoucherCode() {
            // When
            Voucher voucher1 = Voucher.initRedeem(VALID_CUSTOMER_ID, VALID_VOUCHER_PACK_ID, mockDiscount, mockDateRange);
            Voucher voucher2 = Voucher.initRedeem(VALID_CUSTOMER_ID, VALID_VOUCHER_PACK_ID, mockDiscount, mockDateRange);

            // Then
            assertNotNull(voucher1.getVoucherCode());
            assertNotNull(voucher2.getVoucherCode());
            assertNotEquals(voucher1.getVoucherCode(), voucher2.getVoucherCode());
        }

        @Test
        @DisplayName("Should throw exception for invalid customer ID format")
        void shouldThrowExceptionForInvalidCustomerIdFormat() {
            // Given
            String invalidCustomerId = "invalid-uuid";

            // When & Then
            assertThrows(IllegalArgumentException.class, () -> 
                Voucher.initRedeem(invalidCustomerId, VALID_VOUCHER_PACK_ID, mockDiscount, mockDateRange)
            );
        }

        @Test
        @DisplayName("Should handle null customer ID")
        void shouldHandleNullCustomerId() {
            // When & Then
            assertThrows(NullPointerException.class, () -> 
                Voucher.initRedeem(null, VALID_VOUCHER_PACK_ID, mockDiscount, mockDateRange)
            );
        }
    }

    @Nested
    @DisplayName("isValid Method Tests")
    class IsValidTests {

        private Voucher voucher;

        @BeforeEach
        void setUp() {
            voucher = Voucher.initRedeem(VALID_CUSTOMER_ID, VALID_VOUCHER_PACK_ID, mockDiscount, mockDateRange);
        }

        @Test
        @DisplayName("Should return true for valid REDEEMED voucher")
        void shouldReturnTrueForValidRedeemedVoucher() {
            // Given
            voucher.setVoucherStatus(VoucherStatus.REDEEMED);

            // When & Then
            assertTrue(voucher.isValid());
        }

        @Test
        @DisplayName("Should return false for PENDING status")
        void shouldReturnFalseForPendingStatus() {
            // Given
            voucher.setVoucherStatus(VoucherStatus.PENDING);

            // When & Then
            assertFalse(voucher.isValid());
        }

        @Test
        @DisplayName("Should return false for USED status")
        void shouldReturnFalseForUsedStatus() {
            // Given
            voucher.setVoucherStatus(VoucherStatus.USED);

            // When & Then
            assertFalse(voucher.isValid());
        }

        @Test
        @DisplayName("Should return false for EXPIRED status")
        void shouldReturnFalseForExpiredStatus() {
            // Given
            voucher.setVoucherStatus(VoucherStatus.EXPIRED);

            // When & Then
            assertFalse(voucher.isValid());
        }

        @Test
        @DisplayName("Should return false for expired voucher")
        void shouldReturnFalseForExpiredVoucher() {
            // Given
            voucher.setVoucherStatus(VoucherStatus.REDEEMED);
            voucher.setValidTo(LocalDateTime.now().minusDays(1));

            // When & Then
            assertFalse(voucher.isValid());
        }
    }

    @Nested
    @DisplayName("canUse Method Tests")
    class CanUseTests {

        private Voucher voucher;

        @BeforeEach
        void setUp() {
            voucher = Voucher.initRedeem(VALID_CUSTOMER_ID, VALID_VOUCHER_PACK_ID, mockDiscount, mockDateRange);
        }

        @Test
        @DisplayName("Should return true for usable voucher")
        void shouldReturnTrueForUsableVoucher() {
            // Given
            voucher.setVoucherStatus(VoucherStatus.REDEEMED);

            // When & Then
            assertTrue(voucher.canUse());
        }

        @Test
        @DisplayName("Should return false for non-REDEEMED status")
        void shouldReturnFalseForNonRedeemedStatus() {
            // Given
            voucher.setVoucherStatus(VoucherStatus.PENDING);

            // When & Then
            assertFalse(voucher.canUse());
        }

        @Test
        @DisplayName("Should return false for expired voucher")
        void shouldReturnFalseForExpiredVoucher() {
            // Given
            voucher.setVoucherStatus(VoucherStatus.REDEEMED);
            voucher.setValidTo(LocalDateTime.now().minusDays(1));

            // When & Then
            assertFalse(voucher.canUse());
        }
    }

    @Nested
    @DisplayName("use Method Tests")
    class UseTests {

        private Voucher voucher;

        @BeforeEach
        void setUp() {
            voucher = Voucher.initRedeem(VALID_CUSTOMER_ID, VALID_VOUCHER_PACK_ID, mockDiscount, mockDateRange);
            voucher.setVoucherStatus(VoucherStatus.REDEEMED);
        }

        @Test
        @DisplayName("Should change status to USED when voucher can be used")
        void shouldChangeStatusToUsedWhenVoucherCanBeUsed() {
            // When
            voucher.use();

            // Then
            assertEquals(VoucherStatus.USED, voucher.getVoucherStatus());
        }

        @Test
        @DisplayName("Should throw exception when voucher cannot be used")
        void shouldThrowExceptionWhenVoucherCannotBeUsed() {
            // Given
            voucher.setVoucherStatus(VoucherStatus.PENDING);

            // When & Then
            assertThrows(VoucherDomainException.class, () -> voucher.use());
        }

        @Test
        @DisplayName("Should throw exception for expired voucher")
        void shouldThrowExceptionForExpiredVoucher() {
            // Given
            voucher.setValidTo(LocalDateTime.now().minusDays(1));

            // When & Then
            assertThrows(VoucherDomainException.class, () -> voucher.use());
        }

        @Test
        @DisplayName("Should throw exception for already used voucher")
        void shouldThrowExceptionForAlreadyUsedVoucher() {
            // Given
            voucher.use(); // First use

            // When & Then
            assertThrows(VoucherDomainException.class, () -> voucher.use());
        }
    }

    @Nested
    @DisplayName("expire Method Tests")
    class ExpireTests {

        private Voucher voucher;

        @BeforeEach
        void setUp() {
            voucher = Voucher.initRedeem(VALID_CUSTOMER_ID, VALID_VOUCHER_PACK_ID, mockDiscount, mockDateRange);
        }

        @Test
        @DisplayName("Should change status to EXPIRED for expired REDEEMED voucher")
        void shouldChangeStatusToExpiredForExpiredRedeemedVoucher() {
            // Given
            voucher.setVoucherStatus(VoucherStatus.REDEEMED);
            voucher.setValidTo(LocalDateTime.now().minusDays(1));

            // When
            voucher.expire();

            // Then
            assertEquals(VoucherStatus.EXPIRED, voucher.getVoucherStatus());
        }

        @Test
        @DisplayName("Should not change status for non-expired voucher")
        void shouldNotChangeStatusForNonExpiredVoucher() {
            // Given
            voucher.setVoucherStatus(VoucherStatus.REDEEMED);
            voucher.setValidTo(LocalDateTime.now().plusDays(1));

            // When
            voucher.expire();

            // Then
            assertEquals(VoucherStatus.REDEEMED, voucher.getVoucherStatus());
        }

        @Test
        @DisplayName("Should not change status for non-REDEEMED voucher")
        void shouldNotChangeStatusForNonRedeemedVoucher() {
            // Given
            voucher.setVoucherStatus(VoucherStatus.PENDING);
            voucher.setValidTo(LocalDateTime.now().minusDays(1));

            // When
            voucher.expire();

            // Then
            assertEquals(VoucherStatus.PENDING, voucher.getVoucherStatus());
        }
    }

    @Nested
    @DisplayName("isExpired Method Tests")
    class IsExpiredTests {

        private Voucher voucher;

        @BeforeEach
        void setUp() {
            voucher = Voucher.initRedeem(VALID_CUSTOMER_ID, VALID_VOUCHER_PACK_ID, mockDiscount, mockDateRange);
        }

        @Test
        @DisplayName("Should return true for expired voucher")
        void shouldReturnTrueForExpiredVoucher() {
            // Given
            voucher.setValidTo(LocalDateTime.now().minusDays(1));

            // When & Then
            assertTrue(voucher.isExpired());
        }

        @Test
        @DisplayName("Should return false for non-expired voucher")
        void shouldReturnFalseForNonExpiredVoucher() {
            // Given
            voucher.setValidTo(LocalDateTime.now().plusDays(1));

            // When & Then
            assertFalse(voucher.isExpired());
        }

        @Test
        @DisplayName("Should return false for voucher expiring in the future")
        void shouldReturnFalseForVoucherExpiringInTheFuture() {
            // Given
            voucher.setValidTo(LocalDateTime.now().plusMinutes(1));

            // When & Then
            assertFalse(voucher.isExpired());
        }
    }
}
