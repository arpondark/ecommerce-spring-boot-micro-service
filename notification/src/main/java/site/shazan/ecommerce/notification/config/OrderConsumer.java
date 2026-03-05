package site.shazan.ecommerce.notification.config;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void handleOverEvenet(Map<String, Object> orderEvent) {
        System.out.println("Order Event Received: " + orderEvent);
        long orderId = Long.parseLong(orderEvent.get("orderId").toString());
        String status = orderEvent.get("status").toString();
        System.out.println("Order ID: " + orderId);
        System.out.println("Order Status: " + status);
    }

}
