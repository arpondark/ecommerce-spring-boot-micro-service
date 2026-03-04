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
                .route("eureka-dashboard", r -> r
                        .path("/eureka/main")
                        .filters(f -> f.setPath("/"))
                        .uri("http://localhost:8761"))
                .route("eureka-static", r -> r
                        .path("/eureka/**")
                        .uri("http://localhost:8761"))
                .route("product", r -> r
                        .path("/products/**")
                        .filters(f -> f.circuitBreaker(config -> config.setName("ecomApp"))
                                       .rewritePath("/products(?<segment>/?.*)", "/api/products${segment}"))
                        .uri("lb://PRODUCT"))
                .route("user", r -> r
                        .path("/users/**")
                        .filters(f -> f.rewritePath("/users(?<segment>/?.*)", "/api/users${segment}"))
                        .uri("lb://USER"))
                .route("order-service", r -> r
                        .path("/orders/**")
                        .filters(f -> f.rewritePath("/orders(?<segment>/?.*)", "/api/orders${segment}"))
                        .uri("lb://ORDER-SERVICE"))
                .route("cart-service", r -> r
                        .path("/cart/**")
                        .filters(f -> f.rewritePath("/cart(?<segment>/?.*)", "/api/cart${segment}"))
                        .uri("lb://ORDER-SERVICE"))
                .build();
    }
}

