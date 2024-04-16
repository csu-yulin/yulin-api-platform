package csu.yulin.filter;

import cn.hutool.json.JSONUtil;
import com.alibaba.nacos.shaded.com.google.common.base.Joiner;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import csu.yulin.model.entity.ApiRequestLog;
import csu.yulin.service.ApiInfoService;
import csu.yulin.service.ApiRequestLogService;
import csu.yulin.service.UserApiRelationService;
import csu.yulin.service.UserService;
import csu.yulin.utils.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.*;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR;


/**
 * @author 刘飘
 */
@Component
@Slf4j
public class RequestLogFilter implements GlobalFilter, Ordered {

    private static Joiner joiner = Joiner.on("");

    @DubboReference
    private UserService userService;

    @DubboReference
    private ApiInfoService apiInfoService;

    @DubboReference
    private ApiRequestLogService apiRequestLogService;

    @DubboReference
    private UserApiRelationService userApiRelationService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 如果不是 API 请求，则直接放行给下一个过滤器
        if (!exchange.getRequest().getPath().toString().startsWith("/api")) {
            return chain.filter(exchange);
        }
        ServerHttpRequest request = exchange.getRequest();
        // 获取请求头中的 X-Ca-Key
        String accessKey = request.getHeaders().getFirst("X-Ca-Key");

        // 获取用户id
        Long userId = userService.getUserIdByAccessKey(accessKey);
        // 获取api Id
        RequestPath path = request.getPath();
        Long apiId = apiInfoService.getApiIdByPath(request.getPath().value());
        HttpMethod method = request.getMethod();

        // 请求参数
        String requestParams = null;
        if (method == HttpMethod.GET) {
            // 获取请求参数
            Map<String, String> params = request.getQueryParams().toSingleValueMap();
            requestParams = JSONUtil.toJsonStr(params);
        } else {
            // 获取请求体
            requestParams = RequestUtil.resolveBodyFromRequest(exchange.getRequest());
        }
        // 开始计时
        long start = System.currentTimeMillis();

        // 获取响应对象
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();
        String finalRequestParams = requestParams;
        ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(response) {
            @NotNull
            @Override
            public Mono<Void> writeWith(@NotNull Publisher<? extends DataBuffer> body) {
                if (Objects.equals(getStatusCode(), HttpStatus.OK) && body instanceof Flux) {
                    // 获取响应 ContentType
                    String responseContentType = exchange.getAttribute(ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
                    // 记录 JSON 格式数据的响应体
                    if (!StringUtils.isEmpty(responseContentType) && responseContentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        // 解决返回体分段传输
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            List<String> list = Lists.newArrayList();
                            dataBuffers.forEach(dataBuffer -> {
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);
                                list.add(new String(content, StandardCharsets.UTF_8));
                            });
                            String responseData = joiner.join(list);

                            // 保存请求日志
                            long end = System.currentTimeMillis();
                            // 获取经过的时间，单位为毫秒
                            long elapsedTimeInMillis = (end - start);
                            // 获取状态码
                            HttpStatusCode statusCode = getStatusCode();

                            ServerHttpRequest request = exchange.getRequest();
                            // 获取 IP 地址
                            String ipAddress = Objects.requireNonNull(request.getRemoteAddress()).getHostString();
                            // 获取用户代理
                            String userAgent = request.getHeaders().getFirst(HttpHeaders.USER_AGENT);
                            // 获取设备信息
                            String deviceInfo = parseDeviceInfo(exchange); // 编写相应的解析方法
                            // 获取请求来源
                            String requestSource = request.getHeaders().getFirst(HttpHeaders.REFERER);

                            // 保存请求日志
                            ApiRequestLog apiRequestLog = new ApiRequestLog();
                            apiRequestLog.setUserId(userId);
                            apiRequestLog.setApiId(apiId);
                            apiRequestLog.setRequestParams(finalRequestParams);
                            apiRequestLog.setResponseData(responseData);
                            apiRequestLog.setStatusCode(statusCode.value());
                            apiRequestLog.setResponseTime(elapsedTimeInMillis);
                            apiRequestLog.setRequestResult(statusCode.is2xxSuccessful() ? "Success" : "Fail");
                            apiRequestLog.setIpAddress(ipAddress);
                            apiRequestLog.setDeviceInfo(deviceInfo);
                            apiRequestLog.setRequestSource(requestSource == null ? "无" : requestSource);
                            apiRequestLog.setRemark("无");
                            apiRequestLogService.save(apiRequestLog);

                            // 用户已调用次数加一
                            Long id = userApiRelationService.getByUserIdAndApiId(userId, apiId).getId();
                            userApiRelationService.increaseCallCount(id);

                            // 打印本次请求的信息
                            log.info("<----------------------------- Request Log ----------------------------->");
                            log.info("当前时间: {}", LocalDateTime.now());
                            log.info("RESPONSE RESULT = [{}]", responseData.replaceAll("\n", "").replaceAll("\t", ""));
                            log.info("Response Time = {} milliseconds", elapsedTimeInMillis);
                            log.info("IP Address = {}", ipAddress);
                            log.info("User Agent = {}", userAgent);
                            log.info("Device Info = {}", deviceInfo);
                            log.info("Request Source = {}", requestSource == null ? "无" : requestSource);
                            log.info("<----------------------------- Request Log ----------------------------->");

                            return bufferFactory.wrap(responseData.getBytes());
                        }));
                    }
                }
                return super.writeWith(body);
            }
        };
        return chain.filter(exchange.mutate().response(responseDecorator).build());
    }

    @Override
    public int getOrder() {
        // 必须要小于 -1
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
    }

    public static String parseDeviceInfo(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();

        // 获取用户代理信息
        String userAgent = request.getHeaders().getFirst(HttpHeaders.USER_AGENT);

        // 解析用户代理信息以获取设备信息
        if (userAgent != null) {
            // 此处以简单的正则表达式示例来提取设备信息，实际应用中可能需要更复杂的逻辑
            Pattern pattern = Pattern.compile("\\(([^)]+)\\)");
            Matcher matcher = pattern.matcher(userAgent);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        return "Unknown";
    }
}
