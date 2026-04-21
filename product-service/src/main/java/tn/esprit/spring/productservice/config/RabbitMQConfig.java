package tn.esprit.spring.productservice.config;

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

    public static final String PRODUCT_STOCK_QUEUE = "productStockQueue";
    public static final String LOW_STOCK_ALERT_QUEUE = "lowStockAlertQueue";
    public static final String PRODUCT_CATEGORY_QUEUE = "productCategoryQueue";
    public static final String CATEGORY_DELETED_QUEUE = "categoryDeletedQueue";

    @Bean
    public Queue productStockQueue() {
        return new Queue(PRODUCT_STOCK_QUEUE, true);
    }

    @Bean
    public Queue lowStockAlertQueue() {
        return new Queue(LOW_STOCK_ALERT_QUEUE, true);
    }

    @Bean
    public Queue productCategoryQueue() {
        return new Queue(PRODUCT_CATEGORY_QUEUE, true);
    }

    @Bean
    public Queue categoryDeletedQueue() {
        return new Queue(CATEGORY_DELETED_QUEUE, true);
    }

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
            MessageConverter messageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}