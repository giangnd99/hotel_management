package com.poly.customerapplicationservice.service;

import com.poly.customerapplicationservice.command.EarnPointLoyaltyCommand;
import com.poly.customerapplicationservice.command.RedeemPointLoyaltyCommand;
import com.poly.customerapplicationservice.dto.LoyaltyPointDto;
import com.poly.customerapplicationservice.port.output.PromotionServicePort;
import com.poly.customerapplicationservice.shared.RedeemTargetType;
import com.poly.customerdomain.model.entity.Customer;
import com.poly.customerdomain.model.entity.LoyaltyPoint;
import com.poly.customerdomain.model.entity.LoyaltyTransaction;
import com.poly.customerdomain.model.entity.valueobject.Level;
import com.poly.customerdomain.model.entity.valueobject.Point;
import com.poly.customerdomain.model.exception.InvalidPointAmountException;
import com.poly.customerdomain.model.exception.LoyaltyNotFoundException;
import com.poly.customerdomain.model.exception.NotEnoughPointException;
import com.poly.customerdomain.output.CustomerRepository;
import com.poly.customerdomain.output.LoyaltyPointRepository;
import com.poly.customerdomain.output.LoyaltyTransactionRepository;
import com.poly.domain.valueobject.CustomerId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoyaltyApplicationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private LoyaltyPointRepository loyaltyPointRepository;

    @Mock
    private LoyaltyTransactionRepository  loyaltyTransactionRepository;

    @Mock
    private PromotionServicePort promotionServicePort;

    @InjectMocks
    private LoyaltyApplicationService loyaltyApplicationService;

    private EarnPointLoyaltyCommand command = new EarnPointLoyaltyCommand(UUID.randomUUID(), BigDecimal.valueOf(50), "Thưởng khi đặt phòng");


    @Test
    void earnPoint_successfully_adds_point_and_saves_transaction() {

        LoyaltyPoint mockLoyalty = LoyaltyPoint.createNew(CustomerId.from(command.getCustomerId()));
        when(loyaltyPointRepository.findByCustomerId(command.getCustomerId()))
                .thenReturn(Optional.of(mockLoyalty));

        // When
        LoyaltyPointDto result = loyaltyApplicationService.earnPoint(command);

        // Then
        assertEquals(Point.from(BigDecimal.valueOf(50)).getValue(), result.getPoints());
        verify(loyaltyTransactionRepository).save(any(LoyaltyTransaction.class));
        verify(loyaltyPointRepository).save(any(LoyaltyPoint.class));
    }

    @Test
    void earnPoint_should_throw_when_loyalty_not_found() {
        when(loyaltyPointRepository.findByCustomerId(command.getCustomerId()))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(LoyaltyNotFoundException.class, () -> loyaltyApplicationService.earnPoint(command));
    }

    @Test
    void earnPoint_should_throw_when_point_is_invalid() {
        command.setAmount(BigDecimal.valueOf(0));
        LoyaltyPoint mockLoyalty = LoyaltyPoint.createNew(CustomerId.from(command.getCustomerId()));

        when(loyaltyPointRepository.findByCustomerId(command.getCustomerId()))
                .thenReturn(Optional.of(mockLoyalty));

        // Then
        assertThrows(InvalidPointAmountException.class, () -> loyaltyApplicationService.earnPoint(command));
    }

    @Test
    void redeemPoint_shouldThrowNotEnoughPointException() {
        Customer customer = Customer.builder()
                .customerId(CustomerId.generate())
                .level(Level.BRONZE)
                .build();

        LoyaltyPoint loyaltyPoint = LoyaltyPoint.createNew(customer.getId());
        loyaltyPoint.addPoints(Point.from(BigDecimal.valueOf(500)));

        RedeemPointLoyaltyCommand command = new RedeemPointLoyaltyCommand();
        command.setCustomerId(customer.getId().getValue());
        command.setAmount(BigDecimal.valueOf(3000));
        command.setTargetType(RedeemTargetType.LEVEL_UP);

        when(loyaltyPointRepository.findByCustomerId(customer.getId().getValue())).thenReturn(Optional.of(loyaltyPoint));
        when(customerRepository.findById(customer.getId().getValue())).thenReturn(Optional.of(customer));

        assertThrows(NotEnoughPointException.class, () -> {
            loyaltyApplicationService.redeemPoint(command);
        });

    }

    @Test
    void redeemPoint_shouldUpgradeLevelSuccessfully() {
        Customer customer = Customer.builder()
                .customerId(CustomerId.generate())
                .level(Level.BRONZE)
                .build();

        LoyaltyPoint loyaltyPoint = LoyaltyPoint.createNew(customer.getId());
        loyaltyPoint.addPoints(Point.from(BigDecimal.valueOf(5000000)));

        RedeemPointLoyaltyCommand command = new RedeemPointLoyaltyCommand();
        command.setCustomerId(customer.getId().getValue());
        command.setAmount(BigDecimal.valueOf(Level.BRONZE.ordinal()));
        command.setTargetType(RedeemTargetType.LEVEL_UP);
        command.setDescription("Nâng hạng");

        when(loyaltyPointRepository.findByCustomerId(customer.getId().getValue())).thenReturn(Optional.of(loyaltyPoint));
        when(customerRepository.findById(customer.getId().getValue())).thenReturn(Optional.of(customer));

        LoyaltyPointDto result = loyaltyApplicationService.redeemPoint(command);

        assertEquals(Level.SILVER, customer.getLevel());
        verify(customerRepository).save(customer);
        verify(loyaltyTransactionRepository, times(1)).save(any());
    }


    @Test
    void redeemPoint_shouldRedeemVoucherSuccessfully() {
        Customer customer = Customer.builder()
                .customerId(CustomerId.generate())
                .level(Level.BRONZE)
                .build();

        RedeemPointLoyaltyCommand command = new RedeemPointLoyaltyCommand();
        command.setCustomerId(customer.getId().getValue());
        command.setAmount(BigDecimal.valueOf(5000000));
        command.setTargetType(RedeemTargetType.VOUCHER);
        command.setDescription("Đổi voucher test");

        LoyaltyPoint loyaltyPoint = LoyaltyPoint.createNew(customer.getId());
        loyaltyPoint.addPoints(Point.from(BigDecimal.valueOf(5000000)));

        when(customerRepository.findById(customer.getId().getValue())).thenReturn(Optional.of(customer));
        when(loyaltyPointRepository.findByCustomerId(customer.getId().getValue())).thenReturn(Optional.of(loyaltyPoint));
        when(promotionServicePort.redeemVoucher(any())).thenReturn(true);

        LoyaltyPointDto result = loyaltyApplicationService.redeemPoint(command);
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(0), result.getPoints());
        verify(promotionServicePort, times(1)).redeemVoucher(any());
        verify(loyaltyTransactionRepository, times(1)).save(any(LoyaltyTransaction.class));
        verify(loyaltyPointRepository, times(1)).save(loyaltyPoint);

    }


}
