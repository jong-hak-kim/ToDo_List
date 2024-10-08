package todo.service;

import org.springframework.http.ResponseEntity;
import todo.dto.request.comment.AddCommentRequestDto;
import todo.dto.request.comment.GetCommentRequestDto;
import todo.dto.request.comment.ModifyCommentRequestDto;
import todo.dto.request.comment.RemoveCommentRequestDto;
import todo.dto.response.ResponseDto;
import todo.util.UserToken;

public interface CommentService {
    ResponseEntity<ResponseDto> addComment(UserToken userToken, AddCommentRequestDto dto);

    ResponseEntity<ResponseDto> modifyComment(UserToken userToken, ModifyCommentRequestDto dto);

    ResponseEntity<ResponseDto> removeComment(UserToken userToken, RemoveCommentRequestDto dto);

    ResponseEntity<ResponseDto> getComment(UserToken userToken, Long listId);
}
