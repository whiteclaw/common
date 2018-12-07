package com.whiteclaw.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 使用jackson序列化和反序列化json工具类
 *
 * @author whiteclaw
 * created on 2018-12-07
 */
public class JacksonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_DEFAULT);
    }

    /**
     * JavaObject序列化为json字符串
     *
     * @param obj java对象
     * @return 序列化后的字符串
     */
    public static <T> String obj2Json(T obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * json字符串反序列化为JavaObject
     *
     * @param json  json字符串
     * @param clazz 目标类型
     * @return T类型的JavaObject
     */
    public static <T> T json2Obj(String json, Class<T> clazz) {
        if (String.class.equals(clazz)) {
            throw new IllegalArgumentException("Class<T>不能为String");
        }
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * json字符串反序列化为JavaObject
     *
     * @param json          json字符串
     * @param typeReference 类型反射器
     * @return T类型的JavaObject
     */
    public static <T> T json2Obj(String json, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(json) || typeReference == null) {
            return null;
        }
        if (String.class.equals(typeReference.getType())) {
            throw new IllegalArgumentException("Class<T>不能为String");
        }
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            return null;
        }
    }

}
