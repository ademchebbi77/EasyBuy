package tn.esprit.spring.productservice.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import tn.esprit.spring.productservice.config.RabbitMQConfig;
import tn.esprit.spring.productservice.dto.CategoryDeletedEventDTO;
import tn.esprit.spring.productservice.entity.Product;
import tn.esprit.spring.productservice.repository.ProductRepository;

import java.util.List;

@Service
public class CategoryDeletedConsumer {

    private final ProductRepository productRepository;

    public CategoryDeletedConsumer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.CATEGORY_DELETED_QUEUE)
    public void receiveCategoryDeletedEvent(CategoryDeletedEventDTO event) {
        System.out.println("Category deleted event received: categoryId=" + event.getCategoryId()
                + ", categoryName=" + event.getCategoryName());

        List<Product> affected = productRepository.findByCategoryId(event.getCategoryId());

        for (Product product : affected) {
            product.setCategoryId(null);
            productRepository.save(product);
            System.out.println("  → Product '" + product.getName() + "' (id=" + product.getId()
                    + ") categoryId set to null");
        }

        System.out.println("  → Total affected products: " + affected.size());
    }
}
