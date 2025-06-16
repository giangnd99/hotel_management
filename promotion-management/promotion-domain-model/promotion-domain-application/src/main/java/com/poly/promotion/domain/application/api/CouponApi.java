package com.poly.promotion.domain.application.api;

public interface CouponApi {

        /**
         * Applies coupons to the user's account.
         *
         * @param couponCodes the codes of the coupons to be applied
         * @return the total discount amount from the applied coupons
         */
        Object applyCoupons(String... couponCodes);


        /**
         * Closes the specified coupons.
         *
         * @param couponCodes the codes of the coupons to be closed
         */
        Object closeCoupons(String... couponCodes);

        /**
         * Retrieves all active coupons for a given user ID.
         *
         * @param userId the ID of the user whose active coupons are to be retrieved
         * @param page the page number for pagination
         * @param size the number of items per page
         * @return a paginated list of active coupons for the specified user
         */
        Object getAllActiveCouponsByUserId(String userId, int page, int size);

        /**
         * Retrieves all coupons for a given user ID.
         *
         * @param userId the ID of the user whose active coupons are to be retrieved
         * @param page the page number for pagination
         * @param size the number of items per page
         * @return a paginated list of all coupons for the specified user
         */
        Object getAllCouponsByUserId(String userId, int page, int size);

        /**
         * Retrieves a coupon by its code.
         *
         * @param couponCode the code of the coupon to retrieve
         * @return the coupon associated with the given code
         */
        Object getCouponByCode(String couponCode);

        /**
         * Redeems a coupon for a user.
         *
         * @param userId the ID of the user redeeming the coupon
         * @param couponPackId the ID of the coupon pack being redeemed
         * @return the result of the redemption process
         */
        Object redeemCoupon(String userId, String couponPackId);
}
