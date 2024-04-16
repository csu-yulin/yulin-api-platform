package csu.yulin.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * 全局过滤器，用于缓存请求体，以便可以多次读取
 *
 * @author 刘飘
 */
@Component
public class CacheBodyGlobalFilter implements Ordered, GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpMethod method = exchange.getRequest().getMethod();
        if (!method.matches("POST")) {
            return chain.filter(exchange);
        } else {
            // 缓存请求体并修改请求对象，以便请求体可以多次读取
            return DataBufferUtils.join(exchange.getRequest().getBody())
                    .flatMap(dataBuffer -> {
                        // 保持数据缓冲区的引用，防止被释放
                        DataBufferUtils.retain(dataBuffer);
                        // 创建一个包含缓存数据的 Flux
                        Flux<DataBuffer> cachedFlux = Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
                        // 创建一个经过修改的请求对象，用于传递给下一个过滤器链
                        ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(
                                exchange.getRequest()) {
                            @Override
                            public Flux<DataBuffer> getBody() {
                                // 返回包含缓存数据的 Flux
                                return cachedFlux;
                            }
                        };
                        // 将修改后的请求对象传递给下一个过滤器链
                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    });
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
