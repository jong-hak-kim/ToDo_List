package todo.service;

import org.springframework.http.ResponseEntity;
import todo.dto.request.AddToDoRequestDto;
import todo.dto.request.CancelCompleteToDoRequestDto;
import todo.dto.request.CompleteToDoRequestDto;
import todo.dto.request.UpdateToDoRequestDto;
import todo.dto.response.ResponseDto;
import todo.util.UserToken;

public interface TodoService {

    public ResponseEntity<ResponseDto> addToDo(UserToken userToken, AddToDoRequestDto dto);

    public ResponseEntity<ResponseDto> updateToDo(UserToken userToken, UpdateToDoRequestDto dto);

    public ResponseEntity<ResponseDto> completeToDo(UserToken userToken, CompleteToDoRequestDto dto);

    public ResponseEntity<ResponseDto> cancelCompleteToDo(UserToken userToken, CancelCompleteToDoRequestDto dto);
}
