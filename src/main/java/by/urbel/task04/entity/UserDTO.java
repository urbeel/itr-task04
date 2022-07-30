package by.urbel.task04.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.Instant;

@Setter
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class UserDTO {
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private Long id;
    @Column(nullable = false)
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 1,max = 50,message = "Name should be between 1 and 50 characters")
    private String name;
    @Column(nullable = false, unique = true)
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email should not be empty")
    private String email;
    @Column(nullable = false, unique = true)
    @NotEmpty(message = "Password should not be empty")
    private String passwordHash;
    @Column()
    private Instant lastLoginTime;
    @Column(nullable = false, updatable = false)
    private Instant registrationTime;
    @Column(nullable = false)
    private Boolean isActive;

    @PrePersist
    public void prePersist() {
        registrationTime = Instant.now();
        isActive = true;
    }
}
