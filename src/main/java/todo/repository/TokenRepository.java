package todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import todo.entity.VerificationToken;

@Repository
public interface TokenRepository extends JpaRepository<VerificationToken, Long> {
    boolean existsByEmail(String email);

    VerificationToken findByToken(String token);

    VerificationToken findByEmail(String email);
}
