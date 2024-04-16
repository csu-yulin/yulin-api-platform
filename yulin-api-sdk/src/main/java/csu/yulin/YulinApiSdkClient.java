package csu.yulin;

import csu.yulin.client.AbstractRequest;
import csu.yulin.client.AbstractResponse;
import csu.yulin.client.SdkClient;
import csu.yulin.enums.HttpMethod;
import csu.yulin.http.HttpClient;
import csu.yulin.utils.DeserializationUtils;
import csu.yulin.utils.SignUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.ConnectException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 刘飘
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "yulin.api.sdk.client")
public class YulinApiSdkClient implements SdkClient {
    /**
     * 访问密钥
     */
    private String accessKey;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 请求地址 例: http://localhost:8080/api
     */
    private String endpoint;

    /**
     * 签名方法
     */
    private String signMethod;

    /**
     * http客户端
     */
    private HttpClient httpClient;

    public YulinApiSdkClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * 执行请求
     *
     * @param request 请求对象
     * @return 请求响应实体
     */
    @Override
    public <T extends AbstractResponse> T execute(AbstractRequest<T> request) throws ConnectException {
        String url = endpoint + request.getPath();
        // 获取请求参数
        Map<String, String> params = request.getApiParams();
        // 获取请求实体
        Object body = request.getBody();

        // 生成安全签名
        String timestamp = String.valueOf(System.currentTimeMillis());// 时间戳
        String nonce = UUID.randomUUID().toString();// 随机数
        String signature = null;
        try {
            signature = SignUtils.generateSignature(url, accessKey, secretKey, timestamp, nonce, params, signMethod, body);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        // 构建请求头
        Map<String, String> headers = buildHeaders(accessKey, timestamp, nonce, signature, signMethod);

        String method = request.getMethod();
        if (HttpMethod.GET == HttpMethod.fromValue(method)) {
            // 发送GET请求
            String response = httpClient.get(url, params, headers);
            return DeserializationUtils.deserialize(request, response);
        } else {
            // 发送POST请求
            byte[] byteArray = DeserializationUtils.objectToByteArray(request.getBody());
            String response = httpClient.post(url, byteArray, headers);
            return DeserializationUtils.deserialize(request, response);
        }
    }

    private Map<String, String> buildHeaders(String accessKey, String timestamp, String nonce, String signature, String signMethod) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Ca-Key", accessKey);
        headers.put("X-Ca-Timestamp", timestamp);
        headers.put("X-Ca-Nonce", nonce);
        headers.put("X-Ca-Signature-Method", signMethod);
        headers.put("X-Ca-Signature", signature);
        return headers;
    }
}
