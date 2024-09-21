package todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todo.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findCommentByCommentId(Long commentId);

    boolean existsCommentByCommentId(Long parentId);

    List<Comment> findCommentsByParentCommentId(Long parentId);

    void deleteCommentsByParentCommentId(Long parentCommentId);

}
