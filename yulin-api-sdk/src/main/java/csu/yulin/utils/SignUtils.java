package csu.yulin.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @author 刘飘
 */
public class SignUtils {
    public static String generateSignature(String url, String accessKey, String secretKey, String timestamp, String nonce, Map<String, String> params, String signMethod, Object body) throws NoSuchAlgorithmException, InvalidKeyException {
        StringBuilder paramStr = new StringBuilder();
        paramStr.append(url);
        paramStr.append("ak=").append(accessKey).append("\n");
        paramStr.append("secretKey=").append(secretKey).append("\n");
        paramStr.append("timestamp=").append(timestamp).append("\n");
        paramStr.append("nonce=").append(nonce).append("\n");

        if (Objects.nonNull(body)) {
            String encode = Base64.encode(DigestUtil.md5(JSONUtil.toJsonStr(body)));
            paramStr.append("body=").append(encode).append("\n");
        }

        if (params != null && !params.isEmpty()) {
            params = new TreeMap<>(params);
            params.forEach((key, value) -> {
                paramStr.append(key).append("=").append(value).append("\n");
            });
        }

        String strToSign = paramStr.substring(0, paramStr.lastIndexOf("\n"));

        Mac mac = Mac.getInstance(signMethod);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), signMethod);
        mac.init(secretKeySpec);
        byte[] bytes = mac.doFinal(strToSign.getBytes());

        return java.util.Base64.getEncoder().encodeToString(bytes);
    }
}


