package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.spring.user.Role;
import tn.esprit.spring.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    List<User> findByUsernameContainingIgnoreCase(String username);
    List<User> findByRole(Role role);
    List<User> findByEnabled(boolean enabled);
    List<User> findByRoleAndEnabled(Role role, boolean enabled);
}