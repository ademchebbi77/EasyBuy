package tn.esprit.spring.productservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import tn.esprit.spring.productservice.client.CategoryClient;
import tn.esprit.spring.productservice.client.UserClient;
import tn.esprit.spring.productservice.dto.CreateProductRequest;
import tn.esprit.spring.productservice.dto.ProductCategoryEventDTO;
import tn.esprit.spring.productservice.dto.ProductResponse;
import tn.esprit.spring.productservice.dto.UpdateProductRequest;
import tn.esprit.spring.productservice.dto.UserDto;
import tn.esprit.spring.productservice.entity.Product;
import tn.esprit.spring.productservice.messaging.ProductCategoryProducer;
import tn.esprit.spring.productservice.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final UserClient userClient;
    private final CategoryClient categoryClient;
    private final ProductCategoryProducer productCategoryProducer;

    public ProductService(ProductRepository repository, UserClient userClient,
                          CategoryClient categoryClient, ProductCategoryProducer productCategoryProducer) {
        this.repository = repository;
        this.userClient = userClient;
        this.categoryClient = categoryClient;
        this.productCategoryProducer = productCategoryProducer;
    }

    public ProductResponse create(CreateProductRequest req) {
        if (req.categoryId() != null) {
            validateCategory(req.categoryId());
        }

        Product product = Product.builder()
                .name(req.name())
                .description(req.description())
                .price(req.price())
                .stock(req.stock())
                .imageUrl(req.imageUrl())
                .userId(req.userId())
                .categoryId(req.categoryId())
                .build();

        Product saved = repository.save(product);

        if (saved.getCategoryId() != null) {
            productCategoryProducer.sendProductCategoryEvent(
                new ProductCategoryEventDTO(saved.getId(), saved.getName(), saved.getCategoryId(), "ASSIGNED")
            );
        }

        return map(saved);
    }

    public List<ProductResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    public List<ProductResponse> searchByName(String name) {
        if (name == null || name.isBlank()) {
            return findAll();
        }
        List<ProductResponse> products = repository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::map)
                .toList();
        
        if (products.isEmpty()) {
            throw new RuntimeException("No products found with name containing: " + name);
        }
        
        return products;
    }

    public ProductResponse findById(Long id) {
        return map(getEntity(id));
    }

    public ProductResponse update(Long id, UpdateProductRequest req) {
        if (req.categoryId() != null) {
            validateCategory(req.categoryId());
        }

        Product product = getEntity(id);
        Long oldCategoryId = product.getCategoryId();

        product.setName(req.name());
        product.setDescription(req.description());
        product.setPrice(req.price());
        product.setStock(req.stock());
        product.setImageUrl(req.imageUrl());
        product.setUserId(req.userId());
        product.setCategoryId(req.categoryId());

        Product saved = repository.save(product);

        // Send event if category changed
        if (req.categoryId() != null && !req.categoryId().equals(oldCategoryId)) {
            productCategoryProducer.sendProductCategoryEvent(
                new ProductCategoryEventDTO(saved.getId(), saved.getName(), saved.getCategoryId(), "ASSIGNED")
            );
        } else if (req.categoryId() == null && oldCategoryId != null) {
            productCategoryProducer.sendProductCategoryEvent(
                new ProductCategoryEventDTO(saved.getId(), saved.getName(), oldCategoryId, "UNASSIGNED")
            );
        }

        return map(saved);
    }

    public void delete(Long id) {
        Product product = getEntity(id);
        repository.delete(product);
    }

    public List<ProductResponse> findByUserId(Long userId) {
        return repository.findByUserId(userId)
                .stream()
                .map(this::map)
                .toList();
    }
    
    public List<ProductResponse> findByCategoryId(Long categoryId) {
        validateCategory(categoryId);
        
        List<ProductResponse> products = repository.findByCategoryId(categoryId)
                .stream()
                .map(this::map)
                .toList();
        
        if (products.isEmpty()) {
            throw new RuntimeException("No products found for category id: " + categoryId);
        }
        
        return products;
    }

    public void reduceStock(Long productId, Integer quantity) {
        Product product = getEntity(productId);

        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        if (product.getStock() < quantity) {
            throw new RuntimeException("Not enough stock for product id: " + productId);
        }

        product.setStock(product.getStock() - quantity);
        repository.save(product);
    }

    public Product getProductEntity(Long id) {
        return getEntity(id);
    }

    private Product getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    private void validateCategory(Long categoryId) {
        try {
            categoryClient.getCategoryById(categoryId);
        } catch (Exception e) {
            throw new RuntimeException("Category not found with id: " + categoryId);
        }
    }

    private ProductResponse map(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getStock(),
                p.getImageUrl(),
                p.getUserId(),
                p.getCategoryId()
        );
    }
}