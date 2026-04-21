package tn.esprit.spring.productservice.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.spring.productservice.config.RabbitMQConfig;
import tn.esprit.spring.productservice.dto.ProductCategoryEventDTO;

@Service
public class ProductCategoryProducer {

    private final RabbitTemplate rabbitTemplate;

    public ProductCategoryProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendProductCategoryEvent(ProductCategoryEventDTO event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.PRODUCT_CATEGORY_QUEUE, event);
        System.out.println("Product category event sent: productId=" + event.getProductId()
                + ", productName=" + event.getProductName()
                + ", categoryId=" + event.getCategoryId()
                + ", eventType=" + event.getEventType());
    }
}
