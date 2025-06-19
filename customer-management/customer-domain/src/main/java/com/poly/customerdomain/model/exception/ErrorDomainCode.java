package com.poly.customerdomain.model.exception;

public enum ErrorDomainCode {

    NAME_INVALID(1, "Tên không hợp lệ !!!"),
    DATE_OF_BIRTH_INVALID(2, "Ngày sinh không hợp lệ !!!"),
    LEVEL_INVALID(3, "Cấp độ thành viên không hợp lệ !!!"),
    USERID_INVALID(4, "User Id không hợp lệ !!!"),
    ADDRESS_INVALID(5, "Địa chỉ không hợp lệ !"),

    UPDATE_INFO_INVALID(20, "Cập nhật thông tin khách hàng không thành công !"),
    CUSTOMER_NOT_FOUND(21, "Khách hàng không tồn tại hoặc không tìm thấy !");

    private final int code;

    private final String message;

    ErrorDomainCode (int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
