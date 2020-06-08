package com.tallmang.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class Json {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String encodeJsonString(Map<String,Object> map ) throws Exception
    {
        return objectMapper.writeValueAsString(map);
    }

    public static Map<String,String> decodeJsonString(String jsonString) throws Exception
    {
        return objectMapper.readValue(jsonString, new TypeReference<Map<String, String>>(){});
    }
}
