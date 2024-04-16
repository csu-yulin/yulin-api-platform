package csu.yulin.client;

import java.util.Map;

/**
 * @author 刘飘
 */
public abstract class AbstractRequest<T extends AbstractResponse> {

    /**
     * 接口请求方法
     * @return 请求方法
     */
    public abstract String getMethod();

    /**
     * 接口请求路径
     * @return 请求路径
     */
    public abstract String getPath();


    /**
     * 接口请求参数
     * @return 请求参数
     */
    public abstract Map<String, String> getApiParams();

    /**
     * 请求实体
     * @return 请求实体
     */
    public abstract Object getBody();

    /**
     * 请求响应实体
     * @return 请求响应实体的Class对象
     */
    public abstract Class<T> getResponseClass();
}