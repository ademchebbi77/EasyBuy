package tn.esprit.spring.backend.order.messaging;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.spring.backend.config.RabbitMQConfig;
import tn.esprit.spring.backend.order.dto.ProductStockEventDTO;

@Service
public class ProductStockProducer {

    private final RabbitTemplate rabbitTemplate;

    public ProductStockProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendStockUpdate(ProductStockEventDTO eventDTO) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.PRODUCT_STOCK_QUEUE, eventDTO);
            System.out.println("Stock update event sent to RabbitMQ for product: " + eventDTO.getProductId());
        } catch (AmqpException e) {
            System.out.println("Error sending stock update event to RabbitMQ");
            throw e;
        }
    }
}