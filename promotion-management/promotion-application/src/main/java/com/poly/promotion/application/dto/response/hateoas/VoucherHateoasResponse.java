package com.poly.promotion.application.dto.response.hateoas;

import com.poly.promotion.domain.application.dto.response.external.VoucherExternalResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 * HATEOAS response model for Voucher endpoints.
 * Extends the base response with HATEOAS links for navigation.
 *
 * @author System
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "vouchers", itemRelation = "voucher")
public class VoucherHateoasResponse extends RepresentationModel<VoucherHateoasResponse> {

    private final VoucherExternalResponse data;

    public VoucherHateoasResponse(VoucherExternalResponse data) {
        this.data = data;
    }
}
