package com.tallmang.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/*
* Manage Exception Handler
*/
//@ControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(AuthException.class)
    @ResponseBody
    public String AuthExceptionHandler(AuthException authException) {
        try {
            authException.printStackTrace();
            Map<String,Object> exceptionData = new HashMap<>();
            exceptionData.put("code",authException.getErrorCode());
            exceptionData.put("message",authException.getErrorMessage());
            return Json.encodeJsonString(exceptionData);
        } catch (Exception e) {
            e.printStackTrace();
            return "{ \"code\" : " + ErrorCode.INTERNAL_ERROR.getCode() +", \"message\" : \"" + ErrorCode.INTERNAL_ERROR.getMessage() + "\" }";
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String ExceptionHandler(Exception exception){
        try {
            exception.printStackTrace();
            Map<String,Object> exceptionData = new HashMap<>();
            exceptionData.put("code",ErrorCode.INTERNAL_ERROR.getCode());
            exceptionData.put("message",ErrorCode.INTERNAL_ERROR.getMessage());
            return Json.encodeJsonString(exceptionData);
        } catch (Exception e) {
            e.printStackTrace();
            return "{ \"code\" : " + ErrorCode.INTERNAL_ERROR.getCode() +", \"message\" : \"" + ErrorCode.INTERNAL_ERROR.getMessage() + "\" }";
        }
    }
}
