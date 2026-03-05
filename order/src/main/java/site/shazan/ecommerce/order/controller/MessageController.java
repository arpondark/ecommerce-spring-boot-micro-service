package site.shazan.ecommerce.order.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class MessageController {
    @Value("${app.message:No message configured}")
    private String message;

    @RateLimiter(name="rateBreaker",fallbackMethod = "fallbackRetry")
    @GetMapping("/message")
    public String getMessage() {
        return message;
    }

    public String fallbackRetry(Exception e){
        return "Service is currently unavailable. Please try again later.";
    }
}
