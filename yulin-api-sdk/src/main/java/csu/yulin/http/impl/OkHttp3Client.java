package csu.yulin.http.impl;

import csu.yulin.exception.HttpClientException;
import csu.yulin.exception.HttpStatusClientException;
import csu.yulin.http.HttpClient;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author 刘飘
 */
public class OkHttp3Client implements HttpClient {

    private final OkHttpClient client;

    public OkHttp3Client() {
        this.client = new OkHttpClient();
    }

    /**
     * 向指定 URL 发送 GET 请求，并返回响应内容。
     *
     * @param url        请求的 URL 地址
     * @param parameters 请求参数，键为参数名，值为参数值的列表
     * @param headers    请求头信息，键为头字段名，值为头字段值的列表
     * @return 返回请求响应的内容
     * @throws HttpClientException 如果发送请求或处理响应时发生错误，则抛出 HttpClientException
     */
    @Override
    public String get(String url, Map<String, String> parameters, Map<String, String> headers) throws HttpClientException {
        try {
            // 构建带参数的 URL
            HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
            if (parameters != null && !parameters.isEmpty()) {
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    urlBuilder.addQueryParameter(key, value);
                }
            }

            // 构建 Request 对象
            Request.Builder requestBuilder = new Request.Builder().url(urlBuilder.build()).get();

            // 设置请求头
            Request request = getRequest(headers, requestBuilder);

            // 执行请求并获取响应
            try (Response response = client.newCall(request).execute()) {
                return getString(response);
            }
        } catch (IOException e) {
            throw new HttpClientException(e);
        }
    }

    @NotNull
    private String getString(Response response) throws IOException {
        String responseBody = response.body().string();

        int statusCode = response.code();
        boolean is2xxSuccessful = statusCode >= 200 && statusCode <= 299;
        boolean is3xxSuccessful = statusCode >= 300 && statusCode <= 399;
        if (!(is2xxSuccessful || is3xxSuccessful)) {
            throw new HttpStatusClientException(statusCode, response.message(), responseBody);
        }

        return responseBody;
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
        RequestBody requestBody = RequestBody.Companion.create(data, MediaType.parse("application/json"));

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(requestBody);

        // 设置请求头
        Request request = getRequest(headers, requestBuilder);

        try (Response response = client.newCall(request).execute()) {
            return getString(response);
        } catch (IOException e) {
            throw new HttpClientException(e);
        }
    }

    private Request getRequest(Map<String, String> headers, Request.Builder requestBuilder) {
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        return requestBuilder.build();
    }
}
