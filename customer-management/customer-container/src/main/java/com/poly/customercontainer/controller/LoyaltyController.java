package com.poly.customercontainer.controller;

import com.poly.customerapplicationservice.command.EarnPointLoyaltyCommand;
import com.poly.customerapplicationservice.command.RedeemPointLoyaltyCommand;
import com.poly.customerapplicationservice.command.RetrieveLoyaltyProfileCommand;
import com.poly.customerapplicationservice.command.RetrieveLoyaltyTransactionCommand;
import com.poly.customerapplicationservice.dto.LoyaltyTransactionDto;
import com.poly.customerapplicationservice.port.input.LoyaltyUsecase;
import com.poly.customercontainer.shared.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.poly.customerapplicationservice.dto.LoyaltyPointDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/loyalty")
public class LoyaltyController {

    private final LoyaltyUsecase loyaltyUsecase;

    public LoyaltyController(LoyaltyUsecase loyaltyUsecase) {
        this.loyaltyUsecase = loyaltyUsecase;
    }

    @GetMapping("/profile/{customerId}")
    public ResponseEntity<ApiResponse<LoyaltyPointDto>> retrieveLoyaltyPoint(@PathVariable UUID customerId) {
        RetrieveLoyaltyProfileCommand command = new RetrieveLoyaltyProfileCommand();
        command.setCustomerId(customerId);
        return ResponseEntity.ok(ApiResponse.success(loyaltyUsecase.retrieveLoyaltyProfile(command)));
    }

    @GetMapping("/profile/transaction/{customerId}")
    public ResponseEntity<ApiResponse<List<LoyaltyTransactionDto>>> retrieveLoyaltyTransaction(@PathVariable UUID customerId) {
        RetrieveLoyaltyTransactionCommand command = new RetrieveLoyaltyTransactionCommand();
        command.setCustomerId(customerId);
        return ResponseEntity.ok(ApiResponse.success(loyaltyUsecase.viewTransactionHistory(command)));
    }

    @PostMapping("/earnpoint")
    public ResponseEntity<ApiResponse<LoyaltyPointDto>> earnPoint(@RequestBody EarnPointLoyaltyCommand command) {
        return  ResponseEntity.ok(ApiResponse.success(loyaltyUsecase.earnPoint(command)));
    }

    @PostMapping("/redeempoint")
    public ResponseEntity<ApiResponse<LoyaltyPointDto>> redeemPoint(@RequestBody RedeemPointLoyaltyCommand command) {
        return ResponseEntity.ok(ApiResponse.success(loyaltyUsecase.redeemPoint(command)));
    }
}
