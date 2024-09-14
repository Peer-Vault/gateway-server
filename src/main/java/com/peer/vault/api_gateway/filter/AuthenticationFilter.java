package com.peer.vault.api_gateway.filter;

import com.peer.vault.api_gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;

import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    //    @Autowired
//    private RestTemplate template;
    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            boolean isSecuredRequest = validator.isSecured.test(exchange.getRequest());

            // Logging for debugging
            System.out.println("Is Secured Request: " + isSecuredRequest);
            System.out.println("Request Path: " + exchange.getRequest().getURI().getPath());

            if (isSecuredRequest) {
                // Check if the header contains a token
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                try {
                    jwtUtil.validateToken(authHeader);
                } catch (Exception e) {
                    throw new RuntimeException("Unauthorized access to application", e);
                }
            }
            return chain.filter(exchange);
        };
    }


//    @Override
//    public GatewayFilter apply(Config config) {
//        return ((exchange, chain) -> {
//            if (validator.isSecured.test(exchange.getRequest())) {
//                //header contains token or not
//                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//                    throw new RuntimeException("missing authorization header");
//                }
//
//                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
//                if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                    authHeader = authHeader.substring(7);
//                }
//                try {
//                    jwtUtil.validateToken(authHeader);
//                } catch (Exception e) {
//                    throw new RuntimeException("un authorized access to application");
//                }
//            }
////            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
////            System.out.println("Auth Header" + authHeader);
////            if (authHeader != null) {
////                return chain.filter(exchange.mutate().request(
////                        exchange.getRequest().mutate().header(HttpHeaders.AUTHORIZATION, authHeader).build()
////                ).build());
////            }
//            return chain.filter(exchange);
//        });
//    }

    public static class Config {

    }
}
