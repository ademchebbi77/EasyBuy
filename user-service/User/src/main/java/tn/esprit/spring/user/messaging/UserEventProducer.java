package tn.esprit.spring.user.messaging;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.spring.config.RabbitMQConfig;
import tn.esprit.spring.user.dto.UserEventDTO;

@Service
public class UserEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public UserEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendUserEvent(UserEventDTO eventDTO) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.USER_EVENT_QUEUE, eventDTO);
            System.out.println("User event sent to RabbitMQ: userId=" + eventDTO.getUserId() 
                    + ", eventType=" + eventDTO.getEventType() + ", enabled=" + eventDTO.isEnabled());
        } catch (AmqpException e) {
            System.out.println("Error sending user event to RabbitMQ");
            throw e;
        }
    }
}