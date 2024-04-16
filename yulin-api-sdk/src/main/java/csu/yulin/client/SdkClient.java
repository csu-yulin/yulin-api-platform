package csu.yulin.client;

import java.net.ConnectException;

/**
 * @author 刘飘
 */
public interface SdkClient {

    /**
     * 执行请求
     *
     * @param request 请求对象
     * @return 请求响应实体
     */
    <T extends AbstractResponse> T execute(AbstractRequest<T> request) throws ConnectException;
}