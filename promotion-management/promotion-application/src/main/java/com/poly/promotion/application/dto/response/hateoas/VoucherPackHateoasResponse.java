package com.poly.promotion.application.dto.response.hateoas;

import com.poly.promotion.domain.application.dto.response.external.VoucherPackExternalResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 * HATEOAS response model for VoucherPack endpoints.
 * Extends the base response with HATEOAS links for navigation.
 *
 * @author System
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "voucherPacks", itemRelation = "voucherPack")
public class VoucherPackHateoasResponse extends RepresentationModel<VoucherPackHateoasResponse> {

    private final VoucherPackExternalResponse data;

    public VoucherPackHateoasResponse(VoucherPackExternalResponse data) {
        this.data = data;
    }
}
