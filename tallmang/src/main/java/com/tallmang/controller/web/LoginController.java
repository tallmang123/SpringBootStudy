package com.tallmang.controller.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallmang.common.AuthException;
import com.tallmang.common.ErrorCode;
import com.tallmang.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

	@Autowired
	AccountService accountService;
	
	@PostMapping(value="/login")
	@ResponseBody
	public String ManualLogin(HttpServletRequest request, @RequestBody String requestJsonBody) throws Exception
	{
		System.out.println(requestJsonBody);
		//password : sha256(md5(string value) . db salt(3));
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> requestMap = mapper.readValue(requestJsonBody, new TypeReference<Map<String, String>>(){});
		System.out.println(requestMap);

		requestMap.forEach((key,value) -> {
			System.out.println(key);
			if(value.isBlank() || value == null)
			{
				throw new AuthException(ErrorCode.INVALID_PARAMETER);
			}
			System.out.println(value);
		});
		String userId		= requestMap.get("userId");
		String md5Password	= requestMap.get("password");

		accountService.manualLoginProcess(request, userId, md5Password);
		return null;
	}
	
	public String AutoLogin()
	{
		return null;
	}

}
