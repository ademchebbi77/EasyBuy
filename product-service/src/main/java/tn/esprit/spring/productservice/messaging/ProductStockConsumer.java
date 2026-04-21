package tn.esprit.spring.productservice.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import tn.esprit.spring.productservice.config.RabbitMQConfig;
import tn.esprit.spring.productservice.dto.LowStockAlertDTO;
import tn.esprit.spring.productservice.dto.ProductStockEventDTO;
import tn.esprit.spring.productservice.entity.Product;
import tn.esprit.spring.productservice.service.ProductService;

@Service
public class ProductStockConsumer {

    private final ProductService productService;
    private final LowStockAlertProducer lowStockAlertProducer;

    public ProductStockConsumer(ProductService productService, LowStockAlertProducer lowStockAlertProducer) {
        this.productService = productService;
        this.lowStockAlertProducer = lowStockAlertProducer;
    }

    @RabbitListener(queues = RabbitMQConfig.PRODUCT_STOCK_QUEUE)
    public void consumeStockUpdate(ProductStockEventDTO eventDTO) {
        System.out.println("Stock update event received for product: " + eventDTO.getProductId());
        
        // Reduce stock
        productService.reduceStock(eventDTO.getProductId(), eventDTO.getQuantity());
        System.out.println("Stock reduced successfully for product: " + eventDTO.getProductId());
        
        // Check if stock is low after reduction
        Product product = productService.getProductEntity(eventDTO.getProductId());
        if (product != null && product.getStock() != null) {
            int currentStock = product.getStock();
            int threshold = 5;
            
            if (currentStock <= threshold) {
                String alertType = currentStock == 0 ? "OUT_OF_STOCK" : "LOW_STOCK";
                
                LowStockAlertDTO alertDTO = new LowStockAlertDTO(
                    product.getId(),
                    product.getName(),
                    currentStock,
                    threshold,
                    product.getUserId(),
                    alertType
                );
                
                lowStockAlertProducer.sendLowStockAlert(alertDTO);
                System.out.println("Low stock alert sent for product: " + product.getName() + " (Stock: " + currentStock + ")");
            }
        }
    }
}