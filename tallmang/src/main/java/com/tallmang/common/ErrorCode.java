package com.tallmang.common;

public enum ErrorCode {
    SUCCESS(200,"success"),
    INVALID_EXCEPTION(9999, "invalid exception");

    final private Integer code;
    final private String message;

    ErrorCode(int code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public int getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }
}




