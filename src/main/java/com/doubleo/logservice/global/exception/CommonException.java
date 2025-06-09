package com.doubleo.logservice.global.exception;

import com.doubleo.logservice.global.exception.errorcode.BaseErrorCode;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public CommonException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
