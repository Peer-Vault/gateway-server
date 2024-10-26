package com.peer.vault.api_gateway.config;

import com.peer.vault.api_gateway.filter.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutingConfig {

    @Bean
    public RouteLocator peerVaultConfig(RouteLocatorBuilder routeLocatorBuilder, AuthenticationFilter authenticationFilter) {
        return routeLocatorBuilder.routes()
                .route(p -> p
                        .path("/file/download/shared/**") // Specific route for public access
                        .uri("lb://FILESERVICE"))
                .route(p -> p
                        .path("/file/**")
                        .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://FILESERVICE"))
                .route(p -> p
                        .path("/user/**")
                        .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://USERSERVICE"))
                .route(p -> p
                        .path("/notifications/**")
                        .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://NOTIFICATIONSERVICE"))
                .build();
    }


}
