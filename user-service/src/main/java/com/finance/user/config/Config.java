package com.finance.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
public class Config {

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        http
////                .csrf(csrf -> csrf.disable())
////                .authorizeHttpRequests(auth -> auth
////                        .requestMatchers("/v1/users/**", "/actuator/**").permitAll()
////                        .anyRequest().authenticated()
////                )
////                .addFilterBefore(new GatewayAuthHeaderFilter(), UsernamePasswordAuthenticationFilter.class);
////
////        return http.build();
//
//
//        return http
//                .csrf(csrfSpec -> csrfSpec.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll())
//                .build();
//
//    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/v1/**","/auth/**", "/public/**").permitAll()
                        .anyExchange().authenticated()
                )
                .build();
    }

}