package com.poly.promotion.domain.application.api;

public interface CouponPackApi {

    Object createNewCouponPack(Object couponPackDto);

    Object updateCouponPack(Object couponPackDto);

    void closeCouponPack(String couponPackId);

    Object getCouponPackById(String couponPackId);

    Object getAllCouponPacks(int page, int size);

    Long getRequiredPointsForCouponPack(String couponPackId);

}
