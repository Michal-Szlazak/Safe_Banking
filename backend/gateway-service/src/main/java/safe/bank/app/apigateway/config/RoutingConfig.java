package safe.bank.app.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutingConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("bank-service", r -> r
                        .path("/bank/**")
                        .uri("https://bank-service:8083")
                )
                .route("auth-service", r -> r
                        .path("/auth/**")
                        .uri("https://auth-service:8082/")
                )
                .build();
    }
}
