package com.poly.promotion.application.controller;

import com.poly.promotion.application.dto.response.hateoas.VoucherPackHateoasResponse;
import com.poly.promotion.application.service.HateoasLinkBuilderService;
import com.poly.promotion.domain.application.api.external.VoucherPackExternalApi;
import com.poly.promotion.domain.application.dto.response.external.VoucherPackExternalResponse;
import com.poly.promotion.domain.application.api.external.VoucherExternalApi;
import com.poly.promotion.domain.application.dto.request.internal.voucher.VoucherRedeemRequest;
import com.poly.promotion.domain.application.dto.response.external.VoucherExternalResponse;
import com.poly.promotion.application.dto.response.hateoas.VoucherHateoasResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.poly.promotion.application.annotation.LogBusinessOperation;
import com.poly.promotion.application.annotation.LogMethodEntry;
import com.poly.promotion.application.annotation.LogMethodError;
import com.poly.promotion.application.annotation.LogMethodExit;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <h2>VoucherPackExternalController Class</h2>
 * 
 * <p>External REST controller for customer-facing operations on voucher packs.
 * This controller provides endpoints for external customer access to voucher pack
 * information, with filtered data appropriate for public consumption.</p>
 * 
 * <p><strong>Purpose:</strong></p>
 * <ul>
 *   <li><strong>Customer Access:</strong> Public endpoints for voucher pack browsing</li>
 *   <li><strong>Data Filtering:</strong> Only PUBLISHED status packs are returned</li>
 *   <li><strong>Security:</strong> Internal business logic and sensitive data is hidden</li>
 *   <li><strong>HATEOAS:</strong> Rich hypermedia responses with navigation links</li>
 * </ul>
 * 
 * <p><strong>Endpoint Categories:</strong></p>
 * <ul>
 *   <li><strong>GET /api/v1/external/promotion-management/voucher-packs:</strong> Retrieve available voucher packs for customers</li>
 *   <li><strong>GET /api/v1/external/promotion-management/voucher-packs/{id}:</strong> Retrieve specific voucher pack details for customers</li>
 *   <li><strong>POST /api/v1/external/promotion-management/voucher-packs/redeem:</strong> Redeem vouchers from voucher packs</li>
 *   <li><strong>POST /api/v1/external/promotion-management/voucher-packs/vouchers/{voucherCode}/apply:</strong> Apply vouchers to transactions</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherPackExternalApi
 */
@RestController
@RequestMapping("/api/v1/external/promotion-management/voucher-packs")
@RequiredArgsConstructor
@Validated
@Tag(name = "Voucher Pack External Access", description = "External APIs for customer access to voucher pack information")
public class VoucherPackExternalController {

    private final VoucherPackExternalApi voucherPackExternalApi;
    private final VoucherExternalApi voucherExternalApi;
    private final HateoasLinkBuilderService hateoasLinkBuilderService;

    /**
     * Retrieves all available voucher packs for customer browsing.
     * 
     * <p>This endpoint provides customers with information about all voucher packs
     * that are currently available for redemption. It filters out internal
     * administrative information and provides only customer-relevant data.</p>
     * 
     * <p><strong>Response Data:</strong></p>
     * <ul>
     *   <li>Voucher pack descriptions and benefits</li>
     *   <li>Discount amounts and validity periods</li>
     *   <li>Required loyalty points and availability</li>
     *   <li>Pack validity dates and current status</li>
     * </ul>
     * 
     * <p><strong>Business Rules:</strong></p>
     * <ul>
     *   <li>Only PUBLISHED status packs are returned</li>
     *   <li>Packs must be within their validity period</li>
     *   <li>Quantity must be greater than zero</li>
     *   <li>Customer-specific filtering may be applied</li>
     * </ul>
     * 
     * @return ResponseEntity containing a list of available voucher packs
     */
    @GetMapping
    @Operation(
        summary = "Get all available voucher packs (External)",
        description = "Retrieves all voucher packs that are currently available for customer redemption"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved voucher packs",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoucherPackExternalResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No voucher packs available"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @LogBusinessOperation(
        value = "Retrieve available voucher packs",
        category = "VOUCHER_PACK_READ_EXTERNAL"
    )
    @LogMethodEntry
    @LogMethodExit
    @LogMethodError
    public ResponseEntity<CollectionModel<VoucherPackHateoasResponse>> getAvailableVoucherPacks() {
        List<VoucherPackExternalResponse> voucherPacks = voucherPackExternalApi.getAllAvailableVoucherPacks();
        
        if (voucherPacks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        // Convert to HATEOAS responses
        List<VoucherPackHateoasResponse> hateoasResponses = voucherPacks.stream()
                .map(voucherPack -> {
                    VoucherPackHateoasResponse response = new VoucherPackHateoasResponse(voucherPack);
                    // Use external links for customer-facing endpoints
                    response.add(hateoasLinkBuilderService.buildVoucherPackLinks(1L)); // Use default ID for links
                    return response;
                })
                .collect(Collectors.toList());
        
        // Create collection model with links
        CollectionModel<VoucherPackHateoasResponse> collectionModel = CollectionModel.of(hateoasResponses);
        collectionModel.add(hateoasLinkBuilderService.buildCollectionLinks());
        
        return ResponseEntity.ok(collectionModel);
    }

    /**
     * Retrieves a specific voucher pack by ID for customer viewing.
     * 
     * <p>This endpoint provides customers with detailed information about
     * a specific voucher pack, including benefits, requirements, and validity
     * information. Only PUBLISHED packs are accessible.</p>
     * 
     * <p><strong>Response Data:</strong></p>
     * <ul>
     *   <li>Voucher pack description and benefits</li>
     *   <li>Discount amount and validity period</li>
     *   <li>Required loyalty points</li>
     *   <li>Validity dates and availability</li>
     * </ul>
     * 
     * @param voucherPackId the unique identifier of the voucher pack to retrieve
     * @return ResponseEntity containing the voucher pack details
     */
    @GetMapping("/{voucherPackId}")
    @Operation(
        summary = "Get voucher pack by ID (External)",
        description = "Retrieves a specific voucher pack by ID for customer viewing (only PUBLISHED packs)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved voucher pack",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoucherPackExternalResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Voucher pack not found or not available"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @LogBusinessOperation(
        value = "Retrieve voucher pack by ID",
        category = "VOUCHER_PACK_READ_EXTERNAL"
    )
    @LogMethodEntry
    @LogMethodExit
    @LogMethodError
    public ResponseEntity<VoucherPackExternalResponse> getVoucherPackById(
            @PathVariable Long voucherPackId) {
        // TODO: Implement getVoucherPackById in VoucherPackExternalApi
        return ResponseEntity.status(501).build(); // Not Implemented
    }

    /**
     * Redeems vouchers from a voucher pack for a customer.
     * 
     * <p>This endpoint allows customers to redeem vouchers from available
     * voucher packs using their loyalty points. The system validates
     * eligibility and creates individual vouchers for the customer.</p>
     * 
     * <p><strong>Business Rules:</strong></p>
     * <ul>
     *   <li>Voucher pack must be PUBLISHED and active</li>
     *   <li>Customer must have sufficient loyalty points</li>
     *   <li>Pack must have sufficient quantity available</li>
     *   <li>Pack must not be expired</li>
     * </ul>
     * 
     * @param request the voucher redemption request containing pack ID and quantity
     * @param customerId the customer's unique identifier
     * @return ResponseEntity containing the redeemed voucher information
     */
    @PostMapping("/redeem")
    @Operation(
        summary = "Redeem vouchers from pack (External)",
        description = "Allows customers to redeem vouchers from available voucher packs"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully redeemed vouchers",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoucherHateoasResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request or insufficient points/quantity"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Voucher pack not found or not available"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @LogBusinessOperation(
        value = "Redeem vouchers from pack",
        category = "VOUCHER_REDEMPTION"
    )
    @LogMethodEntry
    @LogMethodExit
    @LogMethodError
    public ResponseEntity<VoucherHateoasResponse> redeemVouchers(
            @Valid @RequestBody VoucherRedeemRequest request,
            @RequestParam String customerId) {
        
        // Set customer ID in request
        request.setCustomerId(customerId);
        
        VoucherExternalResponse redeemedVoucher = voucherExternalApi.redeemVoucherFromPack(request);
        
        // Convert to HATEOAS response
        VoucherHateoasResponse hateoasResponse = new VoucherHateoasResponse(redeemedVoucher);
        hateoasResponse.add(hateoasLinkBuilderService.buildVoucherLinks(redeemedVoucher.getVoucherCode()));
        
        return ResponseEntity.ok(hateoasResponse);
    }

    /**
     * Applies a voucher to a transaction for a customer.
     * 
     * <p>This endpoint allows customers to use their redeemed vouchers during
     * transactions. The system validates voucher eligibility and marks it as used
     * after successful application.</p>
     * 
     * <p><strong>Business Rules:</strong></p>
     * <ul>
     *   <li>Voucher must be in REDEEMED status</li>
     *   <li>Voucher must not be expired</li>
     *   <li>Voucher must belong to the customer</li>
     *   <li>Voucher can only be used once</li>
     * </ul>
     * 
     * @param voucherCode the unique voucher code to apply
     * @param customerId the customer's unique identifier
     * @return ResponseEntity containing the used voucher information
     */
    @PostMapping("/vouchers/{voucherCode}/apply")
    @Operation(
        summary = "Apply voucher to transaction (External)",
        description = "Allows customers to use their redeemed vouchers during transactions"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully applied voucher",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoucherHateoasResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Voucher cannot be used (expired, already used, etc.)"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Voucher not found"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @LogBusinessOperation(
        value = "Apply voucher to transaction",
        category = "VOUCHER_USAGE"
    )
    @LogMethodEntry
    @LogMethodExit
    @LogMethodError
    public ResponseEntity<VoucherHateoasResponse> applyVoucher(
            @PathVariable String voucherCode,
            @RequestParam String customerId) {
        
        VoucherExternalResponse usedVoucher = voucherExternalApi.applyVoucher(voucherCode, customerId);
        
        // Convert to HATEOAS response
        VoucherHateoasResponse hateoasResponse = new VoucherHateoasResponse(usedVoucher);
        hateoasResponse.add(hateoasLinkBuilderService.buildVoucherLinks(voucherCode));
        
        return ResponseEntity.ok(hateoasResponse);
    }
}
