package com.poly.promotion.domain.application.api;

public interface PromotionApi {

    Object createNewPromotion(Object promotionDto);
    Object updatePromotion(Object promotionDto);
    void closePromotion(String promotionId);
    Object getPromotionById(String promotionId);
    Object getAllPromotions(int page, int size, String sortBy, String sortDirection);
    Object getActivePromotions(int page, int size, String sortBy, String sortDirection);
    Object getClosedPromotions(int page, int size, String sortBy, String sortDirection);
    Object getApplicablePromotionsForBooking(Object bookingDto);

}
