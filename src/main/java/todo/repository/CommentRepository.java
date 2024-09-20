package todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todo.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findCommentByCommentId(Long commentId);

}
