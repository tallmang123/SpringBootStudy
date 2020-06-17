package com.tallmang.aop;

import com.tallmang.common.Json;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Aspect
public class TallmangAop {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Around("execution(* com.tallmang.controller.web.LoginController.*(..))")
	public Object commonBeforeProcess(final ProceedingJoinPoint joinPoint) throws Throwable
	{
        Object aopObject = joinPoint.proceed();

        ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();

        Object inputParam = "";
        for (Object obj : joinPoint.getArgs()) {
            if (obj instanceof String) {
                inputParam = obj;
            }
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();

        LinkedHashMap <String,String> apiLogMap =  new LinkedHashMap<>();
        apiLogMap.put("Date",dateFormat.format(currentDate));
        apiLogMap.put("Api",request.getRequestURI());
        apiLogMap.put("Controller",joinPoint.getSignature().getName());
        apiLogMap.put("RequestData",inputParam.toString());
        apiLogMap.put("ResponseData",aopObject.toString());
        logger.info(apiLogMap.toString());
		return aopObject;
	}
	

}
