package com.poly.promotion.domain.application.service.impl;

import com.poly.promotion.domain.application.service.VoucherPackService;
import com.poly.promotion.domain.application.spi.repository.VoucherRepository;
import com.poly.promotion.domain.core.entity.Voucher;
import com.poly.promotion.domain.core.entity.VoucherPack;
import com.poly.promotion.domain.core.exception.PromotionDomainException;
import com.poly.promotion.domain.core.valueobject.DateRange;
import com.poly.promotion.domain.core.valueobject.DiscountPercentage;
import com.poly.promotion.domain.core.valueobject.VoucherId;
import com.poly.promotion.domain.core.valueobject.VoucherPackId;
import com.poly.promotion.domain.core.valueobject.VoucherStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoucherServiceImplTest {

    @Mock
    private VoucherPackService voucherPackService;

    @Mock
    private VoucherRepository voucherRepository;

    private VoucherServiceImpl voucherService;

    @BeforeEach
    void setUp() {
        voucherService = new VoucherServiceImpl(voucherPackService, voucherRepository);
    }

    @Test
    void testGetAllVouchersWithStatus_ValidCustomerId_ReturnsVouchers() {
        // Arrange
        String customerId = UUID.randomUUID().toString();
        Voucher voucher = createTestVoucher(customerId);
        when(voucherRepository.getAllVouchersWithStatus(eq(customerId), eq(VoucherStatus.PENDING)))
            .thenReturn(List.of(voucher));

        // Act
        List<Voucher> result = voucherService.getAllVouchersWithStatus(customerId, VoucherStatus.PENDING);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(voucher.getId(), result.get(0).getId());
        verify(voucherRepository).getAllVouchersWithStatus(customerId, VoucherStatus.PENDING);
    }

    @Test
    void testGetAllVouchersWithStatus_InvalidCustomerId_ThrowsException() {
        // Act & Assert
        assertThrows(PromotionDomainException.class, () -> 
            voucherService.getAllVouchersWithStatus("invalid-uuid", VoucherStatus.PENDING));
    }

    @Test
    void testGetVoucherById_ValidId_ReturnsVoucher() {
        // Arrange
        Voucher voucher = createTestVoucher(UUID.randomUUID().toString());
        String voucherId = voucher.getId().getValue().toString();
        when(voucherRepository.getVoucherById(voucherId)).thenReturn(voucher);

        // Act
        Voucher result = voucherService.getVoucherById(voucherId);

        // Assert
        assertNotNull(result);
        assertEquals(voucher.getId(), result.getId());
        verify(voucherRepository).getVoucherById(voucherId);
    }

    @Test
    void testGetVoucherById_InvalidId_ThrowsException() {
        // Act & Assert
        assertThrows(PromotionDomainException.class, () -> 
            voucherService.getVoucherById("invalid-uuid"));
    }

    @Test
    void testGetVoucherById_NonExistentId_ThrowsException() {
        // Arrange
        String voucherId = UUID.randomUUID().toString();
        when(voucherRepository.getVoucherById(voucherId)).thenReturn(null);

        // Act & Assert
        assertThrows(PromotionDomainException.class, () -> 
            voucherService.getVoucherById(voucherId));
    }

    @Test
    void testRedeemVoucherFromPack_ValidRequest_ReturnsVoucher() {
        // Arrange
        String customerId = UUID.randomUUID().toString();
        Long packId = 1L;
        Integer quantity = 1;
        
        VoucherPack mockPack = createMockVoucherPack(packId);
        Voucher createdVoucher = createTestVoucher(customerId);
        
        when(voucherPackService.getVoucherPackById(packId)).thenReturn(mockPack);
        when(voucherRepository.isVoucherCodeExists(any())).thenReturn(false);
        when(voucherRepository.createVoucher(any())).thenReturn(createdVoucher);
        doNothing().when(voucherPackService).reduceVoucherPackStockAfterRedeem(eq(packId), eq(quantity));

        // Act
        Voucher result = voucherService.redeemVoucherFromPack(packId, customerId, quantity);

        // Assert
        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId().getValue().toString());
        assertEquals(packId, result.getVoucherPackId().getValue());
        assertEquals(VoucherStatus.PENDING, result.getVoucherStatus());
        
        verify(voucherPackService).reduceVoucherPackStockAfterRedeem(packId, quantity);
        verify(voucherRepository, times(quantity)).createVoucher(any());
    }

    @Test
    void testRedeemVoucherFromPack_InvalidCustomerId_ThrowsException() {
        // Act & Assert
        assertThrows(PromotionDomainException.class, () -> 
            voucherService.redeemVoucherFromPack(1L, "invalid-uuid", 1));
    }

    @Test
    void testRedeemVoucherFromPack_InvalidQuantity_ThrowsException() {
        // Act & Assert
        assertThrows(PromotionDomainException.class, () -> 
            voucherService.redeemVoucherFromPack(1L, UUID.randomUUID().toString(), 0));
    }

    @Test
    void testRedeemVoucherFromPack_PackCannotRedeem_ThrowsException() {
        // Arrange
        String customerId = UUID.randomUUID().toString();
        Long packId = 1L;
        Integer quantity = 1;
        
        VoucherPack mockPack = createMockVoucherPack(packId);
        when(mockPack.canRedeem(quantity)).thenReturn(false);
        when(voucherPackService.getVoucherPackById(packId)).thenReturn(mockPack);

        // Act & Assert
        assertThrows(PromotionDomainException.class, () -> 
            voucherService.redeemVoucherFromPack(packId, customerId, quantity));
    }

    @Test
    void testApplyVoucher_ValidVoucher_ReturnsUpdatedVoucher() {
        // Arrange
        String customerId = UUID.randomUUID().toString();
        Voucher voucher = createTestVoucher(customerId);
        voucher.setVoucherStatus(VoucherStatus.REDEEMED);
        voucher.setValidTo(LocalDateTime.now().plusDays(30));
        
        Voucher updatedVoucher = createTestVoucher(customerId);
        updatedVoucher.setVoucherStatus(VoucherStatus.USED);
        
        when(voucherRepository.getVoucherByCode(voucher.getVoucherCode())).thenReturn(voucher);
        when(voucherRepository.updateVoucher(any())).thenReturn(updatedVoucher);

        // Act
        Voucher result = voucherService.applyVoucher(voucher.getVoucherCode(), customerId);

        // Assert
        assertNotNull(result);
        assertEquals(VoucherStatus.USED, result.getVoucherStatus());
        verify(voucherRepository).getVoucherByCode(voucher.getVoucherCode());
        verify(voucherRepository).updateVoucher(any());
    }

    @Test
    void testApplyVoucher_InvalidVoucherCode_ThrowsException() {
        // Arrange
        String voucherCode = "INVALID-CODE";
        when(voucherRepository.getVoucherByCode(voucherCode)).thenReturn(null);

        // Act & Assert
        assertThrows(PromotionDomainException.class, () -> 
            voucherService.applyVoucher(voucherCode, UUID.randomUUID().toString()));
    }

    @Test
    void testApplyVoucher_WrongCustomer_ThrowsException() {
        // Arrange
        String customerId = UUID.randomUUID().toString();
        String wrongCustomerId = UUID.randomUUID().toString();
        Voucher voucher = createTestVoucher(customerId);
        
        when(voucherRepository.getVoucherByCode(voucher.getVoucherCode())).thenReturn(voucher);

        // Act & Assert
        assertThrows(PromotionDomainException.class, () -> 
            voucherService.applyVoucher(voucher.getVoucherCode(), wrongCustomerId));
    }

    @Test
    void testApplyVoucher_VoucherCannotBeUsed_ThrowsException() {
        // Arrange
        String customerId = UUID.randomUUID().toString();
        Voucher voucher = createTestVoucher(customerId);
        voucher.setVoucherStatus(VoucherStatus.PENDING); // Cannot be used
        
        when(voucherRepository.getVoucherByCode(voucher.getVoucherCode())).thenReturn(voucher);

        // Act & Assert
        assertThrows(PromotionDomainException.class, () -> 
            voucherService.applyVoucher(voucher.getVoucherCode(), customerId));
    }

    private Voucher createTestVoucher(String customerId) {
        Voucher tesVoucher = Voucher.builder()
                .customerId(new com.poly.domain.valueobject.CustomerId(UUID.fromString(customerId)))
                .voucherPackId(new VoucherPackId(1L))
                .voucherCode("TEST-" + UUID.randomUUID().toString().substring(0, 8))
                .discount(new DiscountPercentage(10.0))
                .redeemedAt(LocalDateTime.now())
                .validTo(LocalDateTime.now().plusDays(30))
                .voucherStatus(VoucherStatus.PENDING)
                .build();

        tesVoucher.setId(new VoucherId(UUID.randomUUID()));

        return tesVoucher;
    }

    private VoucherPack createMockVoucherPack(Long packId) {
        VoucherPack pack = mock(VoucherPack.class);
        when(pack.getId()).thenReturn(new VoucherPackId(packId));
        when(pack.getDiscountAmount()).thenReturn(new DiscountPercentage(10.0));
        when(pack.getVoucherValidRange()).thenReturn(new DateRange(30, ChronoUnit.DAYS));
        when(pack.canRedeem(any())).thenReturn(true);
        return pack;
    }
}
