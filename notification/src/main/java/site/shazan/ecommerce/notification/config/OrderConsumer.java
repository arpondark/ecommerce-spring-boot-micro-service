package site.shazan.ecommerce.notification.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import site.shazan.ecommerce.notification.payload.OrderCreatedEvent;
import site.shazan.ecommerce.notification.payload.OrderStatus;

import java.util.function.Consumer;


@Service
@Slf4j
public class OrderConsumer {

//    @RabbitListener(queues = "${rabbitmq.queue.name}")
//    public void handleOverEvenet(OrderCreatedEvent orderEvent) {
//        System.out.println("Order Event Received: " + orderEvent);
//        long orderId = orderEvent.getOrderId();
//        OrderStatus orderStatus = orderEvent.getStatus();
//        System.out.println("Order ID: " + orderId);
//        System.out.println("Order Status: " + orderStatus);
//    }

    @Bean
    public Consumer<OrderCreatedEvent> orderCreated() {
        return event->{
            log.info("Received Order Created Event : {}",event.getOrderId());
            log.info("Received Order Created Event : {}",event.getUserId());
        };

    }

}
