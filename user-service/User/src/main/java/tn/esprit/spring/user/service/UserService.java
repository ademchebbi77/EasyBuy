package tn.esprit.spring.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.spring.repository.UserRepository;
import tn.esprit.spring.user.Role;
import tn.esprit.spring.user.User;
import tn.esprit.spring.user.client.OrderClient;
import tn.esprit.spring.user.client.ProductClient;
import tn.esprit.spring.user.dto.CreateUserRequest;
import tn.esprit.spring.user.dto.OrderDto;
import tn.esprit.spring.user.dto.ProductDto;
import tn.esprit.spring.user.dto.UpdateUserRequest;
import tn.esprit.spring.user.dto.UserEventDTO;
import tn.esprit.spring.user.dto.UserResponse;
import tn.esprit.spring.user.messaging.UserEventProducer;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final UserEventProducer userEventProducer;
    private final OrderClient orderClient;
    private final ProductClient productClient;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserResponse create(CreateUserRequest req) {

        if (repo.existsByEmail(req.email()))
            throw new RuntimeException("Email already used");

        if (repo.existsByUsername(req.username()))
            throw new RuntimeException("Username already used");

        Role role = (req.role() == null || req.role().isBlank())
                ? Role.USER
                : Role.valueOf(req.role().toUpperCase());

        User user = User.builder()
                .username(req.username())
                .email(req.email())
                .passwordHash(encoder.encode(req.password()))
                .role(role)
                .enabled(true)
                .build();

        user = repo.save(user);
        return mapToResponse(user);
    }

    public List<UserResponse> findAll() {
        return repo.findAll().stream().map(this::mapToResponse).toList();
    }

    public List<UserResponse> searchUsers(String username, String role, Boolean enabled) {
        List<User> users;
        
        // If all filters are null, return all users
        if ((username == null || username.isBlank()) && role == null && enabled == null) {
            users = repo.findAll();
        }
        // Filter by username only
        else if ((username != null && !username.isBlank()) && role == null && enabled == null) {
            users = repo.findByUsernameContainingIgnoreCase(username);
        }
        // Filter by role only
        else if ((username == null || username.isBlank()) && role != null && enabled == null) {
            Role roleEnum = Role.valueOf(role.toUpperCase());
            users = repo.findByRole(roleEnum);
        }
        // Filter by enabled only
        else if ((username == null || username.isBlank()) && role == null && enabled != null) {
            users = repo.findByEnabled(enabled);
        }
        // Filter by role AND enabled
        else if ((username == null || username.isBlank()) && role != null && enabled != null) {
            Role roleEnum = Role.valueOf(role.toUpperCase());
            users = repo.findByRoleAndEnabled(roleEnum, enabled);
        }
        // Filter by username with role and/or enabled (manual filtering)
        else {
            users = repo.findByUsernameContainingIgnoreCase(username);
            if (role != null) {
                Role roleEnum = Role.valueOf(role.toUpperCase());
                users = users.stream().filter(u -> u.getRole() == roleEnum).toList();
            }
            if (enabled != null) {
                users = users.stream().filter(u -> u.isEnabled() == enabled).toList();
            }
        }
        
        if (users.isEmpty()) {
            StringBuilder message = new StringBuilder("No users found");
            if (username != null && !username.isBlank()) message.append(" with username containing: ").append(username);
            if (role != null) message.append(" with role: ").append(role.toUpperCase());
            if (enabled != null) message.append(" with status: ").append(enabled ? "ENABLED" : "DISABLED");
            throw new RuntimeException(message.toString());
        }
        
        return users.stream().map(this::mapToResponse).toList();
    }

    public UserResponse findById(Long id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToResponse(user);
    }

    public UserResponse update(Long id, UpdateUserRequest req) {

        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (req.username() != null && !req.username().isBlank())
            user.setUsername(req.username());

        if (req.email() != null && !req.email().isBlank())
            user.setEmail(req.email());

        if (req.password() != null && !req.password().isBlank())
            user.setPasswordHash(encoder.encode(req.password()));

        if (req.role() != null && !req.role().isBlank())
            user.setRole(Role.valueOf(req.role().toUpperCase()));

        user = repo.save(user);
        return mapToResponse(user);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<OrderDto> getUserOrders(Long userId) {
        findById(userId);
        try {
            return orderClient.getOrdersByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException("No orders found for user id: " + userId);
        }
    }

    public List<ProductDto> getUserProducts(Long userId) {
        findById(userId);
        try {
            return productClient.getProductsByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException("No products found for user id: " + userId);
        }
    }

    public UserResponse setEnabled(Long id, boolean enabled) {        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean wasEnabled = user.isEnabled();
        user.setEnabled(enabled);
        user = repo.save(user);

        // Send event to RabbitMQ when user is disabled/enabled
        if (wasEnabled != enabled) {
            String eventType = enabled ? "ENABLED" : "DISABLED";
            UserEventDTO eventDTO = new UserEventDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    eventType,
                    user.getRole().name(),
                    user.isEnabled()
            );
            userEventProducer.sendUserEvent(eventDTO);
        }

        return mapToResponse(user);
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.isEnabled()
        );
    }
}