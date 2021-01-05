package com.tallmang.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

//@Component
public class LoggingFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        ContentCachingRequestWrapper contentCachingRequestWrapper   = new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
        ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);

        filterChain.doFilter(contentCachingRequestWrapper,contentCachingResponseWrapper);

        /////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////API LOGGING////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode requestJson = objectMapper.readTree(contentCachingRequestWrapper.getContentAsByteArray());
        JsonNode responseJson = objectMapper.readTree(contentCachingResponseWrapper.getContentAsByteArray());

        LinkedHashMap<String,String> apiLogMap =  new LinkedHashMap<>();
        apiLogMap.put("Date",dateFormat.format(currentDate));
        apiLogMap.put("Api",contentCachingRequestWrapper.getRequestURI());
        apiLogMap.put("Request",requestJson.toString());
        apiLogMap.put("Response",responseJson.toString());
        logger.info(apiLogMap.toString());
        /////////////////////////////////////////////////////////////////////////////////

        contentCachingResponseWrapper.copyBodyToResponse();

    }

}
