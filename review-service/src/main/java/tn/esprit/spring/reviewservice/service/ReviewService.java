package tn.esprit.spring.reviewservice.service;

import org.springframework.stereotype.Service;
import tn.esprit.spring.reviewservice.client.ProductClient;
import tn.esprit.spring.reviewservice.client.UserClient;
import tn.esprit.spring.reviewservice.dto.*;
import tn.esprit.spring.reviewservice.entity.Review;
import tn.esprit.spring.reviewservice.messaging.ReviewCreatedProducer;
import tn.esprit.spring.reviewservice.repository.ReviewRepository;

import java.time.Instant;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository repository;
    private final UserClient userClient;
    private final ProductClient productClient;
    private final ReviewCreatedProducer reviewCreatedProducer;

    public ReviewService(ReviewRepository repository, UserClient userClient,
                         ProductClient productClient, ReviewCreatedProducer reviewCreatedProducer) {
        this.repository = repository;
        this.userClient = userClient;
        this.productClient = productClient;
        this.reviewCreatedProducer = reviewCreatedProducer;
    }

    public ReviewResponse create(CreateReviewRequest req) {
        ProductDto product = getProductOrThrow(req.productId());

        if (req.rating() == null || req.rating() < 1 || req.rating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        Review review = Review.builder()
                .userId(req.userId())
                .productId(req.productId())
                .rating(req.rating())
                .comment(req.comment())
                .createdAt(Instant.now())
                .build();

        Review saved = repository.save(review);

        reviewCreatedProducer.sendReviewCreatedEvent(new ReviewCreatedEventDTO(
                saved.getId(),
                req.userId(),
                "user",
                product.id(),
                product.name(),
                saved.getRating(),
                saved.getComment()
        ));

        return map(saved);
    }

    public List<ReviewResponse> findAll() {
        return repository.findAll().stream().map(this::map).toList();
    }

    public ReviewResponse findById(Long id) {
        return map(getEntity(id));
    }

    public List<ReviewResponse> findByProductId(Long productId) {
        getProductOrThrow(productId);
        List<ReviewResponse> reviews = repository.findByProductId(productId)
                .stream().map(this::map).toList();
        if (reviews.isEmpty()) {
            throw new RuntimeException("No reviews found for product id: " + productId);
        }
        return reviews;
    }

    public List<ReviewResponse> findByUserId(Long userId) {
        List<ReviewResponse> reviews = repository.findByUserId(userId)
                .stream().map(this::map).toList();
        if (reviews.isEmpty()) {
            throw new RuntimeException("No reviews found for user id: " + userId);
        }
        return reviews;
    }

    public ReviewResponse update(Long id, UpdateReviewRequest req) {
        Review review = getEntity(id);

        if (req.rating() != null) {
            if (req.rating() < 1 || req.rating() > 5) {
                throw new RuntimeException("Rating must be between 1 and 5");
            }
            review.setRating(req.rating());
        }

        if (req.comment() != null) {
            review.setComment(req.comment());
        }

        return map(repository.save(review));
    }

    public void delete(Long id) {
        getEntity(id);
        repository.deleteById(id);
    }

    private Review getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
    }

    private UserDto getUserOrThrow(Long userId) {
        try {
            UserDto user = userClient.getUserById(userId);
            if (!user.enabled()) {
                throw new RuntimeException("User is disabled with id: " + userId);
            }
            return user;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

    private ProductDto getProductOrThrow(Long productId) {
        try {
            return productClient.getProductById(productId);
        } catch (Exception e) {
            throw new RuntimeException("Product not found with id: " + productId);
        }
    }

    private ReviewResponse map(Review r) {
        return new ReviewResponse(
                r.getId(),
                r.getUserId(),
                r.getProductId(),
                r.getRating(),
                r.getComment(),
                r.getCreatedAt()
        );
    }
}
