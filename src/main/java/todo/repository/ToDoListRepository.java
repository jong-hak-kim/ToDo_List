package todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todo.entity.ToDoList;
import todo.entity.User;

import java.util.List;

public interface ToDoListRepository extends JpaRepository<ToDoList, Long> {

    ToDoList findToDoListByListId(Long listId);

    List<ToDoList> findToDoListsByUser(User user);
}
