package todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todo.entity.ToDoList;
import todo.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface ToDoListRepository extends JpaRepository<ToDoList, Long> {

    ToDoList findToDoListByListId(Long listId);

    List<ToDoList> findToDoListsByUser(User user);

    List<ToDoList> findToDoListsByUserAndDate(User user, LocalDate date);

}
