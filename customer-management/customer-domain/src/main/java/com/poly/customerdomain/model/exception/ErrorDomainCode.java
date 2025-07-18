package com.poly.customerdomain.model.exception;

public enum ErrorDomainCode {

    NAME_INVALID(1, "Tên không được để trống !!!"),
    DATE_OF_BIRTH_INVALID(2, "Ngày sinh không hợp lệ !!!"),
    LEVEL_INVALID(3, "Cấp độ thành viên không hợp lệ !!!"),
    USERID_INVALID(4, "User Id không được để trống !!!");

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
