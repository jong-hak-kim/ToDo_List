package todo.service;

import org.springframework.http.ResponseEntity;
import todo.dto.request.AddCommentRequestDto;
import todo.dto.request.ModifyCommentRequestDto;
import todo.dto.response.ResponseDto;
import todo.util.UserToken;

public interface CommentService {
    public ResponseEntity<ResponseDto> addComment(UserToken userToken, AddCommentRequestDto dto);

    public ResponseEntity<ResponseDto> modifyComment(UserToken userToken, ModifyCommentRequestDto dto);
}
