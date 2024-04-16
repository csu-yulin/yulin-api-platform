package csu.yulin.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author 刘飘
 */
@Component
@Slf4j
public class PrintUrlFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获得路由
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        String uri = route.getUri().toString();

        ServerHttpRequest request = exchange.getRequest();
        // 请求路径中域名之后的部分,本例中是/api/name/get
        String path = request.getPath().toString();

        // 转发后的完整地址,本例中是http://127.0.0.1:8001/api/name/get
        String address = uri + path;

        log.info("Real Request URL: {}", address);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
