package pl.daniel.kolban.tankfleetmanagement.tank;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import pl.daniel.kolban.tankfleetmanagement.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Tank implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String sideNumber;
    private String producer;
    private String model;
    private String currentModification;
    private Integer yearOfProduction;
    private Date countryEntryDate;
    private Integer mileage;
    private Integer ammunition;
    private Integer frontArmor;
    private Integer sideArmor;
    private Integer backArmor;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;
}


