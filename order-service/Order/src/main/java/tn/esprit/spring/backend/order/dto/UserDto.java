package tn.esprit.spring.backend.order.dto;

public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String role;
    private Boolean enabled;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    public boolean isEnabled() {
        return enabled == null ? false : enabled;
    }
}