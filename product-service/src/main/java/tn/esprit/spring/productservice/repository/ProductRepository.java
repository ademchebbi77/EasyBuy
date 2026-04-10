package tn.esprit.spring.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.spring.productservice.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUserId(Long userId);
}