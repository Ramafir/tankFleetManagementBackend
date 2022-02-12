package pl.daniel.kolban.tankfleetmanagement.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import pl.daniel.kolban.tankfleetmanagement.tank.Tank;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String userId;
    @NotBlank(message="*Must give a full name")
    @Size(min=2, max=255)
    private String fullName;
    private String country;
    @NotBlank(message="*Must give a username")
    @Size(min=2, max=255)
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @NotBlank
    @Email(message="*Must be a valid email address")
    @Size(min=1, max=255)
    private String email;
    private LocalDateTime joinDate;
    private String role;
    private String[] authorities;
    private boolean isActive;
    private boolean isNotLocked;
    private boolean hasAtomicButton;
    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE)
    private List<Tank> tanks = new ArrayList<>();


    public User(Long id, String userId, String fullName, String country, String username, String password, String email, LocalDateTime joinDate, String role, String[] authorities, boolean isActive, boolean isNotLocked, boolean hasAtomicButton, List<Tank> tanks) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.country = country;
        this.username = username;
        this.password = password;
        this.email = email;
        this.joinDate = joinDate;
        this.role = role;
        this.authorities = authorities;
        this.isActive = isActive;
        this.isNotLocked = isNotLocked;
        this.hasAtomicButton = hasAtomicButton;
        this.tanks = tanks;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String[] getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isNotLocked() {
        return isNotLocked;
    }

    public void setNotLocked(boolean notLocked) {
        isNotLocked = notLocked;
    }

    public boolean isHasAtomicButton() {
        return hasAtomicButton;
    }

    public void setHasAtomicButton(boolean hasAtomicButton) {
        this.hasAtomicButton = hasAtomicButton;
    }

    public List<Tank> getTanks() {
        return tanks;
    }

    public void setTanks(List<Tank> tanks) {
        this.tanks = tanks;
    }
}
