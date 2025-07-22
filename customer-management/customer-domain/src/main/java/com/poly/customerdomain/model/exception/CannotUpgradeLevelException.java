package com.poly.customerdomain.model.exception;

public class CannotUpgradeLevelException extends DomainException{
    public CannotUpgradeLevelException() {
        super("Hiện tại đã là Level cao nhất rồi.");
    }

    @Override
    public String getErrorCode() {
        return "CANT_UPGRADE_LEVEL";
    }
}
