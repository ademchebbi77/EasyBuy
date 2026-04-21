package com.example.paymentservice.messaging;

import com.example.paymentservice.config.RabbitMQConfig;
import com.example.paymentservice.dto.PaymentEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendPaymentEvent(PaymentEventDTO event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.PAYMENT_QUEUE, event);
    }
}
