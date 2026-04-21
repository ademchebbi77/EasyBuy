package tn.esprit.spring.productservice.messaging;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.spring.productservice.config.RabbitMQConfig;
import tn.esprit.spring.productservice.dto.LowStockAlertDTO;

@Service
public class LowStockAlertProducer {

    private final RabbitTemplate rabbitTemplate;

    public LowStockAlertProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendLowStockAlert(LowStockAlertDTO alertDTO) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.LOW_STOCK_ALERT_QUEUE, alertDTO);
            System.out.println("Low stock alert sent to RabbitMQ: productId=" + alertDTO.getProductId() 
                    + ", productName=" + alertDTO.getProductName() + ", currentStock=" + alertDTO.getCurrentStock()
                    + ", alertType=" + alertDTO.getAlertType());
        } catch (AmqpException e) {
            System.out.println("Error sending low stock alert to RabbitMQ");
            throw e;
        }
    }
}