package csu.yulin.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author 刘飘
 */
@Configuration
public class GatewayConfiguration {
    @Bean
    public KeyResolver apiKeyResolver() {
        // 使用请求中的 API 密钥作为限流的 key
        return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getHeaders().getFirst("X-Ca-Key")));
    }

}
