package tn.esprit.spring.productservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.spring.productservice.dto.CategoryDto;

@FeignClient(name = "CATEGORY-SERVICE")
public interface CategoryClient {
    
    @GetMapping("/api/categories/{id}")
    CategoryDto getCategoryById(@PathVariable Long id);
}
