package com.poly.promotion.domain.core.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("VoucherStatus Enum Tests")
class VoucherStatusTest {

    @Nested
    @DisplayName("fromString Method Tests")
    class FromStringTests {

        @Test
        @DisplayName("Should return PENDING for 'pending'")
        void shouldReturnPendingForPending() {
            // When
            VoucherStatus status = VoucherStatus.fromString("pending");

            // Then
            assertEquals(VoucherStatus.PENDING, status);
        }

        @Test
        @DisplayName("Should return PENDING for 'PENDING'")
        void shouldReturnPendingForPendingUppercase() {
            // When
            VoucherStatus status = VoucherStatus.fromString("PENDING");

            // Then
            assertEquals(VoucherStatus.PENDING, status);
        }

        @Test
        @DisplayName("Should return REDEEMED for 'redeemed'")
        void shouldReturnRedeemedForRedeemed() {
            // When
            VoucherStatus status = VoucherStatus.fromString("redeemed");

            // Then
            assertEquals(VoucherStatus.REDEEMED, status);
        }

        @Test
        @DisplayName("Should return REDEEMED for 'REDEEMED'")
        void shouldReturnRedeemedForRedeemedUppercase() {
            // When
            VoucherStatus status = VoucherStatus.fromString("REDEEMED");

            // Then
            assertEquals(VoucherStatus.REDEEMED, status);
        }

        @Test
        @DisplayName("Should return USED for 'used'")
        void shouldReturnUsedForUsed() {
            // When
            VoucherStatus status = VoucherStatus.fromString("used");

            // Then
            assertEquals(VoucherStatus.USED, status);
        }

        @Test
        @DisplayName("Should return USED for 'USED'")
        void shouldReturnUsedForUsedUppercase() {
            // When
            VoucherStatus status = VoucherStatus.fromString("USED");

            // Then
            assertEquals(VoucherStatus.USED, status);
        }

        @Test
        @DisplayName("Should return EXPIRED for 'expired'")
        void shouldReturnExpiredForExpired() {
            // When
            VoucherStatus status = VoucherStatus.fromString("expired");

            // Then
            assertEquals(VoucherStatus.EXPIRED, status);
        }

        @Test
        @DisplayName("Should return EXPIRED for 'EXPIRED'")
        void shouldReturnExpiredForExpiredUppercase() {
            // When
            VoucherStatus status = VoucherStatus.fromString("EXPIRED");

            // Then
            assertEquals(VoucherStatus.EXPIRED, status);
        }

        @Test
        @DisplayName("Should throw exception for unknown status")
        void shouldThrowExceptionForUnknownStatus() {
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> 
                VoucherStatus.fromString("UNKNOWN")
            );
        }

        @Test
        @DisplayName("Should throw exception for null status")
        void shouldThrowExceptionForNullStatus() {
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> 
                VoucherStatus.fromString(null)
            );
        }

        @Test
        @DisplayName("Should throw exception for empty status")
        void shouldThrowExceptionForEmptyStatus() {
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> 
                VoucherStatus.fromString("")
            );
        }
    }

    @Nested
    @DisplayName("canTransitionTo Method Tests")
    class CanTransitionToTests {

        @Test
        @DisplayName("Should not allow PENDING to REDEEMED transition")
        void shouldNotAllowPendingToRedeemedTransition() {
            // When & Then
            assertFalse(VoucherStatus.PENDING.canTransitionTo(VoucherStatus.REDEEMED));
        }

        @Test
        @DisplayName("Should not allow PENDING to USED transition")
        void shouldNotAllowPendingToUsedTransition() {
            // When & Then
            assertFalse(VoucherStatus.PENDING.canTransitionTo(VoucherStatus.USED));
        }

        @Test
        @DisplayName("Should allow PENDING to EXPIRED transition")
        void shouldAllowPendingToExpiredTransition() {
            // When & Then
            assertTrue(VoucherStatus.PENDING.canTransitionTo(VoucherStatus.EXPIRED));
        }

        @Test
        @DisplayName("Should allow REDEEMED to USED transition")
        void shouldAllowRedeemedToUsedTransition() {
            // When & Then
            assertTrue(VoucherStatus.REDEEMED.canTransitionTo(VoucherStatus.USED));
        }

        @Test
        @DisplayName("Should not allow REDEEMED to PENDING transition")
        void shouldNotAllowRedeemedToPendingTransition() {
            // When & Then
            assertFalse(VoucherStatus.REDEEMED.canTransitionTo(VoucherStatus.PENDING));
        }

        @Test
        @DisplayName("Should not allow REDEEMED to REDEEMED transition")
        void shouldNotAllowRedeemedToRedeemedTransition() {
            // When & Then
            assertFalse(VoucherStatus.REDEEMED.canTransitionTo(VoucherStatus.REDEEMED));
        }

        @Test
        @DisplayName("Should not allow USED to any transition")
        void shouldNotAllowUsedToAnyTransition() {
            // When & Then
            assertFalse(VoucherStatus.USED.canTransitionTo(VoucherStatus.PENDING));
            assertFalse(VoucherStatus.USED.canTransitionTo(VoucherStatus.REDEEMED));
            assertFalse(VoucherStatus.USED.canTransitionTo(VoucherStatus.USED));
        }

        @Test
        @DisplayName("Should not allow EXPIRED to any transition")
        void shouldNotAllowExpiredToAnyTransition() {
            // When & Then
            assertFalse(VoucherStatus.EXPIRED.canTransitionTo(VoucherStatus.PENDING));
            assertFalse(VoucherStatus.EXPIRED.canTransitionTo(VoucherStatus.REDEEMED));
            assertFalse(VoucherStatus.EXPIRED.canTransitionTo(VoucherStatus.USED));
        }

        @Test
        @DisplayName("Should allow any status to EXPIRED transition")
        void shouldAllowAnyStatusToExpiredTransition() {
            // When & Then
            assertTrue(VoucherStatus.PENDING.canTransitionTo(VoucherStatus.EXPIRED));
            assertTrue(VoucherStatus.REDEEMED.canTransitionTo(VoucherStatus.EXPIRED));
            assertTrue(VoucherStatus.USED.canTransitionTo(VoucherStatus.EXPIRED));
            assertTrue(VoucherStatus.EXPIRED.canTransitionTo(VoucherStatus.EXPIRED));
        }
    }

    @Nested
    @DisplayName("isActive Method Tests")
    class IsActiveTests {

        @Test
        @DisplayName("Should return true for REDEEMED status")
        void shouldReturnTrueForRedeemedStatus() {
            // When & Then
            assertTrue(VoucherStatus.REDEEMED.isActive());
        }

        @Test
        @DisplayName("Should return false for PENDING status")
        void shouldReturnFalseForPendingStatus() {
            // When & Then
            assertFalse(VoucherStatus.PENDING.isActive());
        }

        @Test
        @DisplayName("Should return false for USED status")
        void shouldReturnFalseForUsedStatus() {
            // When & Then
            assertFalse(VoucherStatus.USED.isActive());
        }

        @Test
        @DisplayName("Should return false for EXPIRED status")
        void shouldReturnFalseForExpiredStatus() {
            // When & Then
            assertFalse(VoucherStatus.EXPIRED.isActive());
        }
    }

    @Nested
    @DisplayName("isTerminal Method Tests")
    class IsTerminalTests {

        @Test
        @DisplayName("Should return true for USED status")
        void shouldReturnTrueForUsedStatus() {
            // When & Then
            assertTrue(VoucherStatus.USED.isTerminal());
        }

        @Test
        @DisplayName("Should return true for EXPIRED status")
        void shouldReturnTrueForExpiredStatus() {
            // When & Then
            assertTrue(VoucherStatus.EXPIRED.isTerminal());
        }

        @Test
        @DisplayName("Should return false for PENDING status")
        void shouldReturnFalseForPendingStatus() {
            // When & Then
            assertFalse(VoucherStatus.PENDING.isTerminal());
        }

        @Test
        @DisplayName("Should return false for REDEEMED status")
        void shouldReturnFalseForRedeemedStatus() {
            // When & Then
            assertFalse(VoucherStatus.REDEEMED.isTerminal());
        }
    }

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @Test
        @DisplayName("Should have exactly 4 values")
        void shouldHaveExactlyFourValues() {
            // When
            VoucherStatus[] values = VoucherStatus.values();

            // Then
            assertEquals(4, values.length);
        }

        @Test
        @DisplayName("Should contain all expected statuses")
        void shouldContainAllExpectedStatuses() {
            // When
            VoucherStatus[] values = VoucherStatus.values();

            // Then
            assertTrue(contains(values, VoucherStatus.PENDING));
            assertTrue(contains(values, VoucherStatus.REDEEMED));
            assertTrue(contains(values, VoucherStatus.USED));
            assertTrue(contains(values, VoucherStatus.EXPIRED));
        }

        @Test
        @DisplayName("Should have correct ordinal values")
        void shouldHaveCorrectOrdinalValues() {
            // When & Then
            assertEquals(0, VoucherStatus.PENDING.ordinal());
            assertEquals(1, VoucherStatus.REDEEMED.ordinal());
            assertEquals(2, VoucherStatus.USED.ordinal());
            assertEquals(3, VoucherStatus.EXPIRED.ordinal());
        }

        @Test
        @DisplayName("Should have correct names")
        void shouldHaveCorrectNames() {
            // When & Then
            assertEquals("PENDING", VoucherStatus.PENDING.name());
            assertEquals("REDEEMED", VoucherStatus.REDEEMED.name());
            assertEquals("USED", VoucherStatus.USED.name());
            assertEquals("EXPIRED", VoucherStatus.EXPIRED.name());
        }

        private boolean contains(VoucherStatus[] values, VoucherStatus status) {
            for (VoucherStatus value : values) {
                if (value == status) {
                    return true;
                }
            }
            return false;
        }
    }
}
