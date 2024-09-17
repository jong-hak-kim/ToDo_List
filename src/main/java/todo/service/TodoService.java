package todo.service;

import org.springframework.http.ResponseEntity;
import todo.dto.request.AddToDoRequestDto;
import todo.dto.request.UpdateToDoRequestDto;
import todo.dto.response.ResponseDto;
import todo.util.UserToken;

public interface TodoService {

    public ResponseEntity<ResponseDto> AddTodo(UserToken userToken, AddToDoRequestDto dto);

    public ResponseEntity<ResponseDto> UpdateTodo(UserToken userToken, UpdateToDoRequestDto dto);
}
