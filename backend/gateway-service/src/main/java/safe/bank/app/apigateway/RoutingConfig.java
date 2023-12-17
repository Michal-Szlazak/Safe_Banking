package safe.bank.app.apigateway;

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
                        .path("/api/bank/**")
                        .uri("http://bank-service:8083")
                )
                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .uri("http://auth-service:8082")
                )
                .build();
    }
}
