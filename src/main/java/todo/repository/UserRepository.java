package todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todo.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    User findUserByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.toDoLists WHERE u.email = :email")
    User findUserWithToDoLists(@Param("email") String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String email);

    List<User> findAllByIsActiveIsFalseAndDeactivationDateBefore(LocalDateTime now);
}
