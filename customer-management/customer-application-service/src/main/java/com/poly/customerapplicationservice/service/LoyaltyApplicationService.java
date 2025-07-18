package com.poly.customerapplicationservice.service;

import com.poly.customerapplicationservice.command.*;
import com.poly.customerapplicationservice.dto.LoyaltyPointDto;
import com.poly.customerapplicationservice.dto.LoyaltyTransactionDto;
import com.poly.customerapplicationservice.exception.VoucherRedeemFailedException;
import com.poly.customerapplicationservice.port.input.LoyaltyUsecase;
import com.poly.customerapplicationservice.port.output.PromotionServicePort;
import com.poly.customerdomain.model.entity.Customer;
import com.poly.customerdomain.model.entity.LoyaltyPoint;
import com.poly.customerdomain.model.entity.LoyaltyTransaction;
import com.poly.customerdomain.model.entity.valueobject.*;
import com.poly.customerdomain.model.exception.*;
import com.poly.customerdomain.output.CustomerRepository;
import com.poly.customerdomain.output.LoyaltyPointRepository;
import com.poly.customerdomain.output.LoyaltyTransactionRepository;

import java.time.LocalDateTime;
import java.util.List;

public class LoyaltyApplicationService implements LoyaltyUsecase{

    private final CustomerRepository customerRepository;

    private final LoyaltyPointRepository loyaltyPointRepository;

    private final LoyaltyTransactionRepository loyaltyTransactionRepository;

    private final PromotionServicePort  promotionServicePort;

    public LoyaltyApplicationService(CustomerRepository customerRepository, LoyaltyPointRepository loyaltyPointRepository, LoyaltyTransactionRepository loyaltyTransactionRepository, PromotionServicePort promotionServicePort) {
        this.loyaltyPointRepository = loyaltyPointRepository;
        this.customerRepository = customerRepository;
        this.loyaltyTransactionRepository = loyaltyTransactionRepository;
        this.promotionServicePort = promotionServicePort;
    }

    @Override
    public LoyaltyPointDto retrieveLoyaltyProfile(RetrieveLoyaltyProfileCommand command) {
        if (command.getCustomerId() == null) throw new BlankUserIdException();
        Customer customer = customerRepository.findById(command.getCustomerId()).orElseThrow(() -> new CustomerNotFoundException(command.getCustomerId()));
        LoyaltyPoint loyaltyPoint = loyaltyPointRepository.findByCustomerId(command.getCustomerId()).orElseThrow(() -> new LoyaltyNotFoundException(command.getCustomerId()));
        return LoyaltyPointDto.from(loyaltyPoint);
    }

    @Override
    public List<LoyaltyTransactionDto> viewTransactionHistory(RetrieveLoyaltyTransactionCommand command) {
        return List.of();
    }

    @Override
    public LoyaltyPointDto earnPoint(EarnPointLoyaltyCommand command) {
        LoyaltyPoint loyaltyPoint = loyaltyPointRepository.findByCustomerId(command.getCustomerId())
                .orElseThrow(() -> new LoyaltyNotFoundException(command.getCustomerId()));

        LoyaltyTransaction loyaltyTransaction = LoyaltyTransaction.createNew(
                loyaltyPoint.getId(),
                PointChanged.from(command.getAmount()),
                TransactionType.EARN,
                LocalDateTime.now(),
                Description.from(command.getDescription()));
        loyaltyTransactionRepository.save(loyaltyTransaction);

        if (command.getAmount().intValue() <= 0) throw new InvalidPointAmountException();

        loyaltyPoint.addPoints(Point.from(command.getAmount()));
        loyaltyPointRepository.save(loyaltyPoint);

        return LoyaltyPointDto.from(loyaltyPoint);
    }

    @Override
    public LoyaltyPointDto redeemPoint(RedeemPointLoyaltyCommand command) {
        LoyaltyPoint loyaltyPoint = loyaltyPointRepository.findByCustomerId(command.getCustomerId())
                .orElseThrow(() -> new LoyaltyNotFoundException(command.getCustomerId()));

        Customer customer = customerRepository.findById(command.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(command.getCustomerId()));

        Point amount = Point.from(command.getAmount());

        if (loyaltyPoint.getPoints().isLessThan(amount)) {
            throw new NotEnoughPointException();
        }

        switch (command.getTargetType()) {
            case VOUCHER -> {
                handleVoucherRedeem(command);
                redeemAndRecord(loyaltyPoint, amount, command.getDescription());
            }
            case ROOM -> {
                handleRoomRedeem(command);
                redeemAndRecord(loyaltyPoint, amount, command.getDescription());
            }
            case SERVICE -> {
                handleServiceRedeem(command);
                redeemAndRecord(loyaltyPoint, amount, command.getDescription());
            }
            case LEVEL_UP -> handleLevelUpRedeem(loyaltyPoint, customer);
            default -> throw new UnsupportedRedeemTypeException();
        }
        return LoyaltyPointDto.from(loyaltyPoint);
    }

    private void redeemAndRecord(LoyaltyPoint loyaltyPoint, Point amount, String description) {
        loyaltyPoint.subtractPoints(amount);
        loyaltyPointRepository.save(loyaltyPoint);
        recordRedeemTransaction(loyaltyPoint, amount, description);
    }

    private void recordRedeemTransaction(LoyaltyPoint loyaltyPoint, Point amount, String description) {
        LoyaltyTransaction transaction = LoyaltyTransaction.createNew(
                loyaltyPoint.getId(),
                PointChanged.from(amount.getValue()),
                TransactionType.REDEEM,
                LocalDateTime.now(),
                Description.from(description)
        );
        loyaltyTransactionRepository.save(transaction);
    }


    private void handleLevelUpRedeem(LoyaltyPoint loyaltyPoint, Customer customer) {
        Level currentLevel = customer.getLevel();
        if (currentLevel.isMaxLevel()) {
            throw new CannotUpgradeLevelException();
        }

        Level nextLevel = currentLevel.next();
        Point requiredPoint = Point.from(nextLevel.getSpendingRequired());

        if (loyaltyPoint.getPoints().isLessThan(requiredPoint)) {
            throw new NotEnoughPointToUpgradeException();
        }

        loyaltyPoint.subtractPoints(requiredPoint);
        loyaltyPointRepository.save(loyaltyPoint);

        customer.upgradeTo(nextLevel);
        customerRepository.save(customer);

        recordRedeemTransaction(loyaltyPoint, requiredPoint, "Nâng hạng thành viên lên " + nextLevel.name());
    }



    private void handleVoucherRedeem(RedeemPointLoyaltyCommand command) {
        RedeemVoucherCommand voucherCommand = new RedeemVoucherCommand();
        voucherCommand.setCustomerId(command.getCustomerId());
        voucherCommand.setVoucherId(command.getTargetId());

        try {
            boolean success = promotionServicePort.redeemVoucher(voucherCommand);
            if (!success) {
                // Trường hợp phía promotion-service trả về false
                throw new VoucherRedeemFailedException();
            }
        } catch (Exception ex) {
            // Trường hợp gọi Feign thất bại (timeout, 404, 5xx...)
            throw new VoucherRedeemFailedException("Lỗi khi gọi Promotion Service: " + ex.getMessage(), ex);
        }
    }

    private void handleRoomRedeem(RedeemPointLoyaltyCommand command) {
//        bookingClient.reserveRoomByPoint(command.getCustomerId(), command.getTargetId());
    }
    private void handleServiceRedeem(RedeemPointLoyaltyCommand command) {
//        bookingClient.addServiceByPoint(command.getCustomerId(), command.getTargetId());
    }

}
