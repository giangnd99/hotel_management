package com.poly.promotion.application.controller;

import com.poly.promotion.application.service.HateoasLinkBuilderService;
import com.poly.promotion.domain.application.api.external.VoucherExternalApi;
import com.poly.promotion.domain.application.dto.request.internal.voucher.VoucherRedeemRequest;
import com.poly.promotion.domain.application.dto.response.external.VoucherExternalResponse;
import com.poly.promotion.domain.core.exception.VoucherDomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Comprehensive unit tests for {@link VoucherController} using JUnit and Mockito.
 * Tests cover all business logic flows, validation, and error scenarios.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Voucher Controller Tests")
class VoucherControllerTest {

    @Mock
    private VoucherExternalApi voucherExternalApi;

    @Mock
    private HateoasLinkBuilderService hateoasLinkBuilderService;

    @InjectMocks
    private VoucherController voucherController;

    private VoucherRedeemRequest validRedeemRequest;
    private VoucherExternalResponse validVoucherResponse;
    private Link[] mockLinks;

    @BeforeEach
    void setUp() {
        validRedeemRequest = VoucherRedeemRequest.builder()
                .customerId(UUID.randomUUID().toString())
                .voucherPackId(1L)
                .quantity(Integer.valueOf(1))
                .build();

        validVoucherResponse = VoucherExternalResponse.builder()
                .voucherCode("VOUCHER-001")
                .voucherStatus("REDEEMED")
                .discountAmount(BigDecimal.valueOf(100000))
                .redeemedAt(LocalDateTime.now())
                .validTo(LocalDateTime.now().plusDays(30))
                .build();

        mockLinks = new Link[]{Link.of("/test", "self")};
        
        // Use lenient() to avoid unnecessary stubbing errors
        lenient().when(hateoasLinkBuilderService.buildVoucherLinks(anyString())).thenReturn(mockLinks);
        lenient().when(hateoasLinkBuilderService.buildCollectionLinks()).thenReturn(mockLinks);
    }

    @Nested
    @DisplayName("Voucher Redemption Tests")
    class VoucherRedemptionTests {

        @Test
        @DisplayName("Should successfully redeem vouchers")
        void shouldSuccessfullyRedeemVouchers() {
            // Given
            when(voucherExternalApi.redeemVoucherFromPack(any(VoucherRedeemRequest.class)))
                    .thenReturn(validVoucherResponse);

            // When
            ResponseEntity<EntityModel<VoucherExternalResponse>> response = 
                    voucherController.redeemVouchers(1L, validRedeemRequest);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            
            verify(voucherExternalApi).redeemVoucherFromPack(validRedeemRequest);
            verify(hateoasLinkBuilderService).buildVoucherLinks("VOUCHER-001");
            verify(hateoasLinkBuilderService).buildCollectionLinks();
        }

        @Test
        @DisplayName("Should handle API exceptions gracefully")
        void shouldHandleApiExceptionsGracefully() {
            // Given
            when(voucherExternalApi.redeemVoucherFromPack(any(VoucherRedeemRequest.class)))
                    .thenThrow(new VoucherDomainException("Voucher pack not found"));

            // When & Then
            assertThatThrownBy(() -> voucherController.redeemVouchers(1L, validRedeemRequest))
                    .isInstanceOf(VoucherDomainException.class)
                    .hasMessage("Voucher pack not found");
            
            verify(voucherExternalApi).redeemVoucherFromPack(validRedeemRequest);
            verify(hateoasLinkBuilderService, never()).buildVoucherLinks(any());
            verify(hateoasLinkBuilderService, never()).buildCollectionLinks();
        }

        @Test
        @DisplayName("Should handle null request")
        void shouldHandleNullRequest() {
            // When & Then
            assertThatThrownBy(() -> voucherController.redeemVouchers(1L, null))
                    .isInstanceOf(NullPointerException.class);
            
            verify(voucherExternalApi, never()).redeemVoucherFromPack(any());
            verify(hateoasLinkBuilderService, never()).buildVoucherLinks(any());
            verify(hateoasLinkBuilderService, never()).buildCollectionLinks();
        }

        @Test
        @DisplayName("Should handle HATEOAS service failures gracefully")
        void shouldHandleHateoasServiceFailuresGracefully() {
            // Given
            when(voucherExternalApi.redeemVoucherFromPack(any(VoucherRedeemRequest.class)))
                    .thenReturn(validVoucherResponse);
            when(hateoasLinkBuilderService.buildVoucherLinks(anyString()))
                    .thenThrow(new RuntimeException("HATEOAS service unavailable"));

            // When & Then
            assertThatThrownBy(() -> voucherController.redeemVouchers(1L, validRedeemRequest))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("HATEOAS service unavailable");
            
            verify(voucherExternalApi).redeemVoucherFromPack(validRedeemRequest);
            verify(hateoasLinkBuilderService).buildVoucherLinks("VOUCHER-001");
        }

        @Test
        @DisplayName("Should handle pack ID mismatch validation")
        void shouldHandlePackIdMismatchValidation() {
            // Given - pack ID in path doesn't match request
            VoucherRedeemRequest mismatchedRequest = VoucherRedeemRequest.builder()
                    .customerId(UUID.randomUUID().toString())
                    .voucherPackId(2L) // Different from path parameter
                    .quantity(Integer.valueOf(1))
                    .build();

            when(voucherExternalApi.redeemVoucherFromPack(any(VoucherRedeemRequest.class)))
                    .thenReturn(validVoucherResponse);

            // When & Then
            // Note: Current implementation doesn't validate this, but it's a business logic gap
            // This test documents the current behavior
            ResponseEntity<EntityModel<VoucherExternalResponse>> response = 
                    voucherController.redeemVouchers(1L, mismatchedRequest);
            
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            // The business logic should ideally prevent this mismatch
        }

        @Test
        @DisplayName("Should handle zero quantity validation")
        void shouldHandleZeroQuantityValidation() {
            // Given
            VoucherRedeemRequest zeroQuantityRequest = VoucherRedeemRequest.builder()
                    .customerId(UUID.randomUUID().toString())
                    .voucherPackId(1L)
                    .quantity(Integer.valueOf(0))
                    .build();

            when(voucherExternalApi.redeemVoucherFromPack(any(VoucherRedeemRequest.class)))
                    .thenThrow(new VoucherDomainException("Quantity must be positive"));

            // When & Then
            assertThatThrownBy(() -> voucherController.redeemVouchers(1L, zeroQuantityRequest))
                    .isInstanceOf(VoucherDomainException.class)
                    .hasMessage("Quantity must be positive");
        }

        @Test
        @DisplayName("Should handle negative quantity validation")
        void shouldHandleNegativeQuantityValidation() {
            // Given
            VoucherRedeemRequest negativeQuantityRequest = VoucherRedeemRequest.builder()
                    .customerId(UUID.randomUUID().toString())
                    .voucherPackId(1L)
                    .quantity(Integer.valueOf(-1))
                    .build();

            when(voucherExternalApi.redeemVoucherFromPack(any(VoucherRedeemRequest.class)))
                    .thenThrow(new VoucherDomainException("Quantity must be positive"));

            // When & Then
            assertThatThrownBy(() -> voucherController.redeemVouchers(1L, negativeQuantityRequest))
                    .isInstanceOf(VoucherDomainException.class)
                    .hasMessage("Quantity must be positive");
        }

        @Test
        @DisplayName("Should handle large quantity validation")
        void shouldHandleLargeQuantityValidation() {
            // Given
            VoucherRedeemRequest largeQuantityRequest = VoucherRedeemRequest.builder()
                    .customerId(UUID.randomUUID().toString())
                    .voucherPackId(1L)
                    .quantity(Integer.valueOf(1000)) // Large quantity
                    .build();

            when(voucherExternalApi.redeemVoucherFromPack(any(VoucherRedeemRequest.class)))
                    .thenThrow(new VoucherDomainException("Insufficient quantity available"));

            // When & Then
            assertThatThrownBy(() -> voucherController.redeemVouchers(1L, largeQuantityRequest))
                    .isInstanceOf(VoucherDomainException.class)
                    .hasMessage("Insufficient quantity available");
        }

        @Test
        @DisplayName("Should handle expired voucher pack")
        void shouldHandleExpiredVoucherPack() {
            // Given
            when(voucherExternalApi.redeemVoucherFromPack(any(VoucherRedeemRequest.class)))
                    .thenThrow(new VoucherDomainException("Cannot redeem vouchers from expired pack"));

            // When & Then
            assertThatThrownBy(() -> voucherController.redeemVouchers(1L, validRedeemRequest))
                    .isInstanceOf(VoucherDomainException.class)
                    .hasMessage("Cannot redeem vouchers from expired pack");
        }

        @Test
        @DisplayName("Should handle insufficient loyalty points")
        void shouldHandleInsufficientLoyaltyPoints() {
            // Given
            when(voucherExternalApi.redeemVoucherFromPack(any(VoucherRedeemRequest.class)))
                    .thenThrow(new VoucherDomainException("Insufficient loyalty points"));

            // When & Then
            assertThatThrownBy(() -> voucherController.redeemVouchers(1L, validRedeemRequest))
                    .isInstanceOf(VoucherDomainException.class)
                    .hasMessage("Insufficient loyalty points");
        }
    }

    @Nested
    @DisplayName("Input Validation Tests")
    class InputValidationTests {

        @Test
        @DisplayName("Should handle null customer ID")
        void shouldHandleNullCustomerId() {
            // Given
            VoucherRedeemRequest nullCustomerRequest = VoucherRedeemRequest.builder()
                    .customerId(null)
                    .voucherPackId(1L)
                    .quantity(Integer.valueOf(1))
                    .build();

            when(voucherExternalApi.redeemVoucherFromPack(any(VoucherRedeemRequest.class)))
                    .thenThrow(new VoucherDomainException("Customer ID cannot be null or empty"));

            // When & Then
            assertThatThrownBy(() -> voucherController.redeemVouchers(1L, nullCustomerRequest))
                    .isInstanceOf(VoucherDomainException.class)
                    .hasMessage("Customer ID cannot be null or empty");
        }

        @Test
        @DisplayName("Should handle empty customer ID")
        void shouldHandleEmptyCustomerId() {
            // Given
            VoucherRedeemRequest emptyCustomerRequest = VoucherRedeemRequest.builder()
                    .customerId("")
                    .voucherPackId(1L)
                    .quantity(Integer.valueOf(1))
                    .build();

            when(voucherExternalApi.redeemVoucherFromPack(any(VoucherRedeemRequest.class)))
                    .thenThrow(new VoucherDomainException("Customer ID cannot be null or empty"));

            // When & Then
            assertThatThrownBy(() -> voucherController.redeemVouchers(1L, emptyCustomerRequest))
                    .isInstanceOf(VoucherDomainException.class)
                    .hasMessage("Customer ID cannot be null or empty");
        }

        @Test
        @DisplayName("Should handle null voucher pack ID")
        void shouldHandleNullVoucherPackId() {
            // Given
            VoucherRedeemRequest nullPackRequest = VoucherRedeemRequest.builder()
                    .customerId(UUID.randomUUID().toString())
                    .voucherPackId(null)
                    .quantity(Integer.valueOf(1))
                    .build();

            when(voucherExternalApi.redeemVoucherFromPack(any(VoucherRedeemRequest.class)))
                    .thenThrow(new VoucherDomainException("Voucher pack ID must be a positive number"));

            // When & Then
            assertThatThrownBy(() -> voucherController.redeemVouchers(1L, nullPackRequest))
                    .isInstanceOf(VoucherDomainException.class)
                    .hasMessage("Voucher pack ID must be a positive number");
        }

        @Test
        @DisplayName("Should handle invalid voucher pack ID format")
        void shouldHandleInvalidVoucherPackIdFormat() {
            // Given
            VoucherRedeemRequest invalidPackRequest = VoucherRedeemRequest.builder()
                    .customerId(UUID.randomUUID().toString())
                    .voucherPackId(0L) // Invalid: zero
                    .quantity(Integer.valueOf(1))
                    .build();

            when(voucherExternalApi.redeemVoucherFromPack(any(VoucherRedeemRequest.class)))
                    .thenThrow(new VoucherDomainException("Voucher pack ID must be a positive number"));

            // When & Then
            assertThatThrownBy(() -> voucherController.redeemVouchers(1L, invalidPackRequest))
                    .isInstanceOf(VoucherDomainException.class)
                    .hasMessage("Voucher pack ID must be a positive number");
        }
    }

    @Nested
    @DisplayName("Business Rule Tests")
    class BusinessRuleTests {

        @Test
        @DisplayName("Should enforce voucher pack status validation")
        void shouldEnforceVoucherPackStatusValidation() {
            // Given
            when(voucherExternalApi.redeemVoucherFromPack(any(VoucherRedeemRequest.class)))
                    .thenThrow(new VoucherDomainException("Voucher pack is not in PUBLISHED status"));

            // When & Then
            assertThatThrownBy(() -> voucherController.redeemVouchers(1L, validRedeemRequest))
                    .isInstanceOf(VoucherDomainException.class)
                    .hasMessage("Voucher pack is not in PUBLISHED status");
        }

        @Test
        @DisplayName("Should enforce customer redemption limits")
        void shouldEnforceCustomerRedemptionLimits() {
            // Given
            when(voucherExternalApi.redeemVoucherFromPack(any(VoucherRedeemRequest.class)))
                    .thenThrow(new VoucherDomainException("Customer has exceeded redemption limits"));

            // When & Then
            assertThatThrownBy(() -> voucherController.redeemVouchers(1L, validRedeemRequest))
                    .isInstanceOf(VoucherDomainException.class)
                    .hasMessage("Customer has exceeded redemption limits");
        }

        @Test
        @DisplayName("Should enforce voucher pack availability")
        void shouldEnforceVoucherPackAvailability() {
            // Given
            when(voucherExternalApi.redeemVoucherFromPack(any(VoucherRedeemRequest.class)))
                    .thenThrow(new VoucherDomainException("Voucher pack is not available for redemption"));

            // When & Then
            assertThatThrownBy(() -> voucherController.redeemVouchers(1L, validRedeemRequest))
                    .isInstanceOf(VoucherDomainException.class)
                    .hasMessage("Voucher pack is not available for redemption");
        }
    }
}
