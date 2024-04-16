package csu.yulin.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 响应结果封装类，用于包装API接口的响应数据。
 *
 * @param <T> 响应数据的类型
 * @author 刘飘
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
     * 创建一个失败的响应对象，根据指定的错误信息。
     *
     * @param message 错误信息
     * @return 失败的响应对象
     */
    public static ResultResponse<Object> failure(String message) {
        return new ResultResponse<>("400", message);
    }
}
