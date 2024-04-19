package csu.yulin.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Objects;

/**
 * @author 刘飘
 */
@Component
public class IPFilter extends AbstractGatewayFilterFactory<IPFilter.Config> {

    public IPFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (config.getWhitelist().contains(getClientIP(exchange))) {
                return chain.filter(exchange);
            }
            throw new RuntimeException("该IP地址不在白名单中，禁止访问");
        };
    }

    private String getClientIP(ServerWebExchange exchange) {
        return Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress();
    }

    public static class Config {
        private List<String> whitelist;

        public List<String> getWhitelist() {
            return whitelist;
        }

        public void setWhitelist(List<String> whitelist) {
            this.whitelist = whitelist;
        }
    }
}
