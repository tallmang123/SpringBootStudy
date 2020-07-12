package com.tallmang.service;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tallmang.common.AuthException;
import com.tallmang.common.CookieManager;
import com.tallmang.common.ErrorCode;
import com.tallmang.common.RedisSessionManager;
import com.tallmang.common.encrypt.AES256;
import com.tallmang.common.encrypt.SHA256;
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
	RedisSessionManager redisSessionManager;

	@Autowired
	CookieManager cookieManager;

	@Autowired
	AccountMapper accountMapper;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	public Map<String, Object> manualLoginProcess(HttpServletRequest request, HttpServletResponse response, String userId, String md5Password, boolean isAutoLogin) throws Exception
	{
		//1. Id check
		AccountEntity accountEntity = accountRepository.findById(userId);
		if(accountEntity == null)
		{
			throw new AuthException(ErrorCode.NO_USER);
		}

		//2. password check
		String salt = accountEntity.getSalt();
		String sha256Password = SHA256.encrypt(md5Password + salt);
		if(!accountEntity.getPassword().equals(sha256Password))
		{
			throw new AuthException(ErrorCode.INVALID_PASSWORD);
		}

		//3.autoLogin check
		//auto login true : 30days
		//auto login false : 1days
		int dataExpire = isAutoLogin ? 60*60*24*30 : 60*60;
		int userSeq = accountEntity.getSeq();

		//4. save user key on cookie
		String userTSeq = AES256.encode(Integer.toString(userSeq));
		cookieManager.setData("TSEQ", userTSeq, dataExpire);

		//5. create redis session
		String sessionKey = Base64.getEncoder().encodeToString(AES256.encode(accountEntity.getId() + "*****" +System.currentTimeMillis()).getBytes());
		String token = SHA256.encrypt("tallmang_auth_token" + System.currentTimeMillis());
		Map<String,Object> sessionSaveData = new HashMap<>();
		sessionSaveData.put("id",accountEntity.getId());
		sessionSaveData.put("userSeq",accountEntity.getSeq());
		sessionSaveData.put("token",token);
		String redisSessionKey = redisSessionManager.createSession(sessionKey,sessionSaveData);

		// 6. save session key on cookie
		cookieManager.setData("TSID", redisSessionKey, dataExpire);

		//6. save login status and redisSessionKey
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute("TSID",redisSessionKey);
		httpSession.setAttribute("isLogin",true);

		Map<String, Object> result = new HashMap<>();
		result.put("code",ErrorCode.SUCCESS.getCode());
		result.put("message",ErrorCode.SUCCESS.getMessage());

		return result;
	}

	public Map<String, Object> autoLoginProcess() throws Exception
	{
		/*ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();

		Cookie[] getCookie = request.getCookies();
		String userTSeq = "";
		for (Cookie cookie : getCookie) {
			if (cookie.getName().equals("TSEQ")) {
				userTSeq = cookie.getValue();
			}
		}

		String userSeq = AES256.decode(userTSeq);*/


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

