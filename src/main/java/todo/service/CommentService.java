package todo.service;

import org.springframework.http.ResponseEntity;
import todo.dto.request.AddCommentRequestDto;
import todo.dto.request.ModifyCommentRequestDto;
import todo.dto.request.RemoveCommentRequestDto;
import todo.dto.response.ResponseDto;
import todo.util.UserToken;

public interface CommentService {
    ResponseEntity<ResponseDto> addComment(UserToken userToken, AddCommentRequestDto dto);

    ResponseEntity<ResponseDto> modifyComment(UserToken userToken, ModifyCommentRequestDto dto);

    ResponseEntity<ResponseDto> removeComment(UserToken userToken, RemoveCommentRequestDto dto);
}
