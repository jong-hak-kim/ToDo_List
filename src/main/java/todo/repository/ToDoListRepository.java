package todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todo.entity.ToDoList;

public interface ToDoListRepository extends JpaRepository<ToDoList, Long> {
}
