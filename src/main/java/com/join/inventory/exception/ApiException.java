package com.join.inventory.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class ApiException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1647315650756981670L;

    private final String errorCode;
    private final Object errorData;

    public ApiException(String errorCode, Object errorData) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorData = errorData;
    }
}