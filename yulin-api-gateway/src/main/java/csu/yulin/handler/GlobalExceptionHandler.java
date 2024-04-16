package csu.yulin.handler;

import cn.hutool.json.JSONUtil;
import csu.yulin.common.ResultResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author 刘飘
 */
@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    @NotNull
    @Override
    public Mono<Void> handle(@NotNull ServerWebExchange exchange, @NotNull Throwable ex) {

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String message = ex.getMessage();

        // 返回错误信息给客户端
        return response.writeWith(Mono.just(
                response.bufferFactory().wrap(JSONUtil.toJsonStr(ResultResponse.failure(message)).getBytes())));

    }
}
