package com.poly.promotion.application.controller;

import com.poly.promotion.application.service.HateoasLinkBuilderService;
import com.poly.promotion.domain.application.api.external.VoucherPackExternalApi;
import com.poly.promotion.domain.application.api.external.VoucherExternalApi;
import com.poly.promotion.domain.application.dto.request.internal.voucher.VoucherRedeemRequest;
import com.poly.promotion.domain.application.dto.response.external.VoucherPackExternalResponse;
import com.poly.promotion.domain.application.dto.response.external.VoucherExternalResponse;
import com.poly.promotion.domain.core.exception.VoucherDomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.Link;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Simple unit tests for {@link VoucherPackExternalController} using JUnit and Mockito.
 * Tests focus on core controller logic without complex HATEOAS structures.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Voucher Pack External Controller Tests")
class VoucherPackExternalControllerTest {

    @Mock
    private VoucherPackExternalApi voucherPackExternalApi;

    @Mock
    private VoucherExternalApi voucherExternalApi;

    @Mock
    private HateoasLinkBuilderService hateoasLinkBuilderService;

    @InjectMocks
    private VoucherPackExternalController voucherPackExternalController;

    private VoucherPackExternalResponse validVoucherPackResponse;
    private VoucherExternalResponse validVoucherResponse;
    private VoucherRedeemRequest validRedeemRequest;

    @BeforeEach
    void setUp() {
        validVoucherPackResponse = VoucherPackExternalResponse.builder()
                .description("Get 100,000 VND off your next booking")
                .discountAmount(BigDecimal.valueOf(100000))
                .validRange("30 DAYS")
                .requiredPoints(50L)
                .quantity(100)
                .validFrom(LocalDateTime.now().toLocalDate())
                .validTo(LocalDateTime.now().plusDays(90).toLocalDate())
                .packStatus("PUBLISHED")
                .build();

        validVoucherResponse = VoucherExternalResponse.builder()
                .voucherCode("VOUCHER-001")
                .voucherStatus("REDEEMED")
                .discountAmount(BigDecimal.valueOf(100000))
                .redeemedAt(LocalDateTime.now())
                .validTo(LocalDateTime.now().plusDays(30))
                .build();

        validRedeemRequest = VoucherRedeemRequest.builder()
                .customerId(UUID.randomUUID().toString())
                .voucherPackId(1L)
                .quantity(Integer.valueOf(1))
                .build();

        // Mock HATEOAS service to return valid links
        lenient().when(hateoasLinkBuilderService.buildVoucherPackLinks(anyLong())).thenReturn(new Link[]{Link.of("/test", "self")});
        lenient().when(hateoasLinkBuilderService.buildVoucherLinks(anyString())).thenReturn(new Link[]{Link.of("/test", "self")});
        lenient().when(hateoasLinkBuilderService.buildCollectionLinks()).thenReturn(new Link[]{Link.of("/test", "self")});
    }

    @Test
    @DisplayName("Should return available voucher packs")
    void shouldReturnAvailableVoucherPacks() {
        // Given
        List<VoucherPackExternalResponse> packs = Arrays.asList(validVoucherPackResponse);
        when(voucherPackExternalApi.getAllAvailableVoucherPacks()).thenReturn(packs);

        // When
        ResponseEntity<?> response = voucherPackExternalController.getAvailableVoucherPacks();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        verify(voucherPackExternalApi).getAllAvailableVoucherPacks();
    }

    @Test
    @DisplayName("Should return empty list when no packs available")
    void shouldReturnEmptyListWhenNoPacksAvailable() {
        // Given
        when(voucherPackExternalApi.getAllAvailableVoucherPacks()).thenReturn(Arrays.asList());

        // When
        ResponseEntity<?> response = voucherPackExternalController.getAvailableVoucherPacks();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        
        verify(voucherPackExternalApi).getAllAvailableVoucherPacks();
    }

    @Test
    @DisplayName("Should handle API exceptions gracefully")
    void shouldHandleApiExceptionsGracefully() {
        // Given
        when(voucherPackExternalApi.getAllAvailableVoucherPacks())
                .thenThrow(new VoucherDomainException("Service unavailable"));

        // When & Then
        assertThatThrownBy(() -> voucherPackExternalController.getAvailableVoucherPacks())
                .isInstanceOf(VoucherDomainException.class)
                .hasMessage("Service unavailable");
        
        verify(voucherPackExternalApi).getAllAvailableVoucherPacks();
    }

    @Test
    @DisplayName("Should successfully redeem voucher")
    void shouldSuccessfullyRedeemVoucher() {
        // Given
        when(voucherExternalApi.redeemVoucherFromPack(any(VoucherRedeemRequest.class)))
                .thenReturn(validVoucherResponse);

        // When
        ResponseEntity<?> response = voucherPackExternalController.redeemVouchers(validRedeemRequest, "customer-123");

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        verify(voucherExternalApi).redeemVoucherFromPack(validRedeemRequest);
    }

    @Test
    @DisplayName("Should handle redemption exceptions gracefully")
    void shouldHandleRedemptionExceptionsGracefully() {
        // Given
        when(voucherExternalApi.redeemVoucherFromPack(any(VoucherRedeemRequest.class)))
                .thenThrow(new VoucherDomainException("Insufficient loyalty points"));

        // When & Then
        assertThatThrownBy(() -> voucherPackExternalController.redeemVouchers(validRedeemRequest, "customer-123"))
                .isInstanceOf(VoucherDomainException.class)
                .hasMessage("Insufficient loyalty points");
        
        verify(voucherExternalApi).redeemVoucherFromPack(validRedeemRequest);
    }

    @Test
    @DisplayName("Should successfully apply voucher")
    void shouldSuccessfullyApplyVoucher() {
        // Given
        String voucherCode = "VOUCHER-001";
        String customerId = UUID.randomUUID().toString();
        when(voucherExternalApi.applyVoucher(voucherCode, customerId))
                .thenReturn(validVoucherResponse);

        // When
        ResponseEntity<?> response = voucherPackExternalController.applyVoucher(voucherCode, customerId);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        verify(voucherExternalApi).applyVoucher(voucherCode, customerId);
    }

    @Test
    @DisplayName("Should handle application exceptions gracefully")
    void shouldHandleApplicationExceptionsGracefully() {
        // Given
        String voucherCode = "VOUCHER-001";
        String customerId = UUID.randomUUID().toString();
        when(voucherExternalApi.applyVoucher(voucherCode, customerId))
                .thenThrow(new VoucherDomainException("Voucher not found"));

        // When & Then
        assertThatThrownBy(() -> voucherPackExternalController.applyVoucher(voucherCode, customerId))
                .isInstanceOf(VoucherDomainException.class)
                .hasMessage("Voucher not found");
        
        verify(voucherExternalApi).applyVoucher(voucherCode, customerId);
    }
}
