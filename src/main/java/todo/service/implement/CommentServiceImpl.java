package todo.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.common.constant.ResponseMessage;
import todo.dto.request.comment.AddCommentRequestDto;
import todo.dto.request.comment.GetCommentRequestDto;
import todo.dto.request.comment.ModifyCommentRequestDto;
import todo.dto.request.comment.RemoveCommentRequestDto;
import todo.dto.response.ResponseDto;
import todo.dto.response.comment.GetCommentListFilterDto;
import todo.dto.response.comment.GetCommentResponseDto;
import todo.dto.response.todo.GetToDoListFilterDto;
import todo.entity.Comment;
import todo.entity.ToDoList;
import todo.entity.User;
import todo.repository.CommentRepository;
import todo.repository.ToDoListRepository;
import todo.repository.UserRepository;
import todo.service.CommentService;
import todo.util.UserToken;

import java.util.List;

import static todo.common.constant.ErrorMessage.DATABASE_ERROR_LOG;

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

            Comment comment = new Comment(toDoList, user, dto.getParentCommentId(), dto.getContent());

            if (dto.getParentCommentId() != null && !commentRepository.existsCommentByCommentId(dto.getParentCommentId())) {
                return ResponseMessage.NOT_EXIST_COMMENT;
            }

            user.addComment(comment);
            toDoList.addComment(comment);

            commentRepository.save(comment);

            return ResponseMessage.SUCCESS;
        } catch (DataAccessException exception) {

            log.error(DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;

        }
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

            return ResponseMessage.SUCCESS;
        } catch (DataAccessException exception) {
            log.error(DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        }

    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> removeComment(UserToken userToken, RemoveCommentRequestDto dto) {
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

            Comment comment = commentRepository.findCommentByCommentId(dto.getCommentId());

            if (comment == null) {
                return ResponseMessage.NOT_EXIST_COMMENT;
            }

            user.removeComment(comment);
            comment.getToDoList().removeComment(comment);

            List<Comment> childComments = commentRepository.findCommentsByParentCommentId(dto.getCommentId());
            for (Comment child : childComments) {
                user.removeComment(child);
                child.getToDoList().removeComment(child);
            }

            commentRepository.deleteCommentsByParentCommentId(dto.getCommentId());
            commentRepository.delete(comment);
            return ResponseMessage.SUCCESS;

        } catch (DataAccessException exception) {
            log.error(DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        }
    }

    @Override
    public ResponseEntity<ResponseDto> getComment(UserToken userToken, Long listId) {
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

            ToDoList toDoList = toDoListRepository.findToDoListByListId(listId);

            if (toDoList == null) {
                return ResponseMessage.NOT_EXIST_TODO;
            }

            List<Comment> comments = commentRepository.findCommentsByToDoList(toDoList);

            List<GetCommentListFilterDto> commentResponseList = comments.stream().map(comment -> new GetCommentListFilterDto(comment.getCommentId(), comment.getParentCommentId(), comment.getUser().getEmail(), comment.getContent())).toList();

            return ResponseEntity.status(HttpStatus.OK).body(new GetCommentResponseDto(commentResponseList));

        } catch (DataAccessException exception) {
            log.error(DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        }
    }
}
