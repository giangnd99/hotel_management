package com.poly.promotion.application.service;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Service for building HATEOAS links for all API endpoints.
 * Provides consistent link generation across the application.
 *
 * @author System
 * @since 1.0.0
 */
@Service
public class HateoasLinkBuilderService {

    /**
     * Builds HATEOAS links for VoucherPack external endpoints.
     *
     * @param voucherPackId the voucher pack ID
     * @return array of HATEOAS links
     */
    public Link[] buildVoucherPackLinks(Long voucherPackId) {
        return new Link[]{
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherPackExternalController.class)
                        .getAvailableVoucherPacks()).withRel("voucherPacks"),
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherPackExternalController.class)
                        .getVoucherPackById(voucherPackId)).withSelfRel(),
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherController.class)
                        .redeemVouchers(voucherPackId, null)).withRel("redeemVoucher")
        };
    }

    /**
     * Builds HATEOAS links for VoucherPack internal endpoints.
     *
     * @param voucherPackId the voucher pack ID
     * @return array of HATEOAS links
     */
    public Link[] buildVoucherPackInternalLinks(Long voucherPackId) {
        return new Link[]{
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherPackInternalController.class)
                        .getAllVoucherPacks()).withRel("voucherPacks"),
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherPackInternalController.class)
                        .getVoucherPackById(voucherPackId)).withSelfRel(),
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherPackInternalController.class)
                        .createVoucherPack(null)).withRel("create"),
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherPackInternalController.class)
                        .updateVoucherPack(voucherPackId, null)).withRel("update"),
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherPackInternalController.class)
                        .publishVoucherPack(voucherPackId)).withRel("publish"),
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherPackInternalController.class)
                        .closeVoucherPack(voucherPackId)).withRel("close"),
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherPackInternalController.class)
                        .deleteVoucherPack(voucherPackId)).withRel("delete")
        };
    }

    /**
     * Builds HATEOAS links for Voucher endpoints.
     *
     * @param voucherId the voucher ID
     * @return array of HATEOAS links
     */
    public Link[] buildVoucherLinks(String voucherId) {
        return new Link[]{
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherPackExternalController.class)
                        .getAvailableVoucherPacks()).withRel("voucherPacks")
        };
    }

    /**
     * Builds HATEOAS links for collection endpoints.
     *
     * @return array of HATEOAS links
     */
    public Link[] buildCollectionLinks() {
        return new Link[]{
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherPackExternalController.class)
                        .getAvailableVoucherPacks()).withRel("voucherPacks"),
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherPackInternalController.class)
                        .getAllVoucherPacks()).withRel("internalVoucherPacks"),
                Link.of("/actuator/health").withRel("health")
        };
    }

    /**
     * Builds HATEOAS links for root endpoint.
     *
     * @return array of HATEOAS links
     */
    public Link[] buildRootLinks() {
        return new Link[]{
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherPackExternalController.class)
                        .getAvailableVoucherPacks()).withRel("voucherPacks"),
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherPackInternalController.class)
                        .getAllVoucherPacks()).withRel("internalVoucherPacks"),
                Link.of("/actuator/health").withRel("health"),
                Link.of("/actuator").withRel("actuator"),
                Link.of("/swagger-ui.html").withRel("swagger-ui"),
                Link.of("/v3/api-docs").withRel("openapi")
        };
    }
}
