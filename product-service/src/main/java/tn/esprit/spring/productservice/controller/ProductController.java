package tn.esprit.spring.productservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.productservice.dto.CreateProductRequest;
import tn.esprit.spring.productservice.dto.ProductResponse;
import tn.esprit.spring.productservice.dto.UpdateProductRequest;
import tn.esprit.spring.productservice.service.ProductService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest req) {
        ProductResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/api/products/" + created.id())).body(created);
    }

    @GetMapping
    public List<ProductResponse> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ProductResponse one(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public List<ProductResponse> byUserId(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }
}