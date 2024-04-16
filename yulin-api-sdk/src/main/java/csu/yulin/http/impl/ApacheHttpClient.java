package csu.yulin.http.impl;


import csu.yulin.exception.HttpClientException;
import csu.yulin.handler.ResponseHandler;
import csu.yulin.http.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;

import java.io.IOException;
import java.util.Map;

/**
 * @author 刘飘
 */
public class ApacheHttpClient implements HttpClient {

    private final CloseableHttpClient client;

    public ApacheHttpClient() {
        this.client = HttpClients.createDefault();
    }

    /**
     * 向指定 URL 发送 GET 请求，并返回响应内容。
     *
     * @param url        请求的 URL 地址
     * @param parameters 请求参数，键为参数名，值为参数值的列表（在GET请求中，参数通常直接附在URL中）
     * @param headers    请求头信息，键为头字段名，值为头字段值的列表
     * @return 返回请求响应的内容
     * @throws HttpClientException 如果发送请求或处理响应时发生错误，则抛出 HttpClientException
     */
    @Override
    public String get(String url, Map<String, String> parameters, Map<String, String> headers) {
        // 构建带参数的 URL
        StringBuilder urlBuilder = new StringBuilder(url);
        if (parameters != null && !parameters.isEmpty()) {
            urlBuilder.append("?");
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                urlBuilder.append(key).append("=").append(value).append("&");
            }
            // 删除末尾多余的 "&"
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }

        // 创建 GET 请求对象
        HttpGet httpGet = new HttpGet(urlBuilder.toString());

        // 设置请求头
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }

        try {
            return client.execute(httpGet, new ResponseHandler());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new ByteArrayEntity(data, ContentType.APPLICATION_JSON));

        // 设置请求头
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        }
        try {
            return client.execute(httpPost, new ResponseHandler());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


