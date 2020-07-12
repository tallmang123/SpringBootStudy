package com.tallmang.controller.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallmang.common.AuthException;
import com.tallmang.common.ErrorCode;
import com.tallmang.common.Json;
import com.tallmang.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

	@Autowired
	AccountService accountService;
	
	@PostMapping(value="/login")
	@ResponseBody
	public String ManualLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody String requestJsonBody) throws Exception
	{
		//password : sha256(md5(string value) . db salt(3));
		Map<String, Object> requestMap = Json.decodeJsonString(requestJsonBody);
		requestMap.forEach((key,value) -> {
			if(value.toString().isBlank())
				throw new AuthException(ErrorCode.INVALID_PARAMETER);
		});
		String userId		= requestMap.get("userId").toString();
		String md5Password	= requestMap.get("password").toString();
		boolean isAutoLogin = Boolean.parseBoolean(requestMap.get("autoLogin").toString());

		Map<String, Object> manualLoginResult = accountService.manualLoginProcess(request, response, userId, md5Password, isAutoLogin);
		return Json.encodeJsonString(manualLoginResult);
	}
	
	public String AutoLogin()
	{
		return null;
	}

}
