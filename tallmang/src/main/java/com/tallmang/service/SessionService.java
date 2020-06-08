package com.tallmang.service;


import com.tallmang.common.AuthException;
import com.tallmang.common.ErrorCode;
import com.tallmang.common.Json;
import com.tallmang.common.encrypt.AES256;
import org.h2.engine.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@Service
public class SessionService {

	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Value("${spring.session.expire-time}")
	int timeout;

	public SessionService() throws UnsupportedEncodingException
	{
		AES256 aes256 = AES256.getInstance();
	}

	/**
	 * get HttpServletReqeust object
	 */
	public HttpServletRequest getRequest() {
		ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		return attr.getRequest();
	}

	/**
	 * get HttpServletResponse object
	 */
	public HttpServletResponse getResponse() {
		ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		return attr.getResponse();
	}

	public void createSession(String id, String password) throws Exception{
		String sessionKey = Base64.getEncoder().encodeToString(AES256.encode( id + password).getBytes());
		System.out.println(sessionKey);
		// 1. delete session if it is exists
		if(redisTemplate.opsForValue().get(sessionKey) != null)
		{
			redisTemplate.delete(sessionKey);
		}
		Map<String,Object> sessionData = new HashMap<>();
		sessionData.put("id", id);
		sessionData.put("password", password);

		// 2. create new session
		redisTemplate.opsForValue().set(sessionKey, Json.encodeJsonString(sessionData), this.timeout);

		// 3. save session key on cookie
		Cookie storeIdCookie = new Cookie("TSESSIONID", sessionKey);
		storeIdCookie.setPath("/");
		storeIdCookie.setMaxAge(60 * 60); // one hour
		HttpServletResponse response = this.getResponse();
		response.addCookie(storeIdCookie);
	}

	public Map<String,String> getSession() throws Exception{
		String sessionKey = "";

		HttpServletRequest request = this.getRequest();
		Cookie[] getCookie = request.getCookies();
		for (Cookie cookie : getCookie) {
			if (cookie.getName().equals("TSESSIONID")) {
				sessionKey = cookie.getValue();
				break;
			}
		}

		if(sessionKey.isEmpty())
		{
			throw new AuthException(ErrorCode.INVALID_SESSIONKEY);
		}

		Object sessionJsonData = redisTemplate.opsForValue().get(sessionKey);
		if(sessionJsonData == null)
		{
			throw new AuthException(ErrorCode.INVALID_SESSION);
		}
		return Json.decodeJsonString(sessionJsonData.toString());
	}

	public String getData(String dataKey) throws Exception{
		Map<String,String> sessionData = this.getSession();
		return sessionData.get(dataKey);
	}

	public void saveData(String key , String value) throws Exception{
		Map<String,String> sessionData = this.getSession();
		sessionData.put(key,value);
	}

	public void saveDataList( Map<String,String> dataList) throws Exception{
		Map<String,String> sessionData = this.getSession();
		dataList.forEach(sessionData::put);
	}

}
