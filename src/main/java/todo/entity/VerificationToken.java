package todo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private String email;
    private LocalDateTime expireDate;

    public VerificationToken(String token, String email) {
        this.token = token;
        this.email = email;
        this.expireDate = LocalDateTime.now().plusMinutes(3);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireDate);
    }
}
