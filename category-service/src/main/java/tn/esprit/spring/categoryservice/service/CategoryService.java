package tn.esprit.spring.categoryservice.service;

import org.springframework.stereotype.Service;
import tn.esprit.spring.categoryservice.client.ProductClient;
import tn.esprit.spring.categoryservice.client.UserClient;
import tn.esprit.spring.categoryservice.dto.CategoryDeletedEventDTO;
import tn.esprit.spring.categoryservice.dto.CategoryResponse;
import tn.esprit.spring.categoryservice.dto.CreateCategoryRequest;
import tn.esprit.spring.categoryservice.dto.ProductDto;
import tn.esprit.spring.categoryservice.dto.UpdateCategoryRequest;
import tn.esprit.spring.categoryservice.dto.UserDto;
import tn.esprit.spring.categoryservice.entity.Category;
import tn.esprit.spring.categoryservice.messaging.CategoryDeletedProducer;
import tn.esprit.spring.categoryservice.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    private final UserClient userClient;
    private final ProductClient productClient;
    private final CategoryDeletedProducer categoryDeletedProducer;

    public CategoryService(CategoryRepository repository, UserClient userClient,
                           ProductClient productClient, CategoryDeletedProducer categoryDeletedProducer) {
        this.repository = repository;
        this.userClient = userClient;
        this.productClient = productClient;
        this.categoryDeletedProducer = categoryDeletedProducer;
    }

    public CategoryResponse create(CreateCategoryRequest req) {
        if (repository.existsByName(req.name())) {
            throw new RuntimeException("Category name already exists: " + req.name());
        }

        Category category = Category.builder()
                .name(req.name())
                .description(req.description())
                .build();

        return map(repository.save(category));
    }

    public List<CategoryResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    public List<CategoryResponse> searchByName(String name) {
        if (name == null || name.isBlank()) {
            return findAll();
        }
        List<CategoryResponse> categories = repository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::map)
                .toList();

        if (categories.isEmpty()) {
            throw new RuntimeException("No categories found with name containing: " + name);
        }

        return categories;
    }

    public CategoryResponse findById(Long id) {
        return map(getEntity(id));
    }

    public CategoryResponse update(Long id, UpdateCategoryRequest req) {
        Category category = getEntity(id);

        if (!category.getName().equals(req.name()) && repository.existsByName(req.name())) {
            throw new RuntimeException("Category name already exists: " + req.name());
        }

        category.setName(req.name());
        category.setDescription(req.description());

        return map(repository.save(category));
    }

    public void delete(Long id) {
        Category category = getEntity(id);
        repository.delete(category);
        categoryDeletedProducer.sendCategoryDeletedEvent(
            new CategoryDeletedEventDTO(category.getId(), category.getName())
        );
    }
    
    public List<ProductDto> getProductsInCategory(Long categoryId) {
        getEntity(categoryId);
        
        try {
            return productClient.getProductsByCategoryId(categoryId);
        } catch (Exception e) {
            throw new RuntimeException("No products found for category id: " + categoryId);
        }
    }

    private Category getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
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
            throw new RuntimeException("Only ADMIN users can manage categories. Your role: " + user.role());
        }
    }

    private CategoryResponse map(Category c) {
        return new CategoryResponse(
                c.getId(),
                c.getName(),
                c.getDescription()
        );
    }
}
