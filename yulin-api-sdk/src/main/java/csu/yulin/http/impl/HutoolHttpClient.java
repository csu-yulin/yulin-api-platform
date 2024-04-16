package csu.yulin.http.impl;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpRequest;
import csu.yulin.http.HttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘飘
 */
public class HutoolHttpClient implements HttpClient {
    /**
     * 向指定 URL 发送 GET 请求，并返回响应内容。
     *
     * @param url        请求的 URL 地址
     * @param parameters 请求参数，键为参数名，值为参数值的列表
     * @param headers    请求头信息，键为头字段名，值为头字段值的列表
     * @return 返回请求响应的内容
     */
    @Override
    public String get(String url, Map<String, String> parameters, Map<String, String> headers) {
        Map<String, Object> params = new HashMap<>();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue(); // 将String转换为Object类型
            params.put(key, value);
        }
        return HttpRequest.get(url)
                .form(params)
                .addHeaders(headers)
                .execute().body();
    }

    /**
     * 向指定 URL 发送 POST 请求，并返回响应内容。
     *
     * @param url     请求的 URL 地址
     * @param data    POST 请求的数据，以字节数组形式表示
     * @param headers 请求头信息，键为头字段名，值为头字段值的列表
     * @return 返回请求响应的内容
     */
    @Override
    public String post(String url, byte[] data, Map<String, String> headers) {
        return HttpRequest.post(url)
                .addHeaders(headers)
                .contentType("application/json;charset=UTF-8") // 设置Content-Type为JSON格式
                .charset(CharsetUtil.CHARSET_UTF_8) // 设置字符编码为UTF-8
                .body(data)
                .execute().body();
    }
}
