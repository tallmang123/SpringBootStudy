package com.tallmang.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60)
public class RedisConfig {
	
	/**
	 * https://docs.spring.io/spring-session/docs/current/reference/html5/#httpsession
	 * EnableRedisHttpSession : make session data in redis
	 * When the session is expired and the data is deleted, the data is deleted and an event is generated when redis is searched using the actually issued session key.
	 *  1. spring:session:sessions:expires (string)
	 *   -> If you rely on the expiration time of sessionkey, data cannot be tracked after the expiration time, so it is managed as separate key-value. 
	 *	2. spring:session:expirations (set) 
	 *   -> * If there is no data request and no expire event occurs, the data remains even after the expiration point.
			Therefore, in order to remove this, keys are separately managed based on expirations and used to expire by requesting the values 
			​​in the value with redis every second.
	 *	3. spring:session:sessions (hash) -> session Data
	 *     * Note that the expiration that is set to five minutes after the session actually expires. 
	 *		This is necessary so that the value of the session can be accessed when the session expires. 
	 *		-> It is too risky so watch expire event and delete session immediately
	 */
	
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;
	
	/**
	 * RedisConnectionFactory Class
	 * {@link LettuceConnection} is faster than {@link JedisConnection}
	 * 1. Works asynchronous 
	 * 2. Use Connection Pool
	 * @return
	 */
	@Bean
	public RedisConnectionFactory redisConnectionFactory() 
	{
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisHost,redisPort);
		return lettuceConnectionFactory;
	}
	
	/**
	 * Setting Redis Template
	 * key : String , value : Json 
	 * @return
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate()
	{
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		
		return redisTemplate;
	}

}
