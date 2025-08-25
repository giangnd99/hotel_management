package com.poly.promotion.application.controller;

import com.poly.promotion.application.service.HateoasLinkBuilderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.poly.promotion.application.annotation.LogBusinessOperation;
import com.poly.promotion.application.annotation.LogMethodEntry;
import com.poly.promotion.application.annotation.LogMethodError;
import com.poly.promotion.application.annotation.LogMethodExit;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Root controller that provides HATEOAS links to all available endpoints.
 * Serves as the entry point for API discovery and navigation.
 *
 * @author System
 * @since 1.0.0
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Tag(name = "Root", description = "Root endpoint with HATEOAS links to all available resources")
public class RootController {

    private final HateoasLinkBuilderService hateoasLinkBuilderService;

    /**
     * Provides HATEOAS links to all available endpoints.
     * This endpoint serves as the API discovery mechanism.
     *
     * @return HATEOAS response with links to all available resources
     */
    @GetMapping
    @Operation(
        summary = "Get API root with HATEOAS links",
        description = "Returns HATEOAS links to all available endpoints for API discovery and navigation"
    )
    @LogBusinessOperation(
        value = "Get API root with HATEOAS links",
        category = "API_DISCOVERY"
    )
    @LogMethodEntry
    @LogMethodExit
    @LogMethodError
    public ResponseEntity<RepresentationModel<?>> getRoot() {
        RepresentationModel<?> rootModel = new RepresentationModel<>();
        rootModel.add(hateoasLinkBuilderService.buildRootLinks());
        return ResponseEntity.ok(rootModel);
    }

    /**
     * Provides HATEOAS links to all available endpoints.
     * Alternative endpoint for API discovery.
     *
     * @return HATEOAS response with links to all available resources
     */
    @GetMapping("/api")
    @Operation(
        summary = "Get API discovery endpoint",
        description = "Returns HATEOAS links to all available API endpoints"
    )
    @LogBusinessOperation(
        value = "Get API discovery endpoint",
        category = "API_DISCOVERY"
    )
    @LogMethodEntry
    @LogMethodExit
    @LogMethodError
    public ResponseEntity<RepresentationModel<?>> getApiDiscovery() {
        RepresentationModel<?> discoveryModel = new RepresentationModel<>();
        discoveryModel.add(hateoasLinkBuilderService.buildRootLinks());
        return ResponseEntity.ok(discoveryModel);
    }
}
