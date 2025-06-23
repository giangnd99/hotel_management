package com.springboot.asm.fpoly_asm_springboot.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_EXISTED(1001, "User already existed", HttpStatus.CONFLICT),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_INVALID(1003, "Username is invalid, please input at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password is invalid, please input at least 8 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED_EXCEPTION(1006, "Unauthenticated error", HttpStatus.UNAUTHORIZED),
    PRODUCT_NOT_EXISTED(2001, "Product not existed", HttpStatus.NOT_FOUND),
    PRODUCT_ALREADY_EXISTED(2005, "Product already existed", HttpStatus.CONFLICT),
    UNAUTHORIZED(1009, "You don't have permission", HttpStatus.FORBIDDEN),
    INVALID_KEY(9991, "Invalid key", HttpStatus.BAD_REQUEST),
    FIELD_BLANK(1011, "Field first name is blank", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED(3001, "Category already existed", HttpStatus.CONFLICT),
    CATEGORY_NOT_EXISTED(3005, "Category not existed", HttpStatus.CONFLICT),
    INVALID_PARAMETER(6001, "Invalid Parameter", HttpStatus.BAD_REQUEST),
    RESPONSE_NOT_FOUND(6002, "Not Found Payment Response", HttpStatus.NOT_FOUND),
    INVALID_REQUEST_BODY(6007, "Invalid Request Body", HttpStatus.BAD_REQUEST),
    CREATE_PAYMENT_FAILED(6008, "Create payment failed",HttpStatus.INTERNAL_SERVER_ERROR),
    CART_ITEM_ALREADY_EXISTS(4001, "Cart item already exists", HttpStatus.CONFLICT),
    CART_ITEM_NOT_FOUND(4002, "Cart item not found", HttpStatus.NOT_FOUND),
    INVALID_CART_ITEM(4003, "Invalid cart item", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND(5001, "Order not existed", HttpStatus.NOT_FOUND),
    ORDER_ALREADY_EXISTED(5005, "Order already existed", HttpStatus.CONFLICT),
    ORDER_NOT_SPENDING(5010, "Only pending orders can be confirmed. ", HttpStatus.CONFLICT),
    ORDER_NOT_PROCESSING(5020, "Only processing orders can be shipped. ", HttpStatus.CONFLICT),
    ORDER_NOT_SHIPPING(5030, "Only shipping orders can be completed.", HttpStatus.CONFLICT),
    ORDER_NOT_CANCEL(5040, "Cannot cancel a completed order.", HttpStatus.CONFLICT),
    AVATAR_NOT_PERMISSION(1111, "Cannot change a avatar of other user.", HttpStatus.CONFLICT),
    ;


    private int code;
    private String message;
    private HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
