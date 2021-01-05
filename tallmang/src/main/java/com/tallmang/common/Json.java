package com.tallmang.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class Json {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String encodeJsonString(Object object) throws Exception
    {
        return objectMapper.writeValueAsString(object);
    }

    public static Map<String,Object> decodeJsonString(String jsonString) throws Exception
    {
        return objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>(){});
    }
}
