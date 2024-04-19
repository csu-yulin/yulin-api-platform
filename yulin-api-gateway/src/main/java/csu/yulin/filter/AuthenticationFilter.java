package csu.yulin.filter;

import csu.yulin.model.entity.UserApiRelation;
import csu.yulin.service.ApiInfoService;
import csu.yulin.service.ApiRequestLogService;
import csu.yulin.service.UserApiRelationService;
import csu.yulin.service.UserService;
import csu.yulin.utils.RequestUtil;
import csu.yulin.utils.SignUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author 刘飘
 */
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<Object> {
    @DubboReference
    private UserService userService;

    @DubboReference
    private ApiInfoService apiInfoService;

    @DubboReference
    private ApiRequestLogService apiRequestLogService;

    @DubboReference
    private UserApiRelationService userApiRelationService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            // 进行鉴权操作，例如检查请求头中的某些信息是否符合预期
            checkAuthentication(exchange);
            // 加一个请求头
            ServerHttpRequest request = exchange.getRequest().mutate().header("Source",
                    "GateWay").build();
            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    private void checkAuthentication(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();

        // 获取请求头中的 X-Ca-Key
        String accessKey = headers.getFirst("X-Ca-Key");
        // 获取请求头中的 X-Ca-Timestamp
        String timestamp = headers.getFirst("X-Ca-Timestamp");
        // 获取请求头中的 X-Ca-Nonce
        String nonce = headers.getFirst("X-Ca-Nonce");
        // 获取请求头中的 X-Ca-Signature-Method
        String signMethod = headers.getFirst("X-Ca-Signature-Method");
        // 获取请求头中的 X-Ca-Signature
        String originalSignature = headers.getFirst("X-Ca-Signature");

        // 如果请求头中的某些信息为空，则返回 false
        if (StringUtils.isAllBlank(accessKey, timestamp, nonce, signMethod, originalSignature)) {
            throw new RuntimeException("请求头信息不完整!");
        }

        // 检查本次请求的时效性
        long currentTimestamp = System.currentTimeMillis();
        long requestTimestamp = Long.parseLong(timestamp);
        // 5 minutes window
        long timestampWindow = 5 * 60 * 1000;
        if (Math.abs(currentTimestamp - requestTimestamp) > timestampWindow) {
            throw new RuntimeException("请求已过期!");
        }

        // 检查本次请求的唯一性
        Boolean result = redisTemplate.opsForValue().setIfAbsent(nonce, "1", 5, TimeUnit.MINUTES);
        if (Boolean.FALSE.equals(result)) {
            throw new RuntimeException("请求已被处理!");
        }

        // 拼接请求 URL
        String uri = request.getURI().toString();
        String url = uri.contains("?") ? uri.substring(0, uri.indexOf("?")) : uri;
        // 获取用户的密钥
        String secretKey = userService.getUserSecretKeyByAccessKey(accessKey);
        // 判断接口是否存在
        boolean apiExist = apiInfoService.isApiExist(request.getPath().value());
        if (!apiExist) {
            throw new RuntimeException("接口不存在!");
        }
        // 判断用户是否购买了此接口以及是否还有调用次数
        // 获取用户id
        Long userId = userService.getUserIdByAccessKey(accessKey);
        // 获取api Id
        RequestPath path = request.getPath();
        Long apiId = apiInfoService.getApiIdByPath(request.getPath().value());
        // 异常会在调用时抛出,不用判空
        UserApiRelation userApiRelation = userApiRelationService.getByUserIdAndApiId(userId, apiId);
        boolean couldInvoke = userApiRelationService.isCouldInvoke(userApiRelation.getId());
        if (!couldInvoke) {
            throw new RuntimeException("用户调用次数已用完!");
        }

        Map<String, String> params = request.getQueryParams().toSingleValueMap();
        // 读取请求体
        String body = RequestUtil.resolveBodyFromRequest(exchange.getRequest());
        body = body.isBlank() ? null : body;

        // 生成安全签名
        String signature = null;
        try {
            signature = SignUtils.generateSignature(url, accessKey, secretKey, timestamp, nonce, params, signMethod, body);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        if (!Objects.equals(originalSignature, signature)) {
            throw new RuntimeException("签名错误!");
        }
    }
}

