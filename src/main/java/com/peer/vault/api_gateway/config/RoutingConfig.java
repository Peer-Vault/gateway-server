package com.peer.vault.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutingConfig {

    @Bean
    public RouteLocator peerVaultConfig(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/api/file/**")
                        .uri("lb://FILESERVICE"))
                .build();
    }

}
