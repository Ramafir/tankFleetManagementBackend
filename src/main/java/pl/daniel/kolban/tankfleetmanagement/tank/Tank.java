package pl.daniel.kolban.tankfleetmanagement.tank;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
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
    @Range(min = 1900, max = 2022)
    private Integer yearOfProduction;
    private Date countryEntryDate;
    @Range(min = 1)
    private Integer mileage;
    @Range(min = 0)
    private Integer ammunition;
    @Range(min = 0)
    private Integer frontArmor;
    @Range(min = 0)
    private Integer sideArmor;
    @Range(min = 0)
    private Integer backArmor;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    @ManyToOne
    @JsonBackReference
    private User user;
}


