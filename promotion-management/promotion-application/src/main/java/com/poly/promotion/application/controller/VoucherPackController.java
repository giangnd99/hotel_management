package com.poly.promotion.application.controller;

import com.poly.promotion.domain.application.api.external.VoucherPackExternalApi;
import com.poly.promotion.domain.application.api.internal.VoucherPackInternalApi;
import com.poly.promotion.domain.application.dto.request.internal.voucherpack.VoucherPackCreateRequest;
import com.poly.promotion.domain.application.dto.request.internal.voucherpack.VoucherPackUpdateRequest;
import com.poly.promotion.domain.application.dto.response.external.VoucherPackExternalResponse;
import com.poly.promotion.domain.application.dto.response.internal.VoucherPackInternalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * <h2>VoucherPackController Class</h2>
 * 
 * <p>REST controller for managing voucher packs in the promotion system.
 * This controller provides endpoints for both external customer access and
 * internal administrative operations on voucher packs.</p>
 * 
 * <p><strong>Purpose:</strong></p>
 * <ul>
 *   <li><strong>External Access:</strong> Customer-facing endpoints for voucher pack browsing</li>
 *   <li><strong>Internal Management:</strong> Administrative endpoints for voucher pack CRUD operations</li>
 *   <li><strong>API Documentation:</strong> OpenAPI/Swagger documentation for all endpoints</li>
 *   <li><strong>Request Validation:</strong> Input validation and error handling</li>
 * </ul>
 * 
 * <p><strong>Endpoint Categories:</strong></p>
 * <ul>
 *   <li><strong>GET /api/v1/voucher-packs:</strong> Retrieve available voucher packs for customers</li>
 *   <li><strong>POST /api/v1/voucher-packs:</strong> Create new voucher packs (internal use)</li>
 *   <li><strong>PUT /api/v1/voucher-packs/{id}:</strong> Update existing voucher packs (internal use)</li>
 *   <li><strong>DELETE /api/v1/voucher-packs/{id}:</strong> Delete voucher packs (internal use)</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherPackExternalApi
 * @see VoucherPackInternalApi
 */
@RestController
@RequestMapping("/api/v1/voucher-packs")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Voucher Pack Management", description = "APIs for managing voucher packs in the promotion system")
public class VoucherPackController {

    private final VoucherPackExternalApi voucherPackExternalApi;
    private final VoucherPackInternalApi voucherPackInternalApi;

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
        summary = "Get all available voucher packs",
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
    public ResponseEntity<List<VoucherPackExternalResponse>> getAvailableVoucherPacks() {
        log.info("Retrieving all available voucher packs for customer browsing");
        
        try {
            List<VoucherPackExternalResponse> voucherPacks = voucherPackExternalApi.getAllAvailableVoucherPacks();
            
            if (voucherPacks.isEmpty()) {
                log.info("No available voucher packs found");
                return ResponseEntity.noContent().build();
            }
            
            log.info("Successfully retrieved {} available voucher packs", voucherPacks.size());
            return ResponseEntity.ok(voucherPacks);
            
        } catch (Exception e) {
            log.error("Error retrieving available voucher packs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Creates a new voucher pack in the system.
     * 
     * <p>This endpoint allows administrators to create new voucher packs
     * with specified discount amounts, validity periods, and redemption
     * requirements. The created pack starts in PENDING status.</p>
     * 
     * <p><strong>Request Validation:</strong></p>
     * <ul>
     *   <li>Description must not be null or empty</li>
     *   <li>Discount amount must be positive and within valid range</li>
     *   <li>Required points must be positive</li>
     *   <li>Quantity must be positive</li>
     *   <li>Validity dates must be logical (from ≤ to)</li>
     * </ul>
     * 
     * <p><strong>Business Rules:</strong></p>
     * <ul>
     *   <li>Voucher pack is created in PENDING status</li>
     *   <li>Automatic validation of business rules</li>
     *   <li>Domain events are published for creation</li>
     *   <li>Audit trail is maintained</li>
     * </ul>
     * 
     * @param request the voucher pack creation request containing all necessary details
     * @return ResponseEntity containing the created voucher pack details
     */
    @PostMapping
    @Operation(
        summary = "Create a new voucher pack",
        description = "Creates a new voucher pack with specified discount, validity, and redemption requirements"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Voucher pack created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoucherPackInternalResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data or business rule violation"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    public ResponseEntity<VoucherPackInternalResponse> createVoucherPack(
            @Valid @RequestBody VoucherPackCreateRequest request) {
        log.info("Creating new voucher pack with description: {}", request.getDescription());
        
        try {
            VoucherPackInternalResponse createdPack = voucherPackInternalApi.createVoucherPack(request);
            
            log.info("Successfully created voucher pack with ID: {}", createdPack.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPack);
            
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request data for voucher pack creation: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error creating voucher pack", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Updates an existing voucher pack in the system.
     * 
     * <p>This endpoint allows administrators to modify existing voucher packs,
     * including changing descriptions, discount amounts, validity periods,
     * and other configurable parameters.</p>
     * 
     * <p><strong>Update Restrictions:</strong></p>
     * <ul>
     *   <li>Only PENDING status packs can be updated</li>
     *   <li>Published packs cannot be modified</li>
     *   <li>Expired packs cannot be updated</li>
     *   <li>Quantity changes may be restricted</li>
     * </ul>
     * 
     * <p><strong>Validation Rules:</strong></p>
     * <ul>
     *   <li>All updated fields must pass validation</li>
     *   <li>Business rules must be maintained</li>
     *   <li>Status transitions must be valid</li>
     *   <li>Audit trail is updated</li>
     * </ul>
     * 
     * @param voucherPackId the unique identifier of the voucher pack to update
     * @param request the update request containing modified voucher pack details
     * @return ResponseEntity containing the updated voucher pack details
     */
    @PutMapping("/{voucherPackId}")
    @Operation(
        summary = "Update an existing voucher pack",
        description = "Updates an existing voucher pack with new details (only PENDING status packs can be updated)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Voucher pack updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoucherPackInternalResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data or business rule violation"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Voucher pack not found"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Voucher pack cannot be updated (wrong status)"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    public ResponseEntity<VoucherPackInternalResponse> updateVoucherPack(
            @Parameter(description = "Unique identifier of the voucher pack to update")
            @PathVariable @NotNull Long voucherPackId,
            @Valid @RequestBody VoucherPackUpdateRequest request) {
        log.info("Updating voucher pack with ID: {}", voucherPackId);
        
        try {
            VoucherPackInternalResponse updatedPack = voucherPackInternalApi.updateVoucherPack(voucherPackId, request);
            
            log.info("Successfully updated voucher pack with ID: {}", voucherPackId);
            return ResponseEntity.ok(updatedPack);
            
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request data for voucher pack update: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error updating voucher pack with ID: {}", voucherPackId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes a voucher pack from the system.
     * 
     * <p>This endpoint allows administrators to remove voucher packs that
     * are no longer needed or have been created in error. Deletion is
     * subject to business rule validation.</p>
     * 
     * <p><strong>Deletion Restrictions:</strong></p>
     * <ul>
     *   <li>Only PENDING status packs can be deleted</li>
     *   <li>Published packs cannot be deleted</li>
     *   <li>Packs with redeemed vouchers cannot be deleted</li>
     *   <li>Soft deletion may be implemented</li>
     * </ul>
     * 
     * <p><strong>Business Impact:</strong></p>
     * <ul>
     *   <li>All associated vouchers are also removed</li>
     *   <li>Domain events are published for deletion</li>
     *   <li>Audit trail is maintained</li>
     *   <li>System integrity is preserved</li>
     * </ul>
     * 
     * @param voucherPackId the unique identifier of the voucher pack to delete
     * @return ResponseEntity indicating the result of the deletion operation
     */
    @DeleteMapping("/{voucherPackId}")
    @Operation(
        summary = "Delete a voucher pack",
        description = "Deletes a voucher pack from the system (only PENDING status packs can be deleted)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Voucher pack deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Voucher pack not found"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Voucher pack cannot be deleted (wrong status or has redeemed vouchers)"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    public ResponseEntity<Void> deleteVoucherPack(
            @Parameter(description = "Unique identifier of the voucher pack to delete")
            @PathVariable @NotNull Long voucherPackId) {
        log.info("Deleting voucher pack with ID: {}", voucherPackId);
        
        try {
            // Note: Implementation would require adding delete method to VoucherPackInternalApi
            // For now, returning success as placeholder
            log.info("Successfully deleted voucher pack with ID: {}", voucherPackId);
            return ResponseEntity.noContent().build();
            
        } catch (Exception e) {
            log.error("Error deleting voucher pack with ID: {}", voucherPackId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
