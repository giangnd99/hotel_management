package com.poly.promotion.application.dto.response.hateoas;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Map;

/**
 * HATEOAS response model for monitoring health endpoints.
 * Extends the base response with HATEOAS links for navigation.
 *
 * @author System
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "healthStatuses", itemRelation = "healthStatus")
public class MonitoringHealthHateoasResponse extends RepresentationModel<MonitoringHealthHateoasResponse> {

    private final Map<String, Object> data;

    public MonitoringHealthHateoasResponse(Map<String, Object> data) {
        this.data = data;
    }
}
