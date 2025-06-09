package com.doubleo.logservice.global.exception.errorcode;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {
    HttpStatus getHttpStatus();

    String getMessage();

    String errorClassName();
}
