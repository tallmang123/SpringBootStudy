package com.tallmang.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.tallmang.common.ErrorCode;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
	
	@Autowired
	RedisTemplate<String, Object> redisTemplate;
	
	public String manualLoginProcess(String userId, String password)
	{
		AccountEntity accountEntity = accountRepository.findById(userId);
		return null;
	}
	
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
	
	public String getRedisData()
	{	
		redisTemplate.opsForValue().set("test", "This is Test");
		String result = (String) redisTemplate.opsForValue().get("test");
		return result;
	}
	
	public String getSessionRedis(HttpSession httpSession)
	{
		return httpSession.getId();	
	}

}
