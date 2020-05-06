package com.tallmang.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tallmang.entity.AccountEntity;
import com.tallmang.mybatis.mapper.AccountMapper;
import com.tallmang.mybatis.vo.AccountVO;
import com.tallmang.repository.AccountRepository;

@Service
@Transactional
public class AccountService {
	
	@Autowired
	AccountMapper accountMapper;
	
	@Autowired
	AccountRepository accountRepository;
	
	public AccountVO getAccountInfo()
	{
		AccountVO account = accountMapper.getUser();
		System.out.println(account.toString());
		return account;
	}
	
	public List<AccountEntity> getAccountInfoJpa()
	{
		List<AccountEntity> accountEntityList = accountRepository.findAll();
		return accountEntityList;
	}

}
