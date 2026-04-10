package tn.esprit.spring.productservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import tn.esprit.spring.productservice.client.UserClient;
import tn.esprit.spring.productservice.dto.CreateProductRequest;
import tn.esprit.spring.productservice.dto.ProductResponse;
import tn.esprit.spring.productservice.dto.UpdateProductRequest;
import tn.esprit.spring.productservice.dto.UserDto;
import tn.esprit.spring.productservice.entity.Product;
import tn.esprit.spring.productservice.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final UserClient userClient;

    public ProductService(ProductRepository repository, UserClient userClient) {
        this.repository = repository;
        this.userClient = userClient;
    }

    public ProductResponse create(CreateProductRequest req) {
        validateAdminUser(req.userId());

        Product product = Product.builder()
                .name(req.name())
                .description(req.description())
                .price(req.price())
                .stock(req.stock())
                .imageUrl(req.imageUrl())
                .userId(req.userId())
                .build();

        return map(repository.save(product));
    }

    public List<ProductResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    public ProductResponse findById(Long id) {
        return map(getEntity(id));
    }

    public ProductResponse update(Long id, UpdateProductRequest req) {
        validateAdminUser(req.userId());

        Product product = getEntity(id);

        product.setName(req.name());
        product.setDescription(req.description());
        product.setPrice(req.price());
        product.setStock(req.stock());
        product.setImageUrl(req.imageUrl());
        product.setUserId(req.userId());

        return map(repository.save(product));
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

    private Product getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    private void validateAdminUser(Long userId) {
        UserDto user;
        try {
            user = userClient.getUserById(userId);
        } catch (Exception e) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        if (user == null) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        if (!user.enabled()) {
            throw new RuntimeException("User is disabled with id: " + userId);
        }

        if (!"ADMIN".equalsIgnoreCase(user.role())) {
            throw new RuntimeException("User is not ADMIN with id: " + userId);
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
                p.getUserId()
        );
    }
}