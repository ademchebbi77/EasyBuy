package tn.esprit.spring.user.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.user.dto.CreateUserRequest;
import tn.esprit.spring.user.dto.OrderDto;
import tn.esprit.spring.user.dto.ProductDto;
import tn.esprit.spring.user.dto.UpdateUserRequest;
import tn.esprit.spring.user.dto.UserResponse;
import tn.esprit.spring.user.service.UserService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // ADMIN only can create users
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest req) {
        UserResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/api/users/" + created.id())).body(created);
    }

    // ADMIN only can view all users
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> all(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean enabled) {
        return service.searchUsers(username, role, enabled);
    }

    // BOTH can view a user by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public UserResponse one(@PathVariable Long id) {
        return service.findById(id);
    }

    // ADMIN only can update users
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse update(@PathVariable Long id,
                               @Valid @RequestBody UpdateUserRequest req) {
        return service.update(id, req);
    }

    // ADMIN only can delete users
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ADMIN only can enable/disable users
    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse enable(@PathVariable Long id) {
        return service.setEnabled(id, true);
    }

    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse disable(@PathVariable Long id) {
        return service.setEnabled(id, false);
    }

    // BOTH can view user orders
    @GetMapping("/{id}/orders")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<OrderDto> getUserOrders(@PathVariable Long id) {
        return service.getUserOrders(id);
    }

    // BOTH can view user products
    @GetMapping("/{id}/products")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<ProductDto> getUserProducts(@PathVariable Long id) {
        return service.getUserProducts(id);
    }
}