package site.shazan.ecommerce.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Gateway Configuration with Eureka Service Discovery
 * Uses lb:// (load balancer) scheme to dynamically discover services from Eureka
 * instead of hardcoded URLs. This allows services to be added/removed without
 * restarting the gateway.
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product", r -> r
                        .path("/api/products/**")
                        .uri("lb://PRODUCT"))
                .route("user", r -> r
                        .path("/api/users/**")
                        .uri("lb://USER"))
                .route("order-service", r -> r
                        .path("/api/orders/**")
                        .uri("lb://ORDER-SERVICE"))
                .route("cart-service", r -> r
                        .path("/api/cart/**")
                        .uri("lb://ORDER-SERVICE"))
                .build();
    }
}

