package todo.service;

import org.springframework.http.ResponseEntity;
import todo.dto.request.todo.*;
import todo.dto.response.ResponseDto;
import todo.util.UserToken;

public interface TodoService {

    ResponseEntity<ResponseDto> addToDo(UserToken userToken, AddToDoRequestDto dto);

    ResponseEntity<ResponseDto> modifyToDo(UserToken userToken, ModifyToDoRequestDto dto);

    ResponseEntity<ResponseDto> completeToDo(UserToken userToken, CompleteToDoRequestDto dto);

    ResponseEntity<ResponseDto> cancelCompleteToDo(UserToken userToken, CancelCompleteToDoRequestDto dto);

    ResponseEntity<ResponseDto> removeToDo(UserToken userToken, RemoveToDoRequestDto dto);

    ResponseEntity<ResponseDto> getToDoList(UserToken userToken);
}
