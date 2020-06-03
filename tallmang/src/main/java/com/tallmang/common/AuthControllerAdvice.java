package com.tallmang.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/*
* Manage Exception Handler
*/
@ControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(AuthException.class)
    @ResponseBody
    public String AuthExceptionHandler(HttpServletRequest request, AuthException authException) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(authException);
        } catch (IOException e) {
            e.printStackTrace();
            return "{ \"code\" : " + ErrorCode.INVALID_EXCEPTION.getCode() +", \"message\" : \"" + ErrorCode.INVALID_EXCEPTION.getMessage() + "\" }";
        }
    }
}
