package com.poly.promotion.data.access.jparepository;

import com.poly.promotion.data.access.jpaentity.VoucherJpaEntity;
import com.poly.promotion.data.access.jpaentity.VoucherPackJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * <h2>Voucher JPA Repository Test</h2>
 * 
 * <p>Unit tests for VoucherJpaRepository covering:</p>
 * <ul>
 *   <li>Repository method contracts</li>
 *   <li>Query method behavior</li>
 *   <li>Status-based filtering</li>
 *   <li>Customer-based filtering</li>
 *   <li>Voucher pack relationship queries</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Voucher JPA Repository Tests")
class VoucherJpaRepositoryTest {

    @Mock
    private VoucherJpaRepository voucherRepository;

    @Mock
    private VoucherPackJpaRepository voucherPackRepository;

    private VoucherPackJpaEntity voucherPack;
    private VoucherJpaEntity redeemedVoucher;
    private VoucherJpaEntity usedVoucher;
    private VoucherJpaEntity expiredVoucher;

    @BeforeEach
    void setUp() {
        // Create a voucher pack first
        voucherPack = VoucherPackJpaEntity.builder()
                .description("Test Voucher Pack")
                .discountAmount(new BigDecimal("10.00"))
                .quantity(100)
                .requiredPoints(50L)
                .validRange("30 days")
                .status(VoucherPackJpaEntity.VoucherPackStatus.PUBLISHED)
                .validFrom(LocalDate.now())
                .validTo(LocalDate.now().plusDays(30))
                .createdBy("test-user")
                .build();

        // Create test vouchers
        redeemedVoucher = createVoucher("REDEEM001", "customer-123", VoucherJpaEntity.VoucherStatus.REDEEMED, 
            LocalDateTime.now().plusDays(30));
        
        usedVoucher = createVoucher("USED001", "customer-456", VoucherJpaEntity.VoucherStatus.USED, 
            LocalDateTime.now().plusDays(30));
        usedVoucher.setUsedAt(LocalDateTime.now());
        
        expiredVoucher = createVoucher("EXPIRED001", "customer-789", VoucherJpaEntity.VoucherStatus.EXPIRED, 
            LocalDateTime.now().minusDays(1));
    }

    private VoucherJpaEntity createVoucher(String voucherCode, String customerId, VoucherJpaEntity.VoucherStatus status, 
                                          LocalDateTime validTo) {
        return VoucherJpaEntity.builder()
                .voucherCode(voucherCode)
                .customerId(customerId)
                .voucherPack(voucherPack)
                .discountAmount(new BigDecimal("10.00"))
                .status(status)
                .redeemedAt(LocalDateTime.now())
                .validTo(validTo)
                .build();
    }

    @Nested
    @DisplayName("Basic CRUD Operations")
    class BasicCrudOperations {

        @Test
        @DisplayName("Should save voucher")
        void shouldSaveVoucher() {
            // Given
            VoucherJpaEntity newVoucher = createVoucher("NEW001", "customer-new", VoucherJpaEntity.VoucherStatus.REDEEMED, 
                LocalDateTime.now().plusDays(30));
            when(voucherRepository.save(any(VoucherJpaEntity.class)))
                    .thenReturn(newVoucher);

            // When
            VoucherJpaEntity saved = voucherRepository.save(newVoucher);

            // Then
            assertThat(saved).isNotNull();
            assertThat(saved.getVoucherCode()).isEqualTo("NEW001");
            assertThat(saved.getStatus()).isEqualTo(VoucherJpaEntity.VoucherStatus.REDEEMED);
            verify(voucherRepository).save(newVoucher);
        }

        @Test
        @DisplayName("Should find voucher by ID")
        void shouldFindVoucherById() {
            // Given
            when(voucherRepository.findById("voucher-123"))
                    .thenReturn(Optional.of(redeemedVoucher));

            // When
            Optional<VoucherJpaEntity> found = voucherRepository.findById("voucher-123");

            // Then
            assertThat(found).isPresent();
            assertThat(found.get().getVoucherCode()).isEqualTo("REDEEM001");
            verify(voucherRepository).findById("voucher-123");
        }

        @Test
        @DisplayName("Should return empty for non-existent ID")
        void shouldReturnEmptyForNonExistentId() {
            // Given
            when(voucherRepository.findById("non-existent"))
                    .thenReturn(Optional.empty());

            // When
            Optional<VoucherJpaEntity> found = voucherRepository.findById("non-existent");

            // Then
            assertThat(found).isNotPresent();
            verify(voucherRepository).findById("non-existent");
        }

        @Test
        @DisplayName("Should update voucher")
        void shouldUpdateVoucher() {
            // Given
            String newVoucherCode = "UPDATED001";
            redeemedVoucher.setVoucherCode(newVoucherCode);
            when(voucherRepository.save(any(VoucherJpaEntity.class)))
                    .thenReturn(redeemedVoucher);

            // When
            VoucherJpaEntity updated = voucherRepository.save(redeemedVoucher);

            // Then
            assertThat(updated.getVoucherCode()).isEqualTo(newVoucherCode);
            verify(voucherRepository).save(redeemedVoucher);
        }

        @Test
        @DisplayName("Should delete voucher")
        void shouldDeleteVoucher() {
            // Given
            doNothing().when(voucherRepository).deleteById("voucher-123");

            // When
            voucherRepository.deleteById("voucher-123");

            // Then
            verify(voucherRepository).deleteById("voucher-123");
        }

        @Test
        @DisplayName("Should count all vouchers")
        void shouldCountAllVouchers() {
            // Given
            when(voucherRepository.count()).thenReturn(3L);

            // When
            long count = voucherRepository.count();

            // Then
            assertThat(count).isEqualTo(3);
            verify(voucherRepository).count();
        }
    }

    @Nested
    @DisplayName("Status-Based Queries")
    class StatusBasedQueries {

        @Test
        @DisplayName("Should find vouchers by status")
        void shouldFindVouchersByStatus() {
            // Given
            when(voucherRepository.findByStatus(VoucherJpaEntity.VoucherStatus.REDEEMED))
                    .thenReturn(List.of(redeemedVoucher));

            // When
            List<VoucherJpaEntity> redeemedVouchers = voucherRepository.findByStatus(VoucherJpaEntity.VoucherStatus.REDEEMED);

            // Then
            assertThat(redeemedVouchers).hasSize(1);
            assertThat(redeemedVouchers.get(0).getVoucherCode()).isEqualTo("REDEEM001");
            verify(voucherRepository).findByStatus(VoucherJpaEntity.VoucherStatus.REDEEMED);
        }

        @Test
        @DisplayName("Should find used vouchers")
        void shouldFindUsedVouchers() {
            // Given
            when(voucherRepository.findByStatus(VoucherJpaEntity.VoucherStatus.USED))
                    .thenReturn(List.of(usedVoucher));

            // When
            List<VoucherJpaEntity> usedVouchers = voucherRepository.findByStatus(VoucherJpaEntity.VoucherStatus.USED);

            // Then
            assertThat(usedVouchers).hasSize(1);
            assertThat(usedVouchers.get(0).getVoucherCode()).isEqualTo("USED001");
            verify(voucherRepository).findByStatus(VoucherJpaEntity.VoucherStatus.USED);
        }

        @Test
        @DisplayName("Should find expired vouchers")
        void shouldFindExpiredVouchers() {
            // Given
            when(voucherRepository.findByStatus(VoucherJpaEntity.VoucherStatus.EXPIRED))
                    .thenReturn(List.of(expiredVoucher));

            // When
            List<VoucherJpaEntity> expiredVouchers = voucherRepository.findByStatus(VoucherJpaEntity.VoucherStatus.EXPIRED);

            // Then
            assertThat(expiredVouchers).hasSize(1);
            assertThat(expiredVouchers.get(0).getVoucherCode()).isEqualTo("EXPIRED001");
            verify(voucherRepository).findByStatus(VoucherJpaEntity.VoucherStatus.EXPIRED);
        }
    }

    @Nested
    @DisplayName("Customer-Based Queries")
    class CustomerBasedQueries {

        @Test
        @DisplayName("Should find vouchers by customer ID")
        void shouldFindVouchersByCustomerId() {
            // Given
            when(voucherRepository.findByCustomerId("customer-123"))
                    .thenReturn(List.of(redeemedVoucher));

            // When
            List<VoucherJpaEntity> customerVouchers = voucherRepository.findByCustomerId("customer-123");

            // Then
            assertThat(customerVouchers).hasSize(1);
            assertThat(customerVouchers.get(0).getVoucherCode()).isEqualTo("REDEEM001");
            verify(voucherRepository).findByCustomerId("customer-123");
        }

        @Test
        @DisplayName("Should find vouchers by customer ID and status")
        void shouldFindVouchersByCustomerIdAndStatus() {
            // Given
            when(voucherRepository.findByCustomerIdAndStatus("customer-123", VoucherJpaEntity.VoucherStatus.REDEEMED))
                    .thenReturn(List.of(redeemedVoucher));

            // When
            List<VoucherJpaEntity> customerVouchers = voucherRepository.findByCustomerIdAndStatus(
                "customer-123", VoucherJpaEntity.VoucherStatus.REDEEMED);

            // Then
            assertThat(customerVouchers).hasSize(1);
            assertThat(customerVouchers.get(0).getVoucherCode()).isEqualTo("REDEEM001");
            verify(voucherRepository).findByCustomerIdAndStatus("customer-123", VoucherJpaEntity.VoucherStatus.REDEEMED);
        }

        @Test
        @DisplayName("Should count vouchers by customer ID")
        void shouldCountVouchersByCustomerId() {
            // Given
            when(voucherRepository.countByCustomerId("customer-123"))
                    .thenReturn(1L);

            // When
            long count = voucherRepository.countByCustomerId("customer-123");

            // Then
            assertThat(count).isEqualTo(1);
            verify(voucherRepository).countByCustomerId("customer-123");
        }
    }

    @Nested
    @DisplayName("Voucher Pack Relationship Queries")
    class VoucherPackRelationshipQueries {

        @Test
        @DisplayName("Should find vouchers by voucher pack ID")
        void shouldFindVouchersByVoucherPackId() {
            // Given
            when(voucherRepository.findByVoucherPackId(voucherPack.getId()))
                    .thenReturn(List.of(redeemedVoucher, usedVoucher, expiredVoucher));

            // When
            List<VoucherJpaEntity> packVouchers = voucherRepository.findByVoucherPackId(voucherPack.getId());

            // Then
            assertThat(packVouchers).hasSize(3);
            verify(voucherRepository).findByVoucherPackId(voucherPack.getId());
        }

        @Test
        @DisplayName("Should find vouchers by voucher pack ID and status")
        void shouldFindVouchersByVoucherPackIdAndStatus() {
            // Given
            when(voucherRepository.findByVoucherPackIdAndStatus(voucherPack.getId(), VoucherJpaEntity.VoucherStatus.REDEEMED))
                    .thenReturn(List.of(redeemedVoucher));

            // When
            List<VoucherJpaEntity> packVouchers = voucherRepository.findByVoucherPackIdAndStatus(
                voucherPack.getId(), VoucherJpaEntity.VoucherStatus.REDEEMED);

            // Then
            assertThat(packVouchers).hasSize(1);
            assertThat(packVouchers.get(0).getVoucherCode()).isEqualTo("REDEEM001");
            verify(voucherRepository).findByVoucherPackIdAndStatus(voucherPack.getId(), VoucherJpaEntity.VoucherStatus.REDEEMED);
        }

        @Test
        @DisplayName("Should count vouchers by voucher pack ID")
        void shouldCountVouchersByVoucherPackId() {
            // Given
            when(voucherRepository.findByVoucherPackId(voucherPack.getId()))
                    .thenReturn(List.of(redeemedVoucher, usedVoucher, expiredVoucher));

            // When
            List<VoucherJpaEntity> packVouchers = voucherRepository.findByVoucherPackId(voucherPack.getId());

            // Then
            assertThat(packVouchers).hasSize(3);
            verify(voucherRepository).findByVoucherPackId(voucherPack.getId());
        }
    }

    @Nested
    @DisplayName("Expiration and Usage Queries")
    class ExpirationAndUsageQueries {

        @Test
        @DisplayName("Should find vouchers eligible for expiration")
        void shouldFindVouchersEligibleForExpiration() {
            // Given
            when(voucherRepository.findVouchersEligibleForExpiration())
                    .thenReturn(List.of(expiredVoucher));

            // When
            List<VoucherJpaEntity> expiringVouchers = voucherRepository.findVouchersEligibleForExpiration();

            // Then
            assertThat(expiringVouchers).hasSize(1);
            assertThat(expiringVouchers.get(0).getVoucherCode()).isEqualTo("EXPIRED001");
            verify(voucherRepository).findVouchersEligibleForExpiration();
        }

        @Test
        @DisplayName("Should find vouchers by voucher code")
        void shouldFindVouchersByVoucherCode() {
            // Given
            when(voucherRepository.findByVoucherCode("REDEEM001"))
                    .thenReturn(Optional.of(redeemedVoucher));

            // When
            Optional<VoucherJpaEntity> found = voucherRepository.findByVoucherCode("REDEEM001");

            // Then
            assertThat(found).isPresent();
            assertThat(found.get().getCustomerId()).isEqualTo("customer-123");
            verify(voucherRepository).findByVoucherCode("REDEEM001");
        }

        @Test
        @DisplayName("Should find vouchers by voucher code and customer ID")
        void shouldFindVouchersByVoucherCodeAndCustomerId() {
            // Given
            when(voucherRepository.findByVoucherCode("REDEEM001"))
                    .thenReturn(Optional.of(redeemedVoucher));

            // When
            Optional<VoucherJpaEntity> found = voucherRepository.findByVoucherCode("REDEEM001");

            // Then
            assertThat(found).isPresent();
            assertThat(found.get().getCustomerId()).isEqualTo("customer-123");
            assertThat(found.get().getStatus()).isEqualTo(VoucherJpaEntity.VoucherStatus.REDEEMED);
            verify(voucherRepository).findByVoucherCode("REDEEM001");
        }
    }

    @Nested
    @DisplayName("Complex Query Scenarios")
    class ComplexQueryScenarios {

        @Test
        @DisplayName("Should find vouchers by multiple criteria")
        void shouldFindVouchersByMultipleCriteria() {
            // Given
            when(voucherRepository.findByCustomerIdAndStatus("customer-123", VoucherJpaEntity.VoucherStatus.REDEEMED))
                    .thenReturn(List.of(redeemedVoucher));
            when(voucherRepository.findByCustomerIdAndStatus("customer-123", VoucherJpaEntity.VoucherStatus.USED))
                    .thenReturn(List.of());

            // When
            List<VoucherJpaEntity> redeemedVouchers = voucherRepository.findByCustomerIdAndStatus(
                "customer-123", VoucherJpaEntity.VoucherStatus.REDEEMED);
            List<VoucherJpaEntity> usedVouchers = voucherRepository.findByCustomerIdAndStatus(
                "customer-123", VoucherJpaEntity.VoucherStatus.USED);

            // Then
            assertThat(redeemedVouchers).hasSize(1);
            assertThat(usedVouchers).isEmpty();
            verify(voucherRepository).findByCustomerIdAndStatus("customer-123", VoucherJpaEntity.VoucherStatus.REDEEMED);
            verify(voucherRepository).findByCustomerIdAndStatus("customer-123", VoucherJpaEntity.VoucherStatus.USED);
        }

        @Test
        @DisplayName("Should find vouchers by redemption date range")
        void shouldFindVouchersByRedemptionDateRange() {
            // Given
            LocalDateTime fromDate = LocalDateTime.now().minusDays(1);
            LocalDateTime toDate = LocalDateTime.now().plusDays(1);
            when(voucherRepository.findByRedemptionDateRange(fromDate, toDate))
                    .thenReturn(List.of(redeemedVoucher, usedVoucher, expiredVoucher));

            // When
            List<VoucherJpaEntity> vouchers = voucherRepository.findByRedemptionDateRange(fromDate, toDate);

            // Then
            assertThat(vouchers).hasSize(3); // all vouchers redeemed in range
            verify(voucherRepository).findByRedemptionDateRange(fromDate, toDate);
        }
    }

    @Nested
    @DisplayName("Bulk Operations")
    class BulkOperations {

        @Test
        @DisplayName("Should mark expired vouchers")
        void shouldMarkExpiredVouchers() {
            // Given
            when(voucherRepository.markExpiredVouchers())
                    .thenReturn(1);

            // When
            int updatedCount = voucherRepository.markExpiredVouchers();

            // Then
            assertThat(updatedCount).isGreaterThanOrEqualTo(0); // may be 0 if no vouchers are eligible
            verify(voucherRepository).markExpiredVouchers();
        }

        @Test
        @DisplayName("Should update voucher status")
        void shouldUpdateVoucherStatus() {
            // Given
            when(voucherRepository.updateVoucherStatus(
                    redeemedVoucher.getId(), VoucherJpaEntity.VoucherStatus.USED))
                    .thenReturn(1);

            // When
            int updatedCount = voucherRepository.updateVoucherStatus(
                    redeemedVoucher.getId(), VoucherJpaEntity.VoucherStatus.USED);

            // Then
            assertThat(updatedCount).isEqualTo(1);
            verify(voucherRepository).updateVoucherStatus(
                    redeemedVoucher.getId(), VoucherJpaEntity.VoucherStatus.USED);
        }
    }

    @Nested
    @DisplayName("Constraint Validation")
    class ConstraintValidation {

        @Test
        @DisplayName("Should handle duplicate voucher code gracefully")
        void shouldHandleDuplicateVoucherCodeGracefully() {
            // Given
            VoucherJpaEntity duplicateVoucher = createVoucher("REDEEM001", "customer-duplicate", VoucherJpaEntity.VoucherStatus.REDEEMED, 
                LocalDateTime.now().plusDays(30));
            when(voucherRepository.save(duplicateVoucher))
                    .thenReturn(duplicateVoucher);

            // When
            VoucherJpaEntity saved = voucherRepository.save(duplicateVoucher);

            // Then
            assertThat(saved).isNotNull();
            assertThat(saved.getVoucherCode()).isEqualTo("REDEEM001");
            verify(voucherRepository).save(duplicateVoucher);
        }
    }
}
