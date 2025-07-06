package com.poly.customerdomain.model.exception;

public class BlankCustomerAddressException extends DomainException {
    public BlankCustomerAddressException(String street, String ward, String district, String city) {
        super("Thông tin địa chỉ trống tại: %s %s %s %s".formatted(street, ward, district, city));

    }
    @Override
    public String getErrorCode() {
        return "CUSTOMER_ADDRESS_BLANK";
    }
}
