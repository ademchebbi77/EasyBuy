package tn.esprit.spring.backend.order.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import tn.esprit.spring.backend.config.RabbitMQConfig;
import tn.esprit.spring.backend.order.dto.UserEventDTO;
import tn.esprit.spring.backend.order.service.OrderService;

@Service
public class UserEventConsumer {

    private final OrderService orderService;

    public UserEventConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = RabbitMQConfig.USER_EVENT_QUEUE)
    public void receiveUserEvent(UserEventDTO eventDTO) {
        System.out.println("========================================");
        System.out.println("User Event Received from RabbitMQ:");
        System.out.println("  User ID: " + eventDTO.getUserId());
        System.out.println("  Username: " + eventDTO.getUsername());
        System.out.println("  Event Type: " + eventDTO.getEventType());
        System.out.println("  Enabled: " + eventDTO.isEnabled());
        System.out.println("  Timestamp: " + eventDTO.getTimestamp());
        System.out.println("========================================");

        // Business logic: If user is disabled, cancel their pending orders
        if ("DISABLED".equals(eventDTO.getEventType()) || !eventDTO.isEnabled()) {
            System.out.println("User " + eventDTO.getUserId() + " was disabled. Cancelling their pending orders...");
            // You could add: orderService.cancelUserPendingOrders(eventDTO.getUserId());
        }
    }
}