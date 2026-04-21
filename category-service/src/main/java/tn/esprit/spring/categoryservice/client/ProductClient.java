package tn.esprit.spring.categoryservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.spring.categoryservice.dto.ProductDto;

import java.util.List;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductClient {
    
    @GetMapping("/api/products/category/{categoryId}")
    List<ProductDto> getProductsByCategoryId(@PathVariable Long categoryId);
}
