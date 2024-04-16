package csu.yulin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 刘飘
 */

@Getter
@AllArgsConstructor
public enum HttpMethod {
    GET("GET"),
    POST("POST");
    private final String value;

    public static HttpMethod fromValue(String value) {
        for (HttpMethod method : HttpMethod.values()) {
            if (method.value.equals(value)) {
                return method;
            }
        }
        return null;
    }
}
