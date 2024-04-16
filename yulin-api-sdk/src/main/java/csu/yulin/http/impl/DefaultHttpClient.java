package csu.yulin.http.impl;

import csu.yulin.exception.HttpClientException;
import csu.yulin.exception.HttpStatusClientException;
import csu.yulin.http.HttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 默认的 HTTP 客户端实现，基于 HttpURLConnection
 *
 * @author 刘飘
 */
public class DefaultHttpClient implements HttpClient {
    private static HttpURLConnection getHttpURLConnection(Map<String, String> headers, StringBuilder urlBuilder) throws IOException {
        URL targetUrl = new URL(urlBuilder.toString());
        HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
        connection.setRequestMethod("GET");

        // 设置请求头
        return getHttpURLConnection(headers, connection);
    }

    private static HttpURLConnection getHttpURLConnection(Map<String, String> headers, HttpURLConnection connection) {
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        return connection;
    }

    private static HttpURLConnection getHttpURLConnection(Map<String, String> headers, URL targetUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        // 设置请求头
        return getHttpURLConnection(headers, connection);
    }

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
        try {
            // 构建带参数的 URL
            StringBuilder urlBuilder = new StringBuilder(url);
            if (parameters != null && !parameters.isEmpty()) {
                urlBuilder.append("?");
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    urlBuilder.append(URLEncoder.encode(key, StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode(value, StandardCharsets.UTF_8)).append("&");
                }
                // 删除末尾多余的 "&"
                urlBuilder.deleteCharAt(urlBuilder.length() - 1);
            }

            // 创建 URL 对象
            HttpURLConnection connection = getHttpURLConnection(headers, urlBuilder);

            // 读取响应数据
            return getString(connection);
        } catch (Exception e) {
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
        try {
            URL targetUrl = new URL(url);
            HttpURLConnection connection = getHttpURLConnection(headers, targetUrl);

            // 发送请求数据
            OutputStream out = connection.getOutputStream();
            out.write(data);
            out.close();

            // 读取响应数据
            return getString(connection);
        } catch (IOException e) {
            throw new HttpClientException(e);
        }
    }

    private String getString(HttpURLConnection connection) throws IOException {
        InputStream in = connection.getInputStream();
        ByteArrayOutputStream responseBody = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            responseBody.write(ch);
        }
        in.close();

        String response = responseBody.toString(StandardCharsets.UTF_8);

        int statusCode = connection.getResponseCode();
        boolean is2xxSuccessful = statusCode >= 200 && statusCode <= 299;
        boolean is3xxSuccessful = statusCode >= 300 && statusCode <= 399;
        if (!(is2xxSuccessful || is3xxSuccessful)) {
            throw new HttpStatusClientException(statusCode, connection.getResponseMessage(), response);
        }

        return response;
    }
}
