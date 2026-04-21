package tn.esprit.spring.user.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import tn.esprit.spring.config.RabbitMQConfig;
import tn.esprit.spring.user.dto.ReviewCreatedEventDTO;

@Service
public class ReviewCreatedConsumer {

    @RabbitListener(queues = RabbitMQConfig.REVIEW_CREATED_QUEUE,
            containerFactory = "rabbitListenerContainerFactory")
    public void receiveReviewCreated(ReviewCreatedEventDTO event) {
        System.out.println("========================================");
        System.out.println("NEW REVIEW NOTIFICATION [ADMIN]");
        System.out.println("========================================");
        System.out.println("  User '" + event.getUsername() + "' (id=" + event.getUserId() + ")");
        System.out.println("  added review '" + event.getComment() + "'");
        System.out.println("  with rating " + event.getRating() + "/5");
        System.out.println("  on product '" + event.getProductName() + "' (id=" + event.getProductId() + ")");
        System.out.println("========================================");
    }
}
