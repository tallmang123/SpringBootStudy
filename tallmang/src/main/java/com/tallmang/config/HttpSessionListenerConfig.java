package com.tallmang.config;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@WebListener
@Configuration
public class HttpSessionListenerConfig implements HttpSessionListener{

	/**
	 * Redis session event listener
	 */
	@Autowired
	RedisTemplate<String, Object> redisTemplate;
	
    @Value("${spring.session.redis.namespace}")
    private String sessionNameSpace;
	
    public void sessionCreated(HttpSessionEvent se) {
    	System.out.println("http session created : " + se.getSession().getId());
    }
    
    /**
     * spring:session:sessions (hash) 
     *  -> the expiration that is set to five minutes after the session actually expires.
     *  -> so it needs to delete session data immediately when DetroyEvent is occurred 
     */
    public void sessionDestroyed(HttpSessionEvent se) {
    	String redisSessionKey = sessionNameSpace + ":sessions:" + se.getSession().getId();
    	redisTemplate.delete(redisSessionKey);
    	
    	System.out.println(redisSessionKey);
    	System.out.println("http session destroyed : " + se.getSession().getId());
    	
    }

}
