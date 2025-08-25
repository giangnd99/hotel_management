package com.poly.promotion.application.controller;

import com.poly.promotion.domain.application.api.internal.VoucherPackInternalApi;
import com.poly.promotion.domain.application.dto.request.internal.voucherpack.VoucherPackCreateRequest;
import com.poly.promotion.domain.application.dto.request.internal.voucherpack.VoucherPackUpdateRequest;
import com.poly.promotion.domain.application.dto.response.internal.VoucherPackInternalResponse;
import com.poly.promotion.application.service.HateoasLinkBuilderService;
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
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <h2>VoucherPackInternalController Class</h2>
 * 
 * <p>Internal REST controller for administrative operations on voucher packs.
 * This controller provides endpoints for internal system management and
 * administrative operations on voucher packs.</p>
 * 
 * <p><strong>Purpose:</strong></p>
 * <ul>
 *   <li><strong>Administrative Access:</strong> Internal endpoints for voucher pack CRUD operations</li>
 *   <li><strong>System Management:</strong> Administrative operations and monitoring</li>
 *   <li><strong>Internal API:</strong> Controlled access for internal systems</li>
 *   <li><strong>Full Data Access:</strong> Access to all voucher pack data and statuses</li>
 * </ul>
 * 
 * <p><strong>Endpoint Categories:</strong></p>
 * <ul>
 *   <li><strong>GET /api/v1/internal/promotion-management/voucher-packs:</strong> Retrieve all voucher packs (all statuses)</li>
 *   <li><strong>GET /api/v1/internal/promotion-management/voucher-packs/{id}:</strong> Retrieve specific voucher pack by ID</li>
 *   <li><strong>POST /api/v1/internal/promotion-management/voucher-packs:</strong> Create new voucher packs</li>
 *   <li><strong>PUT /api/v1/internal/promotion-management/voucher-packs/{id}:</strong> Update existing voucher packs</li>
 *   <li><strong>DELETE /api/v1/internal/promotion-management/voucher-packs/{id}:</strong> Delete voucher packs</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherPackInternalApi
 */
@RestController
@RequestMapping("/api/v1/internal/promotion-management/voucher-packs")
@RequiredArgsConstructor
@Validated
@Tag(name = "Voucher Pack Internal Management", description = "Internal APIs for administrative management of voucher packs")
public class VoucherPackInternalController {

    private final VoucherPackInternalApi voucherPackInternalApi;
    private final HateoasLinkBuilderService hateoasLinkBuilderService;

    /**
     * Retrieves all voucher packs in the system for administrative purposes.
     * 
     * <p>This endpoint provides administrators with access to all voucher packs
     * regardless of status, including PENDING, PUBLISHED, EXPIRED, and CLOSED packs.</p>
     * 
     * <p><strong>Response Data:</strong></p>
     * <ul>
     *   <li>Complete voucher pack information</li>
     *   <li>All status types and administrative data</li>
     *   <li>Internal business logic and audit information</li>
     *   <li>Full administrative visibility</li>
     * </ul>
     * 
     * @return ResponseEntity containing a list of all voucher packs
     */
    @GetMapping
    @Operation(
        summary = "Get all voucher packs (Internal)",
        description = "Retrieves all voucher packs in the system for administrative purposes (all statuses)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved all voucher packs",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoucherPackInternalResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No voucher packs found"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @LogBusinessOperation(
        value = "Retrieve all voucher packs",
        category = "VOUCHER_PACK_READ_INTERNAL"
    )
    @LogMethodEntry
    @LogMethodExit
    @LogMethodError
    public ResponseEntity<CollectionModel<EntityModel<VoucherPackInternalResponse>>> getAllVoucherPacks() {
        List<VoucherPackInternalResponse> voucherPacks = voucherPackInternalApi.getAllVoucherPacks();
        
        if (voucherPacks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        // Convert to HATEOAS responses with internal links
        List<EntityModel<VoucherPackInternalResponse>> voucherPackModels = voucherPacks.stream()
                .map(voucherPack -> {
                    EntityModel<VoucherPackInternalResponse> model = EntityModel.of(voucherPack);
                    if (voucherPack.getId() != null) {
                        model.add(hateoasLinkBuilderService.buildVoucherPackInternalLinks(voucherPack.getId()));
                    }
                    return model;
                })
                .collect(Collectors.toList());
        
        // Create collection model with links
        CollectionModel<EntityModel<VoucherPackInternalResponse>> collectionModel = CollectionModel.of(voucherPackModels);
        collectionModel.add(hateoasLinkBuilderService.buildCollectionLinks());
        
        return ResponseEntity.ok(collectionModel);
    }

    /**
     * Retrieves a specific voucher pack by ID for administrative purposes.
     * 
     * <p>This endpoint provides administrators with detailed information about
     * a specific voucher pack, including all internal data and status information.</p>
     * 
     * @param voucherPackId the unique identifier of the voucher pack to retrieve
     * @return ResponseEntity containing the voucher pack details
     */
    @GetMapping("/{voucherPackId}")
    @Operation(
        summary = "Get voucher pack by ID (Internal)",
        description = "Retrieves a specific voucher pack by ID for administrative purposes"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved voucher pack",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoucherPackInternalResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Voucher pack not found"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @LogBusinessOperation(
        value = "Retrieve voucher pack by ID",
        category = "VOUCHER_PACK_READ_INTERNAL"
    )
    @LogMethodEntry
    @LogMethodExit
    @LogMethodError
    public ResponseEntity<EntityModel<VoucherPackInternalResponse>> getVoucherPackById(
            @Parameter(description = "Unique identifier of the voucher pack to retrieve")
            @PathVariable @NotNull Long voucherPackId) {
        VoucherPackInternalResponse voucherPack = voucherPackInternalApi.getVoucherPackById(voucherPackId);
        
        // Create HATEOAS response with internal links
        EntityModel<VoucherPackInternalResponse> model = EntityModel.of(voucherPack);
        model.add(hateoasLinkBuilderService.buildVoucherPackInternalLinks(voucherPackId));
        
        return ResponseEntity.ok(model);
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
        summary = "Create a new voucher pack (Internal)",
        description = "Creates a new voucher pack in the system (starts in PENDING status)"
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
    @LogBusinessOperation(
        value = "Create voucher pack",
        category = "VOUCHER_PACK_CREATE"
    )
    @LogMethodEntry
    @LogMethodExit
    @LogMethodError
    public ResponseEntity<VoucherPackInternalResponse> createVoucherPack(
            @Valid @RequestBody VoucherPackCreateRequest request) {
        VoucherPackInternalResponse createdPack = voucherPackInternalApi.createVoucherPack(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPack);
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
     * @param voucherPackId the unique identifier of the voucher pack to update
     * @param request the update request containing modified voucher pack details
     * @return ResponseEntity containing the updated voucher pack details
     */
    @PutMapping("/{voucherPackId}")
    @Operation(
        summary = "Update an existing voucher pack (Internal)",
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
    @LogBusinessOperation(
        value = "Update voucher pack",
        category = "VOUCHER_PACK_UPDATE"
    )
    @LogMethodEntry
    @LogMethodExit
    @LogMethodError
    public ResponseEntity<VoucherPackInternalResponse> updateVoucherPack(
            @Parameter(description = "Unique identifier of the voucher pack to update")
            @PathVariable @NotNull Long voucherPackId,
            @Valid @RequestBody VoucherPackUpdateRequest request) {
        VoucherPackInternalResponse updatedPack = voucherPackInternalApi.updateVoucherPack(voucherPackId, request);
        return ResponseEntity.ok(updatedPack);
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
     * @param voucherPackId the unique identifier of the voucher pack to delete
     * @return ResponseEntity indicating the result of the deletion operation
     */
    @DeleteMapping("/{voucherPackId}")
    @Operation(
        summary = "Delete a voucher pack (Internal)",
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
    @LogBusinessOperation(
        value = "Delete voucher pack",
        category = "VOUCHER_PACK_DELETE"
    )
    @LogMethodEntry
    @LogMethodExit
    @LogMethodError
    public ResponseEntity<Void> deleteVoucherPack(
            @Parameter(description = "Unique identifier of the voucher pack to delete")
            @PathVariable @NotNull Long voucherPackId) {
        // TODO: Implement deleteVoucherPack in VoucherPackInternalApi
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * Closes a voucher pack, making it unavailable for customer redemption.
     * 
     * <p>This endpoint allows administrators to manually close voucher packs
     * that are no longer needed or have been discontinued.</p>
     * 
     * <p><strong>Closure Rules:</strong></p>
     * <ul>
     *   <li>Only PENDING or PUBLISHED packs can be closed</li>
     *   <li>CLOSED and EXPIRED packs cannot be closed again</li>
     *   <li>Existing vouchers remain valid until their individual expiration</li>
     * </ul>
     * 
     * @param voucherPackId the unique identifier of the voucher pack to close
     * @return ResponseEntity indicating the result of the closure operation
     */
    @PutMapping("/{voucherPackId}/close")
    @Operation(
        summary = "Close a voucher pack (Internal)",
        description = "Closes a voucher pack, making it unavailable for customer redemption"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Voucher pack closed successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Voucher pack not found"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Voucher pack cannot be closed (already closed or expired)"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @LogBusinessOperation(
        value = "Close voucher pack",
        category = "VOUCHER_PACK_CLOSE"
    )
    @LogMethodEntry
    @LogMethodExit
    @LogMethodError
    public ResponseEntity<Void> closeVoucherPack(
            @Parameter(description = "Unique identifier of the voucher pack to close")
            @PathVariable @NotNull Long voucherPackId) {
        voucherPackInternalApi.closeVoucherPack(voucherPackId);
        return ResponseEntity.ok().build();
    }

    /**
     * Publishes a PENDING voucher pack, making it available for customer redemption.
     * 
     * <p>This endpoint allows administrators to manually activate voucher packs
     * that were created with future start dates or were kept in PENDING status.</p>
     * 
     * <p><strong>Publication Rules:</strong></p>
     * <ul>
     *   <li>Only PENDING packs can be published</li>
     *   <li>Pack must have valid dates and configuration</li>
     *   <li>Publication is immediate regardless of packValidFrom date</li>
     * </ul>
     * 
     * @param voucherPackId the unique identifier of the voucher pack to publish
     * @return ResponseEntity containing the published voucher pack details
     */
    @PutMapping("/{voucherPackId}/publish")
    @Operation(
        summary = "Publish a voucher pack (Internal)",
        description = "Publishes a PENDING voucher pack, making it available for customer redemption"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Voucher pack published successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VoucherPackInternalResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Voucher pack not found"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Voucher pack cannot be published (wrong status)"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    @LogBusinessOperation(
        value = "Publish voucher pack",
        category = "VOUCHER_PACK_PUBLISH"
    )
    @LogMethodEntry
    @LogMethodExit
    @LogMethodError
    public ResponseEntity<VoucherPackInternalResponse> publishVoucherPack(
            @Parameter(description = "Unique identifier of the voucher pack to publish")
            @PathVariable @NotNull Long voucherPackId) {
        VoucherPackInternalResponse publishedPack = voucherPackInternalApi.publishVoucherPack(voucherPackId);
        return ResponseEntity.ok(publishedPack);
    }
}
