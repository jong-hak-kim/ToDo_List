package todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import todo.entity.VerificationToken;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<VerificationToken, Long> {
    boolean existsByEmail(String email);

    VerificationToken findByToken(String token);

    Optional<VerificationToken> findByEmail(String email);
}
