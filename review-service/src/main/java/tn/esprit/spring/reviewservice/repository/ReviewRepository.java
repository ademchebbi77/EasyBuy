package tn.esprit.spring.reviewservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.spring.reviewservice.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);
    List<Review> findByUserId(Long userId);
}
