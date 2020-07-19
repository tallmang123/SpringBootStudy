package com.tallmang.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

public class RedisSessionManager {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.session.expire-time}")
    int timeout;

    public String createSession(String sessionKey, Map<String,Object> sessionSaveData) throws Exception{
        // 1. delete session if it is exists
        if(redisTemplate.opsForValue().get(sessionKey) != null)
        {
            redisTemplate.delete(sessionKey);
        }

        // 2. create new session
        redisTemplate.opsForValue().set(sessionKey, Json.encodeJsonString(sessionSaveData), this.timeout);

        return sessionKey;
    }

    public Map<String, Object> getSession(String sessionKey) throws Exception{

        if(sessionKey.isEmpty())
        {
            throw new AuthException(ErrorCode.INVALID_SESSIONKEY);
        }

        Object sessionJsonData = redisTemplate.opsForValue().get(sessionKey);
        if(sessionJsonData == null)
        {
            throw new AuthException(ErrorCode.INVALID_SESSION);
        }
        return Json.decodeJsonString(sessionJsonData.toString());
    }

    public Object getData(String sessionKey, String dataKey) throws Exception{
        Map<String,Object> sessionData = this.getSession(sessionKey);
        return sessionData.get(dataKey);
    }

    public void saveData(String sessionKey, String key , String value) throws Exception{
        Map<String,Object> sessionData = this.getSession(sessionKey);
        sessionData.put(key,value);
    }

    public void saveDataList(String sessionKey, Map<String,Object> dataList) throws Exception{
        Map<String,Object> sessionData = this.getSession(sessionKey);
        dataList.forEach(sessionData::put);
    }
}
