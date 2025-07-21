package com.poly.customerapplicationservice.exception;

public class VoucherRedeemFailedException extends ApplicationServiceException{
    public VoucherRedeemFailedException() {
        super("Không thể đổi voucher. Vui lòng thử lại sau.");
    }

    public VoucherRedeemFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getErrorCode() {
        return "VOUCHER_REDEEM_FAILED";
    }
}
