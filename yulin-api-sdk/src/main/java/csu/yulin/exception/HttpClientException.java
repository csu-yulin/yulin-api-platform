package csu.yulin.exception;

/**
 * 表示 HTTP 客户端操作中的异常情况。
 * 
 * @author 刘飘
 */
public class HttpClientException extends RuntimeException {

    /**
     * 构造一个 HttpClientException 异常实例。
     * 
     * @param message 异常的详细信息
     */
    public HttpClientException(String message) {
        super(message);
    }

    /**
     * 构造一个 HttpClientException 异常实例。
     * 
     * @param cause 异常的原因
     */
    public HttpClientException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造一个 HttpClientException 异常实例。
     * 
     * @param message 异常的详细信息
     * @param cause 异常的原因
     */
    public HttpClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
