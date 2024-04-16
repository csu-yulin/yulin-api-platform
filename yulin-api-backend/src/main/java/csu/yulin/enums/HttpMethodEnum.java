package csu.yulin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * HTTP 方法枚举
 *
 * @author 刘飘
 */
@AllArgsConstructor
@Getter
public enum HttpMethodEnum {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    PATCH("PATCH"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS");

    private final String method;

    public static HttpMethodEnum fromValue(String value) {
        for (HttpMethodEnum method : HttpMethodEnum.values()) {
            if (method.getMethod().equals(value)) {
                return method;
            }
        }
        return null;
    }

    // 判断方法字符串是否存在于枚举中的方法
    public static boolean containsMethod(String method) {
        for (HttpMethodEnum methodEnum : HttpMethodEnum.values()) {
            if (methodEnum.getMethod().equals(method)) {
                return true;
            }
        }
        return false;
    }
}
