package com.tallmang.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallmang.common.AuthException;
import com.tallmang.common.ErrorCode;
import com.tallmang.common.MessageEncrypt;
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
	
	public void manualLoginProcess(HttpServletRequest request, String userId, String md5Password) throws Exception
	{
		AccountEntity accountEntity = accountRepository.findById(userId);
		if(accountEntity == null)
		{
			throw new AuthException(ErrorCode.NO_USER);
		}

		/*
		 * check password
		 */
		String salt = accountEntity.getSalt();
		MessageEncrypt messageEncrypt = new MessageEncrypt();
		String sha256Password = messageEncrypt.sha256Encrypt(md5Password + salt);
		if(!accountEntity.getPassword().equals(sha256Password))
		{
			throw new AuthException(ErrorCode.INVALID_PASSWORD);
		}

		/*
		 * check session (create session and set data)
		 */
		ObjectMapper objectMapper = new ObjectMapper();
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute("USER",  objectMapper.writeValueAsString(accountEntity));

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
