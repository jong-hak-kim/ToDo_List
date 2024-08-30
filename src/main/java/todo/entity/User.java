package todo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
public class User {

    @Id
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "signup_date", nullable = false)
    private LocalDateTime signupDate;

    @Column(nullable = false)
    private String role;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "deactivation_date")
    private LocalDateTime deactivationDate;

}
