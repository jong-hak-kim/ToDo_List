package todo.service;

import org.springframework.http.ResponseEntity;
import todo.dto.request.comment.AddCommentRequestDto;
import todo.dto.request.comment.ModifyCommentRequestDto;
import todo.dto.response.ResponseDto;
import todo.util.UserToken;

public interface CommentService {
    ResponseEntity<ResponseDto> addComment(UserToken userToken, AddCommentRequestDto dto);

    ResponseEntity<ResponseDto> modifyComment(UserToken userToken, Long commentId, ModifyCommentRequestDto dto);

    ResponseEntity<ResponseDto> removeComment(UserToken userToken, Long commentId);

    ResponseEntity<ResponseDto> getComment(UserToken userToken, Long listId);
}
