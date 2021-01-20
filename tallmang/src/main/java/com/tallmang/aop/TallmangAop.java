package com.tallmang.aop;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallmang.common.AuthException;
import com.tallmang.common.ErrorCode;
import com.tallmang.common.Json;
import com.tallmang.common.encrypt.AES256;
import org.apache.commons.io.IOUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Aspect
public class TallmangAop {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Around("execution(* com.tallmang.controller.api.*.*(..))")
	public Object commonApiAspect(final ProceedingJoinPoint joinPoint) throws Throwable
	{
	    try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            String requestType = attr.getRequest().getMethod().toString();
            Object aopObject = null;
            if (attr.getRequest().getMethod().equals("POST"))
            {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(IOUtils.toByteArray(attr.getRequest().getInputStream()));

                BufferedReader reader = new BufferedReader(new InputStreamReader(byteArrayInputStream,"UTF-8"));
                String requestString = reader.lines().collect(Collectors.joining(System.lineSeparator()));

                aopObject = joinPoint.proceed(new Object[] {AES256.getInstance().decode(requestString)});
            }
            else
            {
                aopObject = joinPoint.proceed();
            }

            this.setLogData(joinPoint.getSignature().getName(), joinPoint.getArgs(), aopObject);
            return aopObject;
        }
	    catch(AuthException authException)
        {
            authException.printStackTrace();
            Map<String,Object> exceptionData = new HashMap<>();
            exceptionData.put("code",authException.getErrorCode());
            exceptionData.put("message",authException.getErrorMessage());

            this.setLogData(joinPoint.getSignature().getName(), joinPoint.getArgs(), exceptionData);

            return Json.encodeJsonString(exceptionData);

        }
	    catch(Exception exception)
        {
            exception.printStackTrace();
            Map<String,Object> exceptionData = new HashMap<>();
            exceptionData.put("code", ErrorCode.INTERNAL_ERROR.getCode());
            exceptionData.put("message",ErrorCode.INTERNAL_ERROR.getMessage());

            this.setLogData(joinPoint.getSignature().getName(), joinPoint.getArgs(), exceptionData);
            System.out.println(Json.encodeJsonString(exceptionData));
            return Json.encodeJsonString(exceptionData);
        }
	}

    public void setLogData(String controller, Object[] requestMap, Object responseObject) throws Exception
    {
        LinkedHashMap <String,String> apiLogMap =  new LinkedHashMap<>();

        ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        String requestUri = attr.getRequest().getRequestURI();

        String response = "";
        if(responseObject != null)
        {
            response = Json.encodeJsonString(responseObject);
        }

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
