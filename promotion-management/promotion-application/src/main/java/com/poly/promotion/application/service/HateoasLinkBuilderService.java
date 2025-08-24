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
     * Builds HATEOAS links for VoucherPack endpoints.
     *
     * @param voucherPackId the voucher pack ID
     * @return array of HATEOAS links
     */
    public Link[] buildVoucherPackLinks(Long voucherPackId) {
        return new Link[]{
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherPackController.class)
                        .getAvailableVoucherPacks()).withRel("voucherPacks"),
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherPackController.class)
                        .getAvailableVoucherPacks()).withSelfRel(),
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherPackController.class)
                        .updateVoucherPack(voucherPackId, null)).withRel("update"),
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherPackController.class)
                        .deleteVoucherPack(voucherPackId)).withRel("delete"),
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherController.class)
                        .redeemVoucherFromPack(null)).withRel("redeemVoucher")
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
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherController.class)
                        .getCustomerVouchers(null)).withRel("vouchers"),
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherController.class)
                        .getCustomerVouchers(null)).withSelfRel(),
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherPackController.class)
                        .getAvailableVoucherPacks()).withRel("voucherPacks")
        };
    }

    /**
     * Builds HATEOAS links for monitoring health endpoints.
     *
     * @return array of HATEOAS links
     */
    public Link[] buildMonitoringHealthLinks() {
        return new Link[]{
                linkTo(methodOn(com.poly.promotion.application.controller.MonitoringHealthController.class)
                        .getHealthStatus()).withRel("health"),
                linkTo(methodOn(com.poly.promotion.application.controller.MonitoringHealthController.class)
                        .getUnsyncedData()).withRel("unsyncedData"),
                linkTo(methodOn(com.poly.promotion.application.controller.MonitoringHealthController.class)
                        .triggerManualSync()).withRel("manualSync"),
                linkTo(methodOn(com.poly.promotion.application.controller.MonitoringHealthController.class)
                        .triggerManualCleanup()).withRel("manualCleanup"),
                linkTo(methodOn(com.poly.promotion.application.controller.MonitoringHealthController.class)
                        .getDataTypeStatistics("METHOD_EXECUTION_METRICS")).withRel("methodExecutionStats"),
                linkTo(methodOn(com.poly.promotion.application.controller.MonitoringHealthController.class)
                        .getDataTypeStatistics("PERFORMANCE_METRICS")).withRel("performanceStats"),
                linkTo(methodOn(com.poly.promotion.application.controller.MonitoringHealthController.class)
                        .getDataTypeStatistics("SYSTEM_HEALTH_METRICS")).withRel("systemHealthStats")
        };
    }

    /**
     * Builds HATEOAS links for collection endpoints.
     *
     * @return array of HATEOAS links
     */
    public Link[] buildCollectionLinks() {
        return new Link[]{
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherPackController.class)
                        .getAvailableVoucherPacks()).withRel("voucherPacks"),
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherController.class)
                        .getCustomerVouchers(null)).withRel("vouchers"),
                linkTo(methodOn(com.poly.promotion.application.controller.MonitoringHealthController.class)
                        .getHealthStatus()).withRel("monitoringHealth"),
                linkTo(methodOn(com.poly.promotion.application.controller.MonitoringHealthController.class)
                        .getHealthStatus()).withRel("actuator")
        };
    }

    /**
     * Builds HATEOAS links for root endpoint.
     *
     * @return array of HATEOAS links
     */
    public Link[] buildRootLinks() {
        return new Link[]{
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherPackController.class)
                        .getAvailableVoucherPacks()).withRel("voucherPacks"),
                linkTo(methodOn(com.poly.promotion.application.controller.VoucherController.class)
                        .getCustomerVouchers(null)).withRel("vouchers"),
                linkTo(methodOn(com.poly.promotion.application.controller.MonitoringHealthController.class)
                        .getHealthStatus()).withRel("monitoringHealth"),
                linkTo(methodOn(com.poly.promotion.application.controller.MonitoringHealthController.class)
                        .getHealthStatus()).withRel("actuator"),
                Link.of("/actuator").withRel("actuator"),
                Link.of("/swagger-ui.html").withRel("swagger-ui"),
                Link.of("/v3/api-docs").withRel("openapi")
        };
    }
}
