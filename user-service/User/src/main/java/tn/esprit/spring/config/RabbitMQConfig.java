package tn.esprit.spring.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_QUEUE = "orderQueue";
    public static final String ORDER_STATUS_QUEUE = "orderStatusQueue";
    public static final String USER_EVENT_QUEUE = "userEventQueue";
    public static final String LOW_STOCK_ALERT_QUEUE = "lowStockAlertQueue";
    public static final String REVIEW_CREATED_QUEUE = "reviewCreatedQueue";

    @Bean
    public Queue orderQueue() { return new Queue(ORDER_QUEUE, true); }

    @Bean
    public Queue orderStatusQueue() { return new Queue(ORDER_STATUS_QUEUE, true); }

    @Bean
    public Queue userEventQueue() { return new Queue(USER_EVENT_QUEUE, true); }

    @Bean
    public Queue lowStockAlertQueue() { return new Queue(LOW_STOCK_ALERT_QUEUE, true); }

    @Bean
    public Queue reviewCreatedQueue() { return new Queue(REVIEW_CREATED_QUEUE, true); }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(3);
        return factory;
    }
}