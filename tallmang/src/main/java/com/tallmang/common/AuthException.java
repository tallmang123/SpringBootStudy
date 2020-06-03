package com.tallmang.common;

import org.springframework.web.bind.annotation.ExceptionHandler;

public class AuthException extends RuntimeException{

    private int errorCode;
    private String errorMessage;

    public AuthException(int errorCode)
    {
        this.errorCode = errorCode;
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

