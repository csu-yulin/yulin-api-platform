package csu.yulin.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 响应结果封装类，用于包装API接口的响应数据。
 *
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

    /**
     * 构造函数，根据错误信息构造响应对象。
     *
     * @param errorInfo 错误信息接口实例
     */
    public ResultResponse(BaseResultInterface errorInfo) {
        this.code = errorInfo.getResultCode();
        this.message = errorInfo.getResultMsg();
    }

    /**
     * 创建一个成功的响应对象，不包含响应数据。
     *
     * @return 成功的响应对象
     */
    public static ResultResponse<Object> success() {
        return new ResultResponse<>(ResultCode.SUCCESS);
    }

    /**
     * 创建一个成功的响应对象，包含指定的响应数据。
     *
     * @param t   响应数据
     * @param <T> 响应数据的类型
     * @return 成功的响应对象
     */
    public static <T> ResultResponse<T> success(T t) {
        ResultResponse<T> resultResponse = new ResultResponse<>(ResultCode.SUCCESS);
        resultResponse.setData(t);
        return resultResponse;
    }

    /**
     * 创建一个失败的响应对象，根据指定的错误信息。
     *
     * @param resultCode 错误信息接口实例
     * @return 失败的响应对象
     */
    public static ResultResponse<Object> failure(BaseResultInterface resultCode) {
        return new ResultResponse<>(resultCode);
    }

    /**
     * 创建一个失败的响应对象，根据指定的错误信息和响应数据。
     *
     * @param resultCode 错误信息接口实例
     * @param t          响应数据
     * @param <T>        响应数据的类型
     * @return 失败的响应对象
     */
    public static <T> ResultResponse<T> failure(BaseResultInterface resultCode, T t) {
        ResultResponse<T> resultResponse = new ResultResponse<>(resultCode);
        resultResponse.setData(t);
        return resultResponse;
    }
}
