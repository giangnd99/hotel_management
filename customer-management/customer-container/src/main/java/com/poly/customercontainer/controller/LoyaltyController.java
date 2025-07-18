package com.poly.customercontainer.controller;

import com.poly.customerapplicationservice.command.RetrieveLoyaltyProfileCommand;
import com.poly.customerapplicationservice.port.input.LoyaltyUsecase;
import com.poly.customercontainer.shared.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.poly.customerapplicationservice.dto.LoyaltyPointDto;

import java.util.UUID;

@RestController
@RequestMapping("/loyalty")
public class LoyaltyController {

    private final LoyaltyUsecase loyaltyUsecase;

    public LoyaltyController(LoyaltyUsecase loyaltyUsecase) {
        this.loyaltyUsecase = loyaltyUsecase;
    }

    @GetMapping("/profile/{customerId}")
    public ResponseEntity<ApiResponse<LoyaltyPointDto>> retrieveLoyalty(@PathVariable UUID customerId) {
        RetrieveLoyaltyProfileCommand command = new RetrieveLoyaltyProfileCommand();
        command.setCustomerId(customerId);
        return ResponseEntity.ok(ApiResponse.success(loyaltyUsecase.retrieveLoyaltyProfile(command)));
    }
}
