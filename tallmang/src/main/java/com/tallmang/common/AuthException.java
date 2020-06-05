package com.tallmang.common;

import org.springframework.web.bind.annotation.ExceptionHandler;

public class AuthException extends RuntimeException{

    private int errorCode;
    private String errorMessage;

    public AuthException(ErrorCode errorCode)
    {
        this.errorCode = errorCode.getCode();
        this.errorMessage = errorCode.getMessage();
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }
}

