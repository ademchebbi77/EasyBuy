package com.example.paymentservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitMQConfig {

    public static final String PAYMENT_QUEUE = "paymentQueue";
    public static final String ORDER_STATUS_QUEUE = "orderStatusQueue";

    @Bean
    public Queue paymentQueue() {
        log.info("📦 Creating RabbitMQ queue: {}", PAYMENT_QUEUE);
        return new Queue(PAYMENT_QUEUE, true);
    }

    @Bean
    public Queue orderStatusQueue() {
        log.info("📦 Creating RabbitMQ queue: {}", ORDER_STATUS_QUEUE);
        return new Queue(ORDER_STATUS_QUEUE, true);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        log.info("🔧 Creating Jackson2JsonMessageConverter for RabbitMQ");
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        log.info("🔧 Creating RabbitTemplate with JSON message converter");
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter) {
        log.info("🔧 Creating RabbitListenerContainerFactory with JSON message converter");
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setAutoStartup(true);
        log.info("✅ RabbitListenerContainerFactory configured and ready");
        return factory;
    }
}
