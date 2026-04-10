package tn.esprit.spring.productservice.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import tn.esprit.spring.productservice.config.RabbitMQConfig;
import tn.esprit.spring.productservice.dto.ProductStockEventDTO;
import tn.esprit.spring.productservice.service.ProductService;

@Service
public class ProductStockConsumer {

    private final ProductService productService;

    public ProductStockConsumer(ProductService productService) {
        this.productService = productService;
    }

    @RabbitListener(queues = RabbitMQConfig.PRODUCT_STOCK_QUEUE)
    public void consumeStockUpdate(ProductStockEventDTO eventDTO) {
        System.out.println("Stock update event received for product: " + eventDTO.getProductId());
        productService.reduceStock(eventDTO.getProductId(), eventDTO.getQuantity());
        System.out.println("Stock reduced successfully for product: " + eventDTO.getProductId());
    }
}