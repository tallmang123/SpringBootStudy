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
@ControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(AuthException.class)
    @ResponseBody
    public String AuthExceptionHandler(AuthException authException) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String,String> exceptionData = new HashMap<>();
            exceptionData.put("code",String.valueOf(authException.getErrorCode()));
            exceptionData.put("message",authException.getErrorMessage());
            return objectMapper.writeValueAsString(exceptionData);
        } catch (Exception e) {
            e.printStackTrace();
            return "{ \"code\" : " + ErrorCode.INTERNAL_ERROR.getCode() +", \"message\" : \"" + ErrorCode.INTERNAL_ERROR.getMessage() + "\" }";
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String ExceptionHandler(Exception exception){
        try {
            Map<String,String> exceptionData = new HashMap<>();
            exceptionData.put("code",String.valueOf(ErrorCode.INTERNAL_ERROR.getCode()));
            exceptionData.put("message",ErrorCode.INTERNAL_ERROR.getMessage());

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(exceptionData);
        } catch (Exception e) {
            e.printStackTrace();
            return "{ \"code\" : " + ErrorCode.INTERNAL_ERROR.getCode() +", \"message\" : \"" + ErrorCode.INTERNAL_ERROR.getMessage() + "\" }";
        }
    }
}
