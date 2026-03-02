package site.shazan.ecommerce.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product", r -> r
                        .path("/api/products/**")
                        .uri("http://localhost:8082"))
                .route("user", r -> r
                        .path("/api/users/**")
                        .uri("http://localhost:8081"))
                .route("order-service", r -> r
                        .path("/api/orders/**")
                        .uri("http://localhost:8083"))
                .route("cart-service", r -> r
                        .path("/api/cart/**")
                        .uri("http://localhost:8083"))
                .build();
    }
}

