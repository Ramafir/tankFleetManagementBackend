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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
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
    @Size(min=2, max=20)
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Range(min = 6)
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
}
