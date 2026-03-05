package site.shazan.ecommerce.notification.config;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import site.shazan.ecommerce.notification.payload.OrderCreatedEvent;
import site.shazan.ecommerce.notification.payload.OrderStatus;

import java.util.Map;

@Service
public class OrderConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void handleOverEvenet(OrderCreatedEvent orderEvent) {
        System.out.println("Order Event Received: " + orderEvent);
        long orderId = orderEvent.getOrderId();
        OrderStatus orderStatus = orderEvent.getStatus();
        System.out.println("Order ID: " + orderId);
        System.out.println("Order Status: " + orderStatus);
    }

}
