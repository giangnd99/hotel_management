package com.poly.customerapplicationservice.dto;

import com.poly.customerdomain.model.entity.LoyaltyTransaction;
import com.poly.customerdomain.model.entity.valueobject.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class LoyaltyTransactionDto {
    private UUID transactionId;
    private BigDecimal amountChange;
    private LocalDateTime transactionDate;
    private TransactionType transactionType;
    private String description;

    public static LoyaltyTransactionDto from(LoyaltyTransaction loyaltyTransaction){
        LoyaltyTransactionDto loyaltyTransactionDto = new LoyaltyTransactionDto();
        loyaltyTransactionDto.setTransactionId(loyaltyTransaction.getId().getValue());
        loyaltyTransactionDto.setAmountChange(loyaltyTransaction.getPointChanged().getValueChange());
        loyaltyTransactionDto.setTransactionDate(loyaltyTransaction.getTransactionDate());
        loyaltyTransactionDto.setTransactionType(loyaltyTransaction.getTransactionType());
        loyaltyTransactionDto.setDescription(loyaltyTransaction.getDescription().getValue());
        return loyaltyTransactionDto;
    }

    public static List<LoyaltyTransactionDto> from(List<LoyaltyTransaction> loyaltyTransactions){
        List<LoyaltyTransactionDto> dtoList = new ArrayList<>();
        for (LoyaltyTransaction transaction : loyaltyTransactions) {
            dtoList.add(from(transaction));
        }
        return dtoList;
    }
}
