package tn.esprit.spring.backend.order.messaging;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.spring.backend.config.RabbitMQConfig;
import tn.esprit.spring.backend.order.dto.OrderStatusEventDTO;

@Service
public class OrderStatusProducer {

    private final RabbitTemplate rabbitTemplate;

    public OrderStatusProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderStatusChange(OrderStatusEventDTO eventDTO) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_STATUS_QUEUE, eventDTO);
            System.out.println("Order status change event sent to RabbitMQ: orderId=" + eventDTO.getOrderId() 
                    + ", status=" + eventDTO.getStatus() + ", changedBy=" + eventDTO.getChangedByUserId());
        } catch (AmqpException e) {
            System.out.println("Error sending order status event to RabbitMQ");
            throw e;
        }
    }
}
