package todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todo.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    User findUserByEmail(String email);
}
