package com.tallmang.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
	
	@PostMapping(value="/login")
	public String ManualLogin()
	{
		//password : sha256(md5(string value) . db salt(3));
		return null;
	}
	
	public String AutoLogin()
	{
		return null;
	}

}
