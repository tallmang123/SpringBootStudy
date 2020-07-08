package com.tallmang.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallmang.common.AuthException;
import com.tallmang.common.ErrorCode;
import com.tallmang.common.encrypt.AES256;
import com.tallmang.common.encrypt.SHA256;
import org.h2.engine.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tallmang.entity.AccountEntity;
import com.tallmang.mybatis.mapper.AccountMapper;
import com.tallmang.mybatis.vo.AccountVO;
import com.tallmang.repository.AccountRepository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@Transactional
public class AccountService {

	@Autowired
	SessionService sessionService;
	@Autowired
	AccountMapper accountMapper;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	RedisTemplate<String, Object> redisTemplate;
	
	public Map<String, Object> manualLoginProcess(String userId, String md5Password, boolean isAutoLogin) throws Exception
	{
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		HttpServletResponse response = attr.getResponse();
		//1. Id check
		AccountEntity accountEntity = accountRepository.findById(userId);
		if(accountEntity == null)
		{
			throw new AuthException(ErrorCode.NO_USER);
		}

		//2. password check
		String salt = accountEntity.getSalt();
		SHA256 sha256 = SHA256.getInstance();
		String sha256Password = SHA256.encrypt(md5Password + salt);
		if(!accountEntity.getPassword().equals(sha256Password))
		{
			throw new AuthException(ErrorCode.INVALID_PASSWORD);
		}

		//3.autoLogin check
		int userDataExpire = 0;
		System.out.println(isAutoLogin);
		if(isAutoLogin)
		{
			userDataExpire = 60*60*24; // 1day
		}
		else
		{
			userDataExpire = 60*60; // 1hour
		}

		int userSeq = accountEntity.getSeq();

		//4. save user key on cookie
		String userKey = AES256.encode(Integer.toString(userSeq));
		Cookie myCookie = new Cookie("userKey", userKey);
		myCookie.setMaxAge(userDataExpire);
		myCookie.setPath("/");

		//5. create redis session
		String redisSessionKey = sessionService.createSession(accountEntity.getId(), accountEntity.getPassword(), accountEntity.getSeq());

		//6. save login status and redisSessionKey
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute("redisSessionKey",redisSessionKey);
		httpSession.setAttribute("isLogin",true);

		Map<String, Object> result = new HashMap<>();
		result.put("code",ErrorCode.SUCCESS.getCode());
		result.put("message",ErrorCode.SUCCESS.getMessage());

		return result;
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

