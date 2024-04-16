package csu.yulin.http;


import java.net.ConnectException;
import java.util.Map;

/**
 * http客户端接口
 *
 * @author 刘飘
 */
public interface HttpClient {
    /**
     * 向指定 URL 发送 GET 请求，并返回响应内容。
     *
     * @param url        请求的 URL 地址
     * @param parameters 请求参数，键为参数名，值为参数值的列表
     * @param headers    请求头信息，键为头字段名，值为头字段值的列表
     * @return 返回请求响应的内容
     */
    String get(String url, Map<String, String> parameters, Map<String, String> headers) throws ConnectException;

    /**
     * 向指定 URL 发送 POST 请求，并返回响应内容。
     *
     * @param url     请求的 URL 地址
     * @param data    POST 请求的数据，以字节数组形式表示
     * @param headers 请求头信息，键为头字段名，值为头字段值的列表
     * @return 返回请求响应的内容
     */
    String post(String url, byte[] data, Map<String, String> headers) throws ConnectException;
}
