package csu.yulin.exception;

import lombok.Getter;

/**
 * 表示 HTTP 请求返回非成功状态码的异常。
 *
 * @author 刘飘
 */
@Getter
public class HttpStatusClientException extends RuntimeException {

    private final int statusCode; // 获取 HTTP 响应的状态码

    private final String statusMessage; // 获取 HTTP 响应的状态消息

    private final String responseBody; // 获取 HTTP 响应的响应体

    /**
     * 构造一个 HttpStatusClientException 异常实例。
     *
     * @param statusCode HTTP 响应的状态码
     * @param statusMessage HTTP 响应的状态消息
     * @param responseBody HTTP 响应的响应体
     */
    public HttpStatusClientException(int statusCode, String statusMessage, String responseBody) {
        super("HTTP status code: " + statusCode + ", status message: " + statusMessage);
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.responseBody = responseBody;
    }
}
