package com.poly.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse extends BaseMessage{
    private ResponseStatus status = ResponseStatus.OK;
    private String errorCode;   // optional business/system code
    private String errorMessage;
}
