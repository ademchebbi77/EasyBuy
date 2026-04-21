package tn.esprit.spring.user.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import tn.esprit.spring.config.RabbitMQConfig;
import tn.esprit.spring.user.dto.OrderStatusEventDTO;

@Service
public class OrderStatusConsumer {

    @RabbitListener(
            queues = RabbitMQConfig.ORDER_STATUS_QUEUE,
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void receiveOrderStatusChange(OrderStatusEventDTO eventDTO) {
        System.out.println("========================================");
        System.out.println("Order Status Change Received from RabbitMQ:");
        System.out.println("  Order ID: " + eventDTO.getOrderId());
        System.out.println("  Order Reference: " + eventDTO.getOrderReference());
        System.out.println("  User ID: " + eventDTO.getUserId());
        System.out.println("  Previous Status: " + eventDTO.getPreviousStatus());
        System.out.println("  New Status: " + eventDTO.getStatus());
        System.out.println("  Changed By Admin ID: " + eventDTO.getChangedByUserId());
        System.out.println("  Timestamp: " + eventDTO.getTimestamp());
        System.out.println("========================================");

        // Here you can add additional logic:
        // - Send email/SMS notification to the user
        // - Store status history in database
        // - Trigger other business processes
        // - Update user's order statistics
    }
}
