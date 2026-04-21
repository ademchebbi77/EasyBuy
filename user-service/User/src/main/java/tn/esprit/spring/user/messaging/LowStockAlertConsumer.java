package tn.esprit.spring.user.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import tn.esprit.spring.config.RabbitMQConfig;
import tn.esprit.spring.user.dto.LowStockAlertDTO;

@Service
public class LowStockAlertConsumer {

    @RabbitListener(
            queues = RabbitMQConfig.LOW_STOCK_ALERT_QUEUE,
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void receiveLowStockAlert(LowStockAlertDTO alertDTO) {
        System.out.println("🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨");
        System.out.println("         LOW STOCK ALERT RECEIVED!");
        System.out.println("🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨");
        System.out.println("  Product ID: " + alertDTO.getProductId());
        System.out.println("  Product Name: " + alertDTO.getProductName());
        System.out.println("  Current Stock: " + alertDTO.getCurrentStock());
        System.out.println("  Alert Type: " + alertDTO.getAlertType());
        System.out.println("  Product Owner ID: " + alertDTO.getUserId());
        System.out.println("  Timestamp: " + alertDTO.getTimestamp());
        System.out.println("🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨");

        // Here you can add additional logic:
        // - Send email to admin users
        // - Send notification to product owner
        // - Store alert in database for admin dashboard
        // - Trigger automatic reorder process
        // - Send SMS to warehouse manager
        
        if ("OUT_OF_STOCK".equals(alertDTO.getAlertType())) {
            System.out.println("⚠️  CRITICAL: Product is OUT OF STOCK!");
        } else {
            System.out.println("⚠️  WARNING: Product stock is running low!");
        }
    }
}