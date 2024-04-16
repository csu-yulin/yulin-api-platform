package csu.yulin.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import csu.yulin.enums.HttpMethodEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * HTTP 请求工具类，用于发送 HTTP 请求到指定的 API 地址。
 *
 * @author 刘飘
 */
@Component
public class HttpUtils {
    // API 的基础地址
    @Value("${base-url}")
    private String baseUrl;

    public boolean request(String path, HttpMethodEnum method, String requestExample) {
        // 分类处理请求方法
        if (method == HttpMethodEnum.GET) {
            // 拼接完整请求路径
            String url = baseUrl + requestExample;
            return sendGetRequest(url) == 200;
        } else {
            // 拼接完整请求路径
            String url = baseUrl + path;
            return sendPostRequest(url, requestExample) == 200;
        }
    }

    private int sendGetRequest(String requestExample) {
        try {
            return HttpRequest.get(requestExample)
                    .execute().getStatus();
        } catch (HttpException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    private int sendPostRequest(String url, String requestExample) {
        try {
            return HttpRequest.post(url)
                    .contentType("application/json;charset=UTF-8") // 设置Content-Type为JSON格式
                    .charset(CharsetUtil.CHARSET_UTF_8) // 设置字符编码为UTF-8
                    .body(requestExample)
                    .execute().getStatus();
        } catch (HttpException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }
}
