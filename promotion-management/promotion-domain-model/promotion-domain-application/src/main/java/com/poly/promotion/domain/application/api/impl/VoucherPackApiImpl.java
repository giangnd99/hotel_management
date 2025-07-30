package com.poly.promotion.domain.application.api.impl;

import com.poly.promotion.domain.application.api.VoucherPackApi;
import com.poly.promotion.domain.application.mapper.base.EntityToModelTransformer;
import com.poly.promotion.domain.application.mapper.base.ModelToEntityTransformer;
import com.poly.promotion.domain.application.model.VoucherPackModel;
import com.poly.promotion.domain.application.service.VoucherPackService;
import com.poly.promotion.domain.core.entity.VoucherPack;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherPackApiImpl implements VoucherPackApi {

    VoucherPackService voucherPackService;
    EntityToModelTransformer<VoucherPack, VoucherPackModel> voucherPackToVoucherPackModelTransformer;
    ModelToEntityTransformer<VoucherPackModel, VoucherPack> voucherPackModelToVoucherPackTransformer;

//    public VoucherPackApiImpl() {
//        this.voucherPackService = voucherPackService;
//        this.voucherPackToVoucherPackModelTransformer = voucherPackToVoucherPackModelTransformer;
//        this.voucherPackModelToVoucherPackTransformer = voucherPackModelToVoucherPackTransformer;
//    }

    @Override
    public VoucherPackModel getVoucherPackById(Long voucherPackId) {
        return voucherPackToVoucherPackModelTransformer.transform(
                voucherPackService.getVoucherPackById(voucherPackId)
        );
    }

    @Override
    public List<VoucherPackModel> getAllVoucherPacksWithStatus(Integer status) {
        return voucherPackToVoucherPackModelTransformer.transformCollection(
                voucherPackService.getAllVoucherPacksWithStatus(status)
        );
    }

    @Override
    public VoucherPackModel createVoucherPack(VoucherPackModel voucherPackModel) {
        return voucherPackToVoucherPackModelTransformer.transform(
                voucherPackService.createVoucherPack(
                        voucherPackModelToVoucherPackTransformer.transform(voucherPackModel)
                )
        );
    }

    @Override
    public VoucherPackModel updatePendingVoucherPack(Long voucherPackId, VoucherPackModel voucherPackModel) {
        return voucherPackToVoucherPackModelTransformer.transform(
                voucherPackService.updatePendingVoucherPack(voucherPackId,
                        voucherPackModelToVoucherPackTransformer.transform(voucherPackModel)
                )
        );
    }

    @Override
    public void closeVoucherPack(Long voucherPackId) {
        voucherPackService.closeVoucherPack(voucherPackId);
    }
}
