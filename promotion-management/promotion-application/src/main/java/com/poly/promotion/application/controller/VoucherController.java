package com.poly.promotion.application.controller;

import com.poly.promotion.application.dto.response.hateoas.VoucherHateoasResponse;
import com.poly.promotion.application.service.HateoasLinkBuilderService;
import com.poly.promotion.domain.application.api.external.VoucherExternalApi;
import com.poly.promotion.domain.application.dto.request.internal.voucher.VoucherRedeemRequest;
import com.poly.promotion.domain.application.dto.response.external.VoucherExternalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <h2>VoucherController Class</h2>
 * 
 * <p>REST controller for managing individual vouchers in the promotion system.
 * This controller provides endpoints for customer voucher operations including
 * redemption, viewing, and management of personal vouchers.</p>
 * 
 * <p><strong>Purpose:</strong></p>
 * <ul>
 *   <li><strong>Customer Voucher Management:</strong> Endpoints for customers to manage their vouchers</li>
 *   <li><strong>Voucher Redemption:</strong> Process for redeeming vouchers from voucher packs</li>
 *   <li><strong>Voucher Viewing:</strong> Customer access to their voucher portfolio</li>
 *   <li><strong>API Documentation:</strong> OpenAPI/Swagger documentation for all endpoints</li>
 * </ul>
 * 
 * <p><strong>Endpoint Categories:</strong></p>
 * <ul>
 *   <li><strong>GET /api/v1/vouchers/customer/{customerId}:</strong> Retrieve customer's vouchers</li>
 *   <li><strong>POST /api/v1/vouchers/redeem:</strong> Redeem voucher from voucher pack</li>
 * </ul>
 * 
 * <p><strong>Security Considerations:</strong></p>
 * <ul>
 *   <li>Customer authentication required for all endpoints</li>
 *   <li>Customers can only access their own vouchers</li>
 *   <li>Loyalty point validation during redemption</li>
 *   <li>Business rule enforcement for all operations</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherExternalApi
 */
@RestController
@RequestMapping("/api/v1/vouchers")
@RequiredArgsConstructor
@Validated
@Tag(name = "Voucher Management", description = "APIs for managing individual vouchers in the promotion system")
public class VoucherController {

    private final VoucherExternalApi voucherExternalApi;
    private final HateoasLinkBuilderService hateoasLinkBuilderService;

    /**
     * Retrieves all vouchers belonging to a specific customer.
     * 
     * <p>This endpoint provides customers with access to their complete
     * voucher portfolio, including active, used, and expired vouchers.
     * The response is filtered to show only customer-relevant information.</p>
     * 
     * <p><strong>Response Data:</strong></p>
     * <ul>
     *   <li>Voucher codes for usage during transactions</li>
     *   <li>Discount amounts and validity periods</li>
     *   <li>Redemption dates and expiration timestamps</li>
     *   <li>Current voucher status and usability</li>
     * </ul>
     * 
     * <p><strong>Business Rules:</strong></p>
     * <ul>
     *   <li>Only customer's own vouchers are returned</li>
     *   <li>All voucher statuses are included for transparency</li>
     *   <li>Expired vouchers are clearly marked</li>
     *   <li>Used vouchers show transaction history</li>
     * </ul>
     * 
     * <p><strong>Security:</strong></p>
     * <ul>
     *   <li>Customer authentication is required</li>
     *   <li>Access is restricted to customer's own data</li>
     *   <li>No internal system information is exposed</li>
     *   <li>Customer privacy is maintained</li>
     * </ul>
     * 
     * @param customerId the unique identifier of the customer
     * @return ResponseEntity containing a list of customer's vouchers
     */
    @GetMapping("/customer/{customerId}")
    @Operation(
        summary = "Get customer's vouchers",
        description = "Retrieves all vouchers belonging to the specified customer"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved customer vouchers",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoucherExternalResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No vouchers found for customer"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid customer ID format"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Customer not authenticated"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied to customer data"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @LogBusinessOperation(
        value = "Retrieve customer vouchers",
        category = "VOUCHER_READ"
    )
    @LogMethodEntry
    @LogMethodExit
    @LogMethodError
    public ResponseEntity<CollectionModel<VoucherHateoasResponse>> getCustomerVouchers(
            @Parameter(description = "Unique identifier of the customer")
            @PathVariable @NotBlank String customerId) {
        List<VoucherExternalResponse> customerVouchers = voucherExternalApi.getCustomerVouchers(customerId);
        
        if (customerVouchers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        // Convert to HATEOAS responses
        List<VoucherHateoasResponse> hateoasResponses = customerVouchers.stream()
                .map(voucher -> {
                    VoucherHateoasResponse response = new VoucherHateoasResponse(voucher);
                    response.add(hateoasLinkBuilderService.buildVoucherLinks("default"));
                    return response;
                })
                .collect(Collectors.toList());
        
        // Create collection model with links
        CollectionModel<VoucherHateoasResponse> collectionModel = CollectionModel.of(hateoasResponses);
        collectionModel.add(hateoasLinkBuilderService.buildCollectionLinks());
        
        return ResponseEntity.ok(collectionModel);
    }

    /**
     * Allows a customer to redeem a voucher from a voucher pack using their loyalty points.
     * 
     * <p>This endpoint processes the redemption of vouchers from available voucher packs.
     * The customer must have sufficient loyalty points and the voucher pack must be
     * available for redemption. Upon successful redemption, loyalty points are deducted
     * and a new voucher is created for the customer.</p>
     * 
     * <p><strong>Redemption Process:</strong></p>
     * <ol>
     *   <li>Validate customer authentication and authorization</li>
     *   <li>Check voucher pack availability and status</li>
     *   <li>Verify customer has sufficient loyalty points</li>
     *   <li>Deduct loyalty points from customer account</li>
     *   <li>Create new voucher with unique code</li>
     *   <li>Update voucher pack quantity</li>
     *   <li>Publish domain events for tracking</li>
     * </ol>
     * 
     * <p><strong>Business Rules:</strong></p>
     * <ul>
     *   <li>Voucher pack must be in PUBLISHED status</li>
     *   <li>Voucher pack must have available quantity</li>
     *   <li>Customer must have sufficient loyalty points</li>
     *   <li>Voucher pack must be within validity period</li>
     *   <li>Customer cannot redeem more than available quantity</li>
     * </ul>
     * 
     * <p><strong>Response Data:</strong></p>
     * <ul>
     *   <li>Newly created voucher with unique code</li>
     *   <li>Discount amount and validity period</li>
     *   <li>Redemption timestamp and expiration date</li>
     *   <li>Current voucher status (REDEEMED)</li>
     * </ul>
     * 
     * @param request the voucher redemption request containing customer and voucher pack information
     * @return ResponseEntity containing the redeemed voucher details
     */
    @PostMapping("/redeem")
    @Operation(
        summary = "Redeem voucher from voucher pack",
        description = "Allows customers to redeem vouchers from available voucher packs using loyalty points"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Voucher redeemed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoucherExternalResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data or business rule violation"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Customer not authenticated"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Customer not authorized or insufficient loyalty points"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Voucher pack not found or unavailable"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Voucher pack cannot be redeemed (wrong status, expired, or out of stock)"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @LogBusinessOperation(
        value = "Redeem voucher from pack",
        category = "VOUCHER_REDEMPTION"
    )
    @LogMethodEntry
    @LogMethodExit
    @LogMethodError
    public ResponseEntity<VoucherExternalResponse> redeemVoucherFromPack(
            @Valid @RequestBody VoucherRedeemRequest request) {
        VoucherExternalResponse redeemedVoucher = voucherExternalApi.redeemVoucherFromPack(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(redeemedVoucher);
    }
}
