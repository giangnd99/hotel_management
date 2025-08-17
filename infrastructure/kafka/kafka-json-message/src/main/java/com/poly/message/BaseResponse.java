package com.poly.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseResponse extends BaseMessage{
    private ResponseStatus status = ResponseStatus.OK;
    private String errorCode;
    private String errorMessage;
}
