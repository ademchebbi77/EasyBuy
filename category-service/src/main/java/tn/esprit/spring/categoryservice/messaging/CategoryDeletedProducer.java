package tn.esprit.spring.categoryservice.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.spring.categoryservice.config.RabbitMQConfig;
import tn.esprit.spring.categoryservice.dto.CategoryDeletedEventDTO;

@Service
public class CategoryDeletedProducer {

    private final RabbitTemplate rabbitTemplate;

    public CategoryDeletedProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCategoryDeletedEvent(CategoryDeletedEventDTO event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.CATEGORY_DELETED_QUEUE, event);
        System.out.println("Category deleted event sent: categoryId=" + event.getCategoryId()
                + ", categoryName=" + event.getCategoryName());
    }
}
