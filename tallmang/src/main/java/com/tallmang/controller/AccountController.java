package com.tallmang.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tallmang.entity.AccountEntity;
import com.tallmang.mybatis.vo.AccountVO;
import com.tallmang.service.AccountService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class AccountController {
	
	@Autowired
	AccountService accountService;
	
	@GetMapping(value="/test")
	public String testApi() {
		AccountVO account = accountService.getAccountInfo();
		return account.toString();
	}
	
	
	@GetMapping(value="/jpa")
	public String testJpa() {
		List<AccountEntity> accountEntityList = accountService.getAccountInfoJpa();
		return accountEntityList.toString();
	}
	
	

}
