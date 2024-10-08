package todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todo.entity.Comment;
import todo.entity.ToDoList;
import todo.entity.User;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findCommentByCommentId(Long commentId);

    boolean existsCommentByCommentId(Long parentId);

    List<Comment> findCommentsByParentCommentId(Long parentId);

    List<Comment> findCommentsByUser(User user);

    List<Comment> findCommentsByToDoList(ToDoList toDoList);

    void deleteCommentsByParentCommentId(Long parentCommentId);

}
