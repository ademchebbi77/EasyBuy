package tn.esprit.spring.categoryservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.categoryservice.dto.CategoryResponse;
import tn.esprit.spring.categoryservice.dto.CreateCategoryRequest;
import tn.esprit.spring.categoryservice.dto.ProductDto;
import tn.esprit.spring.categoryservice.dto.UpdateCategoryRequest;
import tn.esprit.spring.categoryservice.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    // ADMIN only can create categories
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> create(@RequestBody CreateCategoryRequest req) {
        CategoryResponse response = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // BOTH can view categories
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<CategoryResponse>> getAll(@RequestParam(required = false) String name) {
        List<CategoryResponse> categories;
        if (name != null && !name.isBlank()) {
            categories = service.searchByName(name);
        } else {
            categories = service.findAll();
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long id) {
        CategoryResponse category = service.findById(id);
        return ResponseEntity.ok(category);
    }

    // ADMIN only can update categories
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id,
                                                   @RequestBody UpdateCategoryRequest req) {
        CategoryResponse updated = service.update(id, req);
        return ResponseEntity.ok(updated);
    }

    // ADMIN only can delete categories
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // BOTH can view products in category
    @GetMapping("/{id}/products")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ProductDto>> getProductsInCategory(@PathVariable Long id) {
        List<ProductDto> products = service.getProductsInCategory(id);
        return ResponseEntity.ok(products);
    }
}
