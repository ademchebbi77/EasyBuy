package tn.esprit.spring.backend.order.dto;

import java.io.Serializable;
import java.time.Instant;

public class UserEventDTO implements Serializable {
    private Long userId;
    private String username;
    private String email;
    private String eventType; // CREATED, UPDATED, DISABLED, ENABLED
    private String role;
    private boolean enabled;
    private Instant timestamp;

    public UserEventDTO() {
    }

    public UserEventDTO(Long userId, String username, String email, String eventType, 
                        String role, boolean enabled) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.eventType = eventType;
        this.role = role;
        this.enabled = enabled;
        this.timestamp = Instant.now();
    }

    // Getters and setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}