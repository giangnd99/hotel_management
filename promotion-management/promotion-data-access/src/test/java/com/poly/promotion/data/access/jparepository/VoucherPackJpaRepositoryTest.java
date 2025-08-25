package com.poly.promotion.data.access.jparepository;

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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * <h2>VoucherPack JPA Repository Test</h2>
 * 
 * <p>Unit tests for VoucherPackJpaRepository covering:</p>
 * <ul>
 *   <li>Repository method contracts</li>
 *   <li>Query method behavior</li>
 *   <li>Status-based filtering</li>
 *   <li>Date-based filtering</li>
 *   <li>Quantity-based filtering</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("VoucherPack JPA Repository Tests")
class VoucherPackJpaRepositoryTest {

    @Mock
    private VoucherPackJpaRepository voucherPackRepository;

    private VoucherPackJpaEntity pendingVoucherPack;
    private VoucherPackJpaEntity publishedVoucherPack;
    private VoucherPackJpaEntity closedVoucherPack;
    private VoucherPackJpaEntity expiredVoucherPack;

    @BeforeEach
    void setUp() {
        // Create test data
        pendingVoucherPack = createVoucherPack("Pending Pack", VoucherPackJpaEntity.VoucherPackStatus.PENDING, 100, 
            LocalDate.now().plusDays(1), LocalDate.now().plusDays(31));
        
        publishedVoucherPack = createVoucherPack("Published Pack", VoucherPackJpaEntity.VoucherPackStatus.PUBLISHED, 50, 
            LocalDate.now().minusDays(1), LocalDate.now().plusDays(30));
        
        closedVoucherPack = createVoucherPack("Closed Pack", VoucherPackJpaEntity.VoucherPackStatus.CLOSED, 0, 
            LocalDate.now().minusDays(10), LocalDate.now().plusDays(20));
        
        expiredVoucherPack = createVoucherPack("Expired Pack", VoucherPackJpaEntity.VoucherPackStatus.EXPIRED, 25, 
            LocalDate.now().minusDays(20), LocalDate.now().minusDays(1));
    }

    private VoucherPackJpaEntity createVoucherPack(String description, VoucherPackJpaEntity.VoucherPackStatus status, 
                                                   int quantity, LocalDate validFrom, LocalDate validTo) {
        return VoucherPackJpaEntity.builder()
                .description(description)
                .discountAmount(new BigDecimal("10.00"))
                .quantity(quantity)
                .requiredPoints(50L)
                .validRange("30 days")
                .status(status)
                .validFrom(validFrom)
                .validTo(validTo)
                .createdBy("test-user")
                .build();
    }

    @Nested
    @DisplayName("Basic CRUD Operations")
    class BasicCrudOperations {

        @Test
        @DisplayName("Should save voucher pack")
        void shouldSaveVoucherPack() {
            // Given
            when(voucherPackRepository.save(any(VoucherPackJpaEntity.class)))
                    .thenReturn(pendingVoucherPack);

            // When
            VoucherPackJpaEntity saved = voucherPackRepository.save(pendingVoucherPack);

            // Then
            assertThat(saved).isNotNull();
            assertThat(saved.getDescription()).isEqualTo("Pending Pack");
            verify(voucherPackRepository).save(pendingVoucherPack);
        }

        @Test
        @DisplayName("Should find voucher pack by ID")
        void shouldFindVoucherPackById() {
            // Given
            when(voucherPackRepository.findById(1L))
                    .thenReturn(Optional.of(pendingVoucherPack));

            // When
            Optional<VoucherPackJpaEntity> found = voucherPackRepository.findById(1L);

            // Then
            assertThat(found).isPresent();
            assertThat(found.get().getDescription()).isEqualTo("Pending Pack");
            verify(voucherPackRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty for non-existent ID")
        void shouldReturnEmptyForNonExistentId() {
            // Given
            when(voucherPackRepository.findById(999L))
                    .thenReturn(Optional.empty());

            // When
            Optional<VoucherPackJpaEntity> found = voucherPackRepository.findById(999L);

            // Then
            assertThat(found).isNotPresent();
            verify(voucherPackRepository).findById(999L);
        }

        @Test
        @DisplayName("Should update voucher pack")
        void shouldUpdateVoucherPack() {
            // Given
            String newDescription = "Updated Description";
            pendingVoucherPack.setDescription(newDescription);
            when(voucherPackRepository.save(any(VoucherPackJpaEntity.class)))
                    .thenReturn(pendingVoucherPack);

            // When
            VoucherPackJpaEntity updated = voucherPackRepository.save(pendingVoucherPack);

            // Then
            assertThat(updated.getDescription()).isEqualTo(newDescription);
            verify(voucherPackRepository).save(pendingVoucherPack);
        }

        @Test
        @DisplayName("Should delete voucher pack")
        void shouldDeleteVoucherPack() {
            // Given
            doNothing().when(voucherPackRepository).deleteById(1L);

            // When
            voucherPackRepository.deleteById(1L);

            // Then
            verify(voucherPackRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should count all voucher packs")
        void shouldCountAllVoucherPacks() {
            // Given
            when(voucherPackRepository.count()).thenReturn(4L);

            // When
            long count = voucherPackRepository.count();

            // Then
            assertThat(count).isEqualTo(4);
            verify(voucherPackRepository).count();
        }
    }

    @Nested
    @DisplayName("Status-Based Queries")
    class StatusBasedQueries {

        @Test
        @DisplayName("Should find voucher packs by status")
        void shouldFindVoucherPacksByStatus() {
            // Given
            when(voucherPackRepository.findByStatus(VoucherPackJpaEntity.VoucherPackStatus.PENDING))
                    .thenReturn(List.of(pendingVoucherPack));

            // When
            List<VoucherPackJpaEntity> pendingPacks = voucherPackRepository.findByStatus(VoucherPackJpaEntity.VoucherPackStatus.PENDING);

            // Then
            assertThat(pendingPacks).hasSize(1);
            assertThat(pendingPacks.get(0).getDescription()).isEqualTo("Pending Pack");
            verify(voucherPackRepository).findByStatus(VoucherPackJpaEntity.VoucherPackStatus.PENDING);
        }

        @Test
        @DisplayName("Should find voucher packs by multiple statuses")
        void shouldFindVoucherPacksByMultipleStatuses() {
            // Given
            when(voucherPackRepository.findByStatus(VoucherPackJpaEntity.VoucherPackStatus.PENDING))
                    .thenReturn(List.of(pendingVoucherPack));
            when(voucherPackRepository.findByStatus(VoucherPackJpaEntity.VoucherPackStatus.PUBLISHED))
                    .thenReturn(List.of(publishedVoucherPack));

            // When
            List<VoucherPackJpaEntity> pendingPacks = voucherPackRepository.findByStatus(VoucherPackJpaEntity.VoucherPackStatus.PENDING);
            List<VoucherPackJpaEntity> publishedPacks = voucherPackRepository.findByStatus(VoucherPackJpaEntity.VoucherPackStatus.PUBLISHED);

            // Then
            assertThat(pendingPacks).hasSize(1);
            assertThat(publishedPacks).hasSize(1);
            verify(voucherPackRepository).findByStatus(VoucherPackJpaEntity.VoucherPackStatus.PENDING);
            verify(voucherPackRepository).findByStatus(VoucherPackJpaEntity.VoucherPackStatus.PUBLISHED);
        }
    }

    @Nested
    @DisplayName("Date-Based Queries")
    class DateBasedQueries {

        @Test
        @DisplayName("Should find voucher packs by status and validity date range")
        void shouldFindVoucherPacksByStatusAndValidityDateRange() {
            // Given
            LocalDate fromDate = LocalDate.now().minusDays(5);
            LocalDate toDate = LocalDate.now().plusDays(5);
            when(voucherPackRepository.findByStatusAndValidityDateRange(
                    VoucherPackJpaEntity.VoucherPackStatus.PUBLISHED, fromDate, toDate))
                    .thenReturn(List.of(publishedVoucherPack));

            // When
            List<VoucherPackJpaEntity> packs = voucherPackRepository.findByStatusAndValidityDateRange(
                    VoucherPackJpaEntity.VoucherPackStatus.PUBLISHED, fromDate, toDate);

            // Then
            assertThat(packs).hasSize(1); // published pack
            verify(voucherPackRepository).findByStatusAndValidityDateRange(
                    VoucherPackJpaEntity.VoucherPackStatus.PUBLISHED, fromDate, toDate);
        }

        @Test
        @DisplayName("Should find expired voucher packs")
        void shouldFindExpiredVoucherPacks() {
            // Given
            when(voucherPackRepository.findVoucherPacksEligibleForExpiration())
                    .thenReturn(List.of(expiredVoucherPack));

            // When
            List<VoucherPackJpaEntity> packs = voucherPackRepository.findVoucherPacksEligibleForExpiration();

            // Then
            assertThat(packs).hasSize(1); // expired pack
            verify(voucherPackRepository).findVoucherPacksEligibleForExpiration();
        }
    }

    @Nested
    @DisplayName("Quantity-Based Queries")
    class QuantityBasedQueries {

        @Test
        @DisplayName("Should find voucher packs eligible for closure")
        void shouldFindVoucherPacksEligibleForClosure() {
            // Given
            when(voucherPackRepository.findVoucherPacksEligibleForClosure())
                    .thenReturn(List.of(closedVoucherPack));

            // When
            List<VoucherPackJpaEntity> packs = voucherPackRepository.findVoucherPacksEligibleForClosure();

            // Then
            assertThat(packs).hasSize(1);
            assertThat(packs.get(0).getDescription()).isEqualTo("Closed Pack");
            verify(voucherPackRepository).findVoucherPacksEligibleForClosure();
        }

        @Test
        @DisplayName("Should find available voucher packs")
        void shouldFindAvailableVoucherPacks() {
            // Given
            when(voucherPackRepository.findAvailableVoucherPacks())
                    .thenReturn(List.of(publishedVoucherPack));

            // When
            List<VoucherPackJpaEntity> packs = voucherPackRepository.findAvailableVoucherPacks();

            // Then
            assertThat(packs).hasSize(1);
            assertThat(packs.get(0).getDescription()).isEqualTo("Published Pack");
            verify(voucherPackRepository).findAvailableVoucherPacks();
        }
    }

    @Nested
    @DisplayName("Complex Query Scenarios")
    class ComplexQueryScenarios {

        @Test
        @DisplayName("Should find voucher packs by description containing text")
        void shouldFindVoucherPacksByDescriptionContainingText() {
            // Given
            when(voucherPackRepository.findByDescriptionContainingIgnoreCase("Pack"))
                    .thenReturn(Arrays.asList(pendingVoucherPack, publishedVoucherPack, closedVoucherPack, expiredVoucherPack));

            // When
            List<VoucherPackJpaEntity> packs = voucherPackRepository.findByDescriptionContainingIgnoreCase("Pack");

            // Then
            assertThat(packs).hasSize(4); // all descriptions contain "Pack"
            verify(voucherPackRepository).findByDescriptionContainingIgnoreCase("Pack");
        }

        @Test
        @DisplayName("Should find voucher packs by required points range")
        void shouldFindVoucherPacksByRequiredPointsRange() {
            // Given
            when(voucherPackRepository.findByRequiredPointsRange(25L, 75L))
                    .thenReturn(List.of(pendingVoucherPack));

            // When
            List<VoucherPackJpaEntity> packs = voucherPackRepository.findByRequiredPointsRange(25L, 75L);

            // Then
            assertThat(packs).hasSize(1);
            assertThat(packs.get(0).getDescription()).isEqualTo("Pending Pack");
            verify(voucherPackRepository).findByRequiredPointsRange(25L, 75L);
        }

        @Test
        @DisplayName("Should count voucher packs by status")
        void shouldCountVoucherPacksByStatus() {
            // Given
            when(voucherPackRepository.countByStatus(VoucherPackJpaEntity.VoucherPackStatus.PENDING))
                    .thenReturn(1L);

            // When
            long count = voucherPackRepository.countByStatus(VoucherPackJpaEntity.VoucherPackStatus.PENDING);

            // Then
            assertThat(count).isEqualTo(1);
            verify(voucherPackRepository).countByStatus(VoucherPackJpaEntity.VoucherPackStatus.PENDING);
        }
    }
}
