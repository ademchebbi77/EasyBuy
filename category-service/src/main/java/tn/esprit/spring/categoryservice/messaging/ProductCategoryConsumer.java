package tn.esprit.spring.categoryservice.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import tn.esprit.spring.categoryservice.config.RabbitMQConfig;
import tn.esprit.spring.categoryservice.dto.ProductCategoryEventDTO;

@Service
public class ProductCategoryConsumer {

    @RabbitListener(queues = RabbitMQConfig.PRODUCT_CATEGORY_QUEUE)
    public void receiveProductCategoryEvent(ProductCategoryEventDTO event) {
        System.out.println("Product category event received from RabbitMQ:");
        System.out.println("  productId=" + event.getProductId());
        System.out.println("  productName=" + event.getProductName());
        System.out.println("  categoryId=" + event.getCategoryId());
        System.out.println("  eventType=" + event.getEventType());

        if ("ASSIGNED".equals(event.getEventType())) {
            System.out.println("  → Product '" + event.getProductName()
                    + "' was assigned to category id=" + event.getCategoryId());
        } else if ("UNASSIGNED".equals(event.getEventType())) {
            System.out.println("  → Product '" + event.getProductName()
                    + "' was removed from category id=" + event.getCategoryId());
        }
    }
}
