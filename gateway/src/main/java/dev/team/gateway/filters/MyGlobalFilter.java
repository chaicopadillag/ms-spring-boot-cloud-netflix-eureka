package dev.team.gateway.filters;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class MyGlobalFilter implements GlobalFilter, Ordered {

    private final Logger log = LoggerFactory.getLogger(MyGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Ejecutando filtro pre");

        exchange.getRequest().mutate().headers(h -> h.add("token", "f5sd4f5ds4f5sd4f5sdf4ds65f4sd8f7sdf4"));

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {

            log.info("Ejecutando filtro post");

            Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("token")).ifPresent(value -> {
                exchange.getRequest().getHeaders().add("token", value);
            });

            exchange.getResponse().getCookies().add("Color", ResponseCookie.from("color", "blue").build());

            exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);

        }));
    }

    @Override
    public int getOrder() {
        return 10;
    }

}
