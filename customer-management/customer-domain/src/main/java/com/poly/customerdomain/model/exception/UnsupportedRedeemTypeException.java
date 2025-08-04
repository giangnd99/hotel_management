package com.poly.customerdomain.model.exception;

public class UnsupportedRedeemTypeException extends DomainException {
    public UnsupportedRedeemTypeException() {
        super("Không hỗ trợ loại đổi hiện tại.");
    }

    @Override
    public String getErrorCode() {
        return "UNSUPPORTED_REDEEM_TYPE";
    }
}
