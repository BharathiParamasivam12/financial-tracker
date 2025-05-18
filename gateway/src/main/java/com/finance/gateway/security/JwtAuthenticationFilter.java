package com.finance.gateway.security;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.beans.factory.annotation.Autowired;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;
import org.springframework.core.io.buffer.DataBufferUtils;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    private final JwtAuth jwtAuth;

    @Autowired
    public JwtAuthenticationFilter(JwtAuth jwtAuth) {
        this.jwtAuth = jwtAuth;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();
        return  chain.filter(exchange);


     /**
        if (request.getURI().getPath().contains("/api/auth/login")) {
            return chain.filter(exchange);
        }

        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return this.onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return this.onError(exchange, "Invalid Authorization Header", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtAuth.extractAllClaims(token);
            exchange.getAttributes().put("claims", claims);
            return chain.filter(exchange);
        } catch (ExpiredJwtException e) {
            return this.onError(exchange, "Token has expired", HttpStatus.UNAUTHORIZED);
        } catch (JwtException e) {
            return this.onError(exchange, "Invalid JWT Token", HttpStatus.UNAUTHORIZED);
        }
      **/
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
        
        return Mono.defer(() -> {
            DataBuffer buffer = response.bufferFactory()
                .wrap(err.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer))
                .doOnError(error -> DataBufferUtils.release(buffer));
        });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}