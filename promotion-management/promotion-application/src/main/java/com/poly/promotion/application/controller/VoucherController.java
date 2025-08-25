package com.poly.promotion.application.controller;

import com.poly.promotion.domain.application.dto.response.external.VoucherExternalResponse;
import com.poly.promotion.application.service.HateoasLinkBuilderService;
import com.poly.promotion.domain.application.api.external.VoucherExternalApi;
import com.poly.promotion.domain.application.dto.request.internal.voucher.VoucherRedeemRequest;
import com.poly.promotion.domain.core.entity.Voucher;
import com.poly.promotion.domain.core.exception.VoucherDomainException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <h2>Voucher Controller</h2>
 * 
 * <p>REST controller for external voucher operations. This controller provides
 * customer-facing endpoints for voucher redemption and usage.</p>
 * 
 * <p><strong>External API Endpoints:</strong></p>
 * <ul>
 *   <li>Voucher redemption from available packs</li>
 *   <li>Voucher application for discounts</li>
 *   <li>Customer voucher portfolio management</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherExternalApi
 * @see VoucherExternalResponse
 */
@RestController
@RequestMapping("/api/v1/external/promotion-management/vouchers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Voucher Management", description = "External API for customer voucher operations")
public class VoucherController {

    private final VoucherExternalApi voucherExternalApi;
    private final HateoasLinkBuilderService hateoasLinkBuilderService;

    /**
     * Redeem vouchers from a specific voucher pack.
     * 
     * @param packId the voucher pack ID to redeem from
     * @param request the redemption request containing quantity and customer ID
     * @return the redeemed vouchers with HATEOAS links
     */
    @PostMapping("/redeem/{packId}")
    @Operation(
        summary = "Redeem vouchers from pack",
        description = "Allows customers to redeem vouchers from an available voucher pack"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Vouchers redeemed successfully",
            content = @Content(schema = @Schema(implementation = VoucherExternalResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid redemption request or insufficient quantity"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Voucher pack not found or not available"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Voucher pack is not in PUBLISHED status or has expired"
        )
    })
    public ResponseEntity<EntityModel<VoucherExternalResponse>> redeemVouchers(
            @Parameter(description = "Voucher pack ID", required = true)
            @PathVariable Long packId,
            @Parameter(description = "Redemption request", required = true)
            @Valid @RequestBody VoucherRedeemRequest request) {
        
        log.info("Redeeming vouchers from pack {} for customer {} with quantity {}", 
                packId, request.getCustomerId(), request.getQuantity());
        
        try {
            // Set the voucher pack ID from the path parameter
            request.setVoucherPackId(packId);
            
            VoucherExternalResponse redeemedVoucher = voucherExternalApi.redeemVoucherFromPack(request);
            
            EntityModel<VoucherExternalResponse> entityModel = EntityModel.of(redeemedVoucher, 
                    hateoasLinkBuilderService.buildVoucherLinks(redeemedVoucher.getVoucherCode()));
            entityModel.add(hateoasLinkBuilderService.buildCollectionLinks());
            
            log.info("Successfully redeemed voucher from pack {} for customer {}", 
                    packId, request.getCustomerId());
            
            return ResponseEntity.ok(entityModel);
            
        } catch (VoucherDomainException e) {
            log.warn("Failed to redeem voucher from pack {} for customer {}: {}", 
                    packId, request.getCustomerId(), e.getMessage());
            throw e;
        }
    }






}
