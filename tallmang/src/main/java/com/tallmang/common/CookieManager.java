package com.tallmang.common;

import com.tallmang.common.encrypt.AES256;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class CookieManager {

    ServletRequestAttributes attr;
    HttpServletRequest request;
    HttpServletResponse response;

    public CookieManager()
    {
        this.attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        this.request = attr.getRequest();
        this.response = attr.getResponse();
    }

    public void setData(String cookieKey , String cookieData, int expire) throws Exception
    {
        Cookie cookie = new Cookie(cookieKey, cookieData);
        cookie.setMaxAge(expire);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void setDataMap(Map<String,String> dataMap, int expire) throws Exception
    {
        for(String cookieKey : dataMap.keySet())
        {
            Cookie cookie = new Cookie(cookieKey, dataMap.get(cookieKey));
            cookie.setMaxAge(expire);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }

    public String getData(String cookieKey) throws Exception
    {
        Cookie[] getCookie = request.getCookies();
        String cookieData = null;
        for (Cookie cookie : getCookie) {
            if (cookie.getName().equals(cookieKey)) {
                cookieData = cookie.getValue();
            }
        }
        return cookieData;
    }

    public void deleteData(String cookieKey) throws Exception
    {
        Cookie cookie = new Cookie(cookieKey, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
