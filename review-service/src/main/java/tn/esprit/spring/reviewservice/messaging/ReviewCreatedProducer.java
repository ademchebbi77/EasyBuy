package tn.esprit.spring.reviewservice.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.spring.reviewservice.config.RabbitMQConfig;
import tn.esprit.spring.reviewservice.dto.ReviewCreatedEventDTO;

@Service
public class ReviewCreatedProducer {

    private final RabbitTemplate rabbitTemplate;

    public ReviewCreatedProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendReviewCreatedEvent(ReviewCreatedEventDTO event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.REVIEW_CREATED_QUEUE, event);
        System.out.println("Review created event sent: userId=" + event.getUserId()
                + ", productId=" + event.getProductId()
                + ", rating=" + event.getRating());
    }
}
