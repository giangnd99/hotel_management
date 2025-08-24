package com.poly.customerdomain.model.exception;

public class NotEnoughPointToUpgradeException extends DomainException {
    public NotEnoughPointToUpgradeException() {
        super("Điểm của khách hàng không đủ để nâng cấp.");
    }

    @Override
    public String getErrorCode() {
        return "NOT_ENOUGH_POINT_TO_UPGRADE";
    }
}
