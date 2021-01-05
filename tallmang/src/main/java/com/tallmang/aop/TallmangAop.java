package com.tallmang.aop;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallmang.common.AuthException;
import com.tallmang.common.ErrorCode;
import com.tallmang.common.Json;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.ServletResponse;
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

	@Around("execution(* com.tallmang.controller.api.*.*(..))")
	public Object commonApiAspect(final ProceedingJoinPoint joinPoint) throws Throwable
	{
	    try
        {
            Object aopObject = joinPoint.proceed();

            this.setLogData(joinPoint.getSignature().getName(), joinPoint.getArgs(), aopObject.toString());
            return aopObject;
        }
	    catch(AuthException authException)
        {
            Map<String,Object> exceptionData = new HashMap<>();
            exceptionData.put("code",authException.getErrorCode());
            exceptionData.put("message",authException.getErrorMessage());

            this.setLogData(joinPoint.getSignature().getName(), joinPoint.getArgs(), Json.encodeJsonString(exceptionData));

            return Json.encodeJsonString(exceptionData);

        }
	    catch(Exception exception)
        {
            Map<String,Object> exceptionData = new HashMap<>();
            exceptionData.put("code", ErrorCode.INTERNAL_ERROR.getCode());
            exceptionData.put("message",ErrorCode.INTERNAL_ERROR.getMessage());

            this.setLogData(joinPoint.getSignature().getName(), joinPoint.getArgs(), Json.encodeJsonString(exceptionData));

            return Json.encodeJsonString(exceptionData);
        }
	    /*Object aopObject = joinPoint.proceed();

		ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
        Object inputParam = "";
        for (Object obj : joinPoint.getArgs())
        {
            if (obj instanceof String) {
                inputParam = obj;
            }
			if (obj instanceof Map) {
				inputParam = Json.encodeJsonString(obj);
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

		return aopObject;*/
	}

    public void setLogData(String controller, Object[] requestMap, String response) throws Exception
    {
        LinkedHashMap <String,String> apiLogMap =  new LinkedHashMap<>();

        ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        String requestUri = attr.getRequest().getRequestURI();

        Object inputParam = "";
        for (Object obj : requestMap)
        {
            if (obj instanceof String) {
                inputParam = obj;
            }
            if (obj instanceof Map) {
                inputParam = Json.encodeJsonString(obj);
            }
        }
        apiLogMap.put("Date",new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss").format(new Date()));
        apiLogMap.put("Api",requestUri);
        apiLogMap.put("Controller",controller);
        apiLogMap.put("RequestData",inputParam.toString());
        apiLogMap.put("ResponseData",response);

        logger.info(apiLogMap.toString());
    }

}
