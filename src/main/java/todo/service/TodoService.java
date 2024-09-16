package todo.service;

import org.springframework.http.ResponseEntity;
import todo.dto.request.AddToDoRequestDto;
import todo.dto.response.ResponseDto;
import todo.util.UserToken;

public interface TodoService {

    public ResponseEntity<ResponseDto> AddTodo(UserToken userToken, AddToDoRequestDto dto);
}
