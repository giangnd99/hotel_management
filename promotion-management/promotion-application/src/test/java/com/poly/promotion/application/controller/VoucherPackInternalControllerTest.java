package com.poly.promotion.application.controller;

import com.poly.promotion.application.service.HateoasLinkBuilderService;
import com.poly.promotion.domain.application.api.internal.VoucherPackInternalApi;
import com.poly.promotion.domain.application.dto.request.internal.voucherpack.VoucherPackCreateRequest;
import com.poly.promotion.domain.application.dto.request.internal.voucherpack.VoucherPackUpdateRequest;
import com.poly.promotion.domain.application.dto.response.internal.VoucherPackInternalResponse;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Simple unit tests for {@link VoucherPackInternalController} using JUnit and Mockito.
 * Tests focus on core controller logic without complex HATEOAS structures.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Voucher Pack Internal Controller Tests")
class VoucherPackInternalControllerTest {

    @Mock
    private VoucherPackInternalApi voucherPackInternalApi;

    @Mock
    private HateoasLinkBuilderService hateoasLinkBuilderService;

    @InjectMocks
    private VoucherPackInternalController voucherPackInternalController;

    private VoucherPackCreateRequest validCreateRequest;
    private VoucherPackUpdateRequest validUpdateRequest;
    private VoucherPackInternalResponse validVoucherPackResponse;

    @BeforeEach
    void setUp() {
        validCreateRequest = VoucherPackCreateRequest.builder()
                .description("Summer Sale Pack")
                .discountAmount(BigDecimal.valueOf(100000))
                .voucherValidRange("30 DAYS")
                .requiredPoints(50L)
                .quantity(100)
                .packValidFrom(LocalDate.now())
                .packValidTo(LocalDate.now().plusDays(90))
                .build();

        validUpdateRequest = VoucherPackUpdateRequest.builder()
                .description("Updated Summer Sale Pack")
                .discountAmount(BigDecimal.valueOf(150000))
                .voucherValidRange("45 DAYS")
                .requiredPoints(75L)
                .quantity(150)
                .packValidFrom(LocalDate.now())
                .packValidTo(LocalDate.now().plusDays(120))
                .build();

        validVoucherPackResponse = VoucherPackInternalResponse.builder()
                .id(1L)
                .description("Summer Sale Pack")
                .discountAmount(BigDecimal.valueOf(100000))
                .validRange("30 DAYS")
                .requiredPoints(50L)
                .quantity(100)
                .validFrom(LocalDate.now())
                .validTo(LocalDate.now().plusDays(90))
                .status("PENDING")
                .build();

        // Mock HATEOAS service to return valid links
        lenient().when(hateoasLinkBuilderService.buildVoucherPackInternalLinks(anyLong())).thenReturn(new Link[]{Link.of("/test", "self")});
        lenient().when(hateoasLinkBuilderService.buildCollectionLinks()).thenReturn(new Link[]{Link.of("/test", "self")});
    }

    @Test
    @DisplayName("Should return all voucher packs")
    void shouldReturnAllVoucherPacks() {
        // Given
        List<VoucherPackInternalResponse> packs = Arrays.asList(validVoucherPackResponse);
        when(voucherPackInternalApi.getAllVoucherPacks(any(String[].class))).thenReturn(packs);

        // When
        ResponseEntity<?> response = voucherPackInternalController.getAllVoucherPacks();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        verify(voucherPackInternalApi).getAllVoucherPacks();
    }

    @Test
    @DisplayName("Should return empty list when no packs available")
    void shouldReturnEmptyListWhenNoPacksAvailable() {
        // Given
        when(voucherPackInternalApi.getAllVoucherPacks(any(String[].class))).thenReturn(Arrays.asList());

        // When
        ResponseEntity<?> response = voucherPackInternalController.getAllVoucherPacks();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        
        verify(voucherPackInternalApi).getAllVoucherPacks();
    }

    @Test
    @DisplayName("Should handle API exceptions gracefully")
    void shouldHandleApiExceptionsGracefully() {
        // Given
        when(voucherPackInternalApi.getAllVoucherPacks(any(String[].class)))
                .thenThrow(new VoucherDomainException("Service unavailable"));

        // When & Then
        assertThatThrownBy(() -> voucherPackInternalController.getAllVoucherPacks())
                .isInstanceOf(VoucherDomainException.class)
                .hasMessage("Service unavailable");
        
        verify(voucherPackInternalApi).getAllVoucherPacks();
    }

    @Test
    @DisplayName("Should successfully create voucher pack")
    void shouldSuccessfullyCreateVoucherPack() {
        // Given
        when(voucherPackInternalApi.createVoucherPack(any(VoucherPackCreateRequest.class)))
                .thenReturn(validVoucherPackResponse);

        // When
        ResponseEntity<?> response = voucherPackInternalController.createVoucherPack(validCreateRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        
        verify(voucherPackInternalApi).createVoucherPack(validCreateRequest);
    }

    @Test
    @DisplayName("Should handle creation exceptions gracefully")
    void shouldHandleCreationExceptionsGracefully() {
        // Given
        when(voucherPackInternalApi.createVoucherPack(any(VoucherPackCreateRequest.class)))
                .thenThrow(new VoucherDomainException("Invalid voucher pack data"));

        // When & Then
        assertThatThrownBy(() -> voucherPackInternalController.createVoucherPack(validCreateRequest))
                .isInstanceOf(VoucherDomainException.class)
                .hasMessage("Invalid voucher pack data");
        
        verify(voucherPackInternalApi).createVoucherPack(validCreateRequest);
    }

    @Test
    @DisplayName("Should successfully update voucher pack")
    void shouldSuccessfullyUpdateVoucherPack() {
        // Given
        Long packId = 1L;
        when(voucherPackInternalApi.updateVoucherPack(anyLong(), any(VoucherPackUpdateRequest.class)))
                .thenReturn(validVoucherPackResponse);

        // When
        ResponseEntity<?> response = voucherPackInternalController.updateVoucherPack(packId, validUpdateRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        verify(voucherPackInternalApi).updateVoucherPack(packId, validUpdateRequest);
    }

    @Test
    @DisplayName("Should handle update exceptions gracefully")
    void shouldHandleUpdateExceptionsGracefully() {
        // Given
        Long packId = 1L;
        when(voucherPackInternalApi.updateVoucherPack(anyLong(), any(VoucherPackUpdateRequest.class)))
                .thenThrow(new VoucherDomainException("Voucher pack cannot be updated"));

        // When & Then
        assertThatThrownBy(() -> voucherPackInternalController.updateVoucherPack(packId, validUpdateRequest))
                .isInstanceOf(VoucherDomainException.class)
                .hasMessage("Voucher pack cannot be updated");
        
        verify(voucherPackInternalApi).updateVoucherPack(packId, validUpdateRequest);
    }

    @Test
    @DisplayName("Should successfully close voucher pack")
    void shouldSuccessfullyCloseVoucherPack() {
        // Given
        Long packId = 1L;
        doNothing().when(voucherPackInternalApi).closeVoucherPack(anyLong());

        // When
        ResponseEntity<?> response = voucherPackInternalController.closeVoucherPack(packId);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        verify(voucherPackInternalApi).closeVoucherPack(packId);
    }

    @Test
    @DisplayName("Should handle close exceptions gracefully")
    void shouldHandleCloseExceptionsGracefully() {
        // Given
        Long packId = 1L;
        doThrow(new VoucherDomainException("Voucher pack cannot be closed"))
                .when(voucherPackInternalApi).closeVoucherPack(anyLong());

        // When & Then
        assertThatThrownBy(() -> voucherPackInternalController.closeVoucherPack(packId))
                .isInstanceOf(VoucherDomainException.class)
                .hasMessage("Voucher pack cannot be closed");
        
        verify(voucherPackInternalApi).closeVoucherPack(packId);
    }

    @Test
    @DisplayName("Should successfully publish voucher pack")
    void shouldSuccessfullyPublishVoucherPack() {
        // Given
        Long packId = 1L;
        when(voucherPackInternalApi.publishVoucherPack(anyLong())).thenReturn(validVoucherPackResponse);

        // When
        ResponseEntity<?> response = voucherPackInternalController.publishVoucherPack(packId);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        verify(voucherPackInternalApi).publishVoucherPack(packId);
    }

    @Test
    @DisplayName("Should handle publish exceptions gracefully")
    void shouldHandlePublishExceptionsGracefully() {
        // Given
        Long packId = 1L;
        when(voucherPackInternalApi.publishVoucherPack(anyLong()))
                .thenThrow(new VoucherDomainException("Voucher pack cannot be published"));

        // When & Then
        assertThatThrownBy(() -> voucherPackInternalController.publishVoucherPack(packId))
                .isInstanceOf(VoucherDomainException.class)
                .hasMessage("Voucher pack cannot be published");
        
        verify(voucherPackInternalApi).publishVoucherPack(packId);
    }
}
