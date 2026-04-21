package tn.esprit.spring.backend.order.messaging;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.spring.backend.config.RabbitMQConfig;
import tn.esprit.spring.backend.order.dto.OrderEventDTO;

@Service
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;

    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrder(OrderEventDTO orderEventDTO) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_QUEUE, orderEventDTO);
            System.out.println("Order event sent to RabbitMQ: " + orderEventDTO.getReference());
        } catch (AmqpException e) {
            System.out.println("Error sending order event to RabbitMQ");
            throw e;
        }
    }
}