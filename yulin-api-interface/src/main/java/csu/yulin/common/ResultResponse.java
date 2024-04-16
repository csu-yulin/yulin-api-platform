package csu.yulin.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 响应结果封装类，用于包装API接口的响应数据。
 *
 * @author 刘飘
 * @param <T> 响应数据的类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultResponse<T> implements Serializable {
    /**
     * 响应代码
     */
    private String code;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 响应结果
     */
    private T data;

    public static <T> ResultResponse<T> success() {
        return new ResultResponse<>("200","Success",null);
    }

    public static <T> ResultResponse<T> success(T t) {
        return new ResultResponse<>("200","Success",t);
    }

    public static <T> ResultResponse<T> success(String message,T t) {
        return new ResultResponse<>("200",message,t);
    }


    public static <T>  ResultResponse<T> failure(String code,String message,T t) {
        return new ResultResponse<>(code,message,t);
    }

    public static <T>  ResultResponse<T> failure(String code,String message) {
        return new ResultResponse<>(code,message,null);
    }
}
