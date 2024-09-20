package todo.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import todo.common.constant.ResponseMessage;
import todo.dto.request.AddCommentRequestDto;
import todo.dto.request.ModifyCommentRequestDto;
import todo.dto.response.ResponseDto;
import todo.entity.Comment;
import todo.entity.ToDoList;
import todo.entity.User;
import todo.repository.CommentRepository;
import todo.repository.ToDoListRepository;
import todo.repository.UserRepository;
import todo.service.CommentService;
import todo.util.UserToken;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final ToDoListRepository toDoListRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, ToDoListRepository toDoListRepository) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.toDoListRepository = toDoListRepository;
    }

    @Override
    public ResponseEntity<ResponseDto> addComment(UserToken userToken, AddCommentRequestDto dto) {

        try {

            if (userToken == null) {
                return ResponseMessage.TOKEN_NOT_FOUND;
            }

            User user = userRepository.findUserByEmail(userToken.getEmail());

            if (user == null) {
                return ResponseMessage.NOT_EXIST_USER;
            }

            if (!user.isActive()) {
                return ResponseMessage.IS_NOT_ACTIVATE;
            }

            ToDoList toDoList = toDoListRepository.findToDoListByListId(dto.getToDoListId());

            if (toDoList == null) {
                return ResponseMessage.NOT_EXIST_TODO;
            }

            Comment comment = new Comment(toDoList, user, dto.getContent());

            user.addComment(comment);
            toDoList.addComment(comment);

            commentRepository.save(comment);

        } catch (DataAccessException exception) {

            log.error("Database error occurred while checking user details", exception);
            return ResponseMessage.DATABASE_ERROR;

        }
        return ResponseMessage.SUCCESS;
    }

    @Override
    public ResponseEntity<ResponseDto> modifyComment(UserToken userToken, ModifyCommentRequestDto dto) {
        try {

            if (userToken == null) {
                return ResponseMessage.TOKEN_NOT_FOUND;
            }

            User user = userRepository.findUserByEmail(userToken.getEmail());

            if (user == null) {
                return ResponseMessage.NOT_EXIST_USER;
            }

            if (!user.isActive()) {
                return ResponseMessage.IS_NOT_ACTIVATE;
            }

            ToDoList toDoList = toDoListRepository.findToDoListByListId(dto.getToDoListId());

            if (toDoList == null) {
                return ResponseMessage.NOT_EXIST_TODO;
            }

            Comment comment = commentRepository.findCommentByCommentId(dto.getCommentId());
            comment.setContent(dto.getContent());

            commentRepository.save(comment);

        } catch (DataAccessException exception) {
            log.error("Database error occurred while checking user details", exception);
            return ResponseMessage.DATABASE_ERROR;
        }

        return ResponseMessage.SUCCESS;
    }
}
