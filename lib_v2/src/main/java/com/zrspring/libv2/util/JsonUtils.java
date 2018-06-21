package com.zrspring.libv2.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.json.JSONObject;

/**
 * Created by shopping on 2017/12/22 10:01.
 * https://github.com/wheroj
 */

public class JsonUtils {

    private static ObjectMapper mMapper = new ObjectMapper();

    static {
        mMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        mMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        mMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mMapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
        mMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mMapper.enableDefaultTyping();
    }

    /**
     * json转化为普通对象
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T json2CommonObj(String json, Class<T> clazz){
        try {
            T object = mMapper.readValue(json, clazz);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * json转化为集合
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T json2Obj (String json, TypeReference<T> clazz){
        try {
            T t = mMapper.readValue(json, clazz);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将对象转化成字符串
     * @param obj
     * @return
     */
    public static String obj2String (Object obj){
        try {
            return mMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断字符串是否JSON数组
     * @param str
     * @return
     */
    public static boolean isJsonArray(String str) {
        if(str != null && str.trim().length() > 1) {
            String temp = str.trim();
            if(temp.startsWith("[") && temp.endsWith("]")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串是否JSON对象
     * @param str
     * @return
     */
    public static boolean isJsonObj(String str) {
        if(str != null && str.trim().length() > 1) {
            String temp = str.trim();
            if(temp.startsWith("{") && temp.endsWith("}")) {
                return true;
            }
        }
        return false;
    }

    public static String getJsonStringObjInKeyValue(String jsonStr, String key) {
        if(isJsonObj(jsonStr)) {
            try {
                JSONObject jo = new JSONObject(jsonStr);
                return jo.getString(key);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static int getIntObjInKeyValue(String jsonStr, String key) {
        if(isJsonObj(jsonStr)) {
            try {
                JSONObject jo = new JSONObject(jsonStr);
                return jo.getInt(key);
            } catch (Exception e) {
            }
        }
        return -1;
    }
}
