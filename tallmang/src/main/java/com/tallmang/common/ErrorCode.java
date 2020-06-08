package com.tallmang.common;

public enum ErrorCode {
    SUCCESS(200,"success"),
    INTERNAL_ERROR(500,"Internal Error"),

    INVALID_PARAMETER(4000, "Invalid Parameter"),

    INVALID_SESSION(5000, "Invalid Session"),
    INVALID_SESSIONKEY(5001, "Invalid Session Key"),


    INVALID_EXCEPTION(9999, "invalid exception"),

    NO_USER(10000, "No User"),
    INVALID_PASSWORD(10001, "Password Not Match");

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




