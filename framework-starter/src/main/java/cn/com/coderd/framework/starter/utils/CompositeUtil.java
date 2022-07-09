package cn.com.coderd.framework.starter.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 通用工具类
 */
@UtilityClass
public class CompositeUtil {
    private static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .build();

    /**
     * json格式化
     *
     * @param obj
     * @return
     */
    @SneakyThrows
    public static String json(Object obj) {
        return OBJECT_MAPPER.writeValueAsString(obj);
    }

    /**
     * 从json实例化
     *
     * @param src
     * @param tClass
     * @param <T>
     * @return
     */
    @SneakyThrows
    public static <T> T fromJson(String src, Class<T> tClass) {
        return OBJECT_MAPPER.readValue(src, tClass);
    }

    /**
     * 实例化json node
     *
     * @param src
     * @return
     */
    @SneakyThrows
    public static JsonNode fromJson(String src) {
        return OBJECT_MAPPER.readTree(src);
    }

    /**
     * 格式化headers
     *
     * @param request HttpServletRequest
     */
    @SneakyThrows
    public static String formatHeader(HttpServletRequest request) {
        Map<String, List<Object>> map = new HashMap<>();
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String headerName = names.nextElement();
            List<Object> list = new ArrayList<>();
            Enumeration<String> values = request.getHeaders(headerName);
            while (values.hasMoreElements()) {
                list.add(values.nextElement());
            }
            map.put(headerName, list);
        }
        return OBJECT_MAPPER.writeValueAsString(map);
    }

    /**
     * 格式化header
     *
     * @param request
     * @return
     */
    @SneakyThrows
    public static String formatHeader(HttpRequest request) {
        return OBJECT_MAPPER.writeValueAsString(request.getHeaders());
    }

    /**
     * 获取实例的私有字段
     *
     * @param clazz
     * @param field
     * @param target
     * @return
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T> T getPrivateField(Class<?> clazz, String field, Object target) {
        Field declaredField = clazz.getDeclaredField(field);
        try {
            return (T) declaredField.get(target);
        } catch (IllegalAccessException e) {
            declaredField.setAccessible(true);
            return (T) declaredField.get(target);
        }
    }

    /**
     * 合并字节数组
     *
     * @param bts1
     * @param bts2
     * @return
     */
    public static byte[] merge(byte[] bts1, byte[] bts2) {
        if (bts1 == null || bts1.length == 0) {
            return bts2;
        }
        if (bts2 == null || bts2.length == 0) {
            return bts1;
        }
        byte[] bts = new byte[bts1.length + bts2.length];
        System.arraycopy(bts1, 0, bts, 0, bts1.length);
        System.arraycopy(bts2, 0, bts, bts1.length, bts2.length);
        return bts;
    }
}
