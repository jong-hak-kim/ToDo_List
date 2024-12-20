package todo.service;

import org.springframework.http.ResponseEntity;
import todo.dto.request.todo.*;
import todo.dto.response.ResponseDto;
import todo.util.UserToken;

public interface TodoService {

    ResponseEntity<ResponseDto> addToDo(UserToken userToken, AddToDoRequestDto dto);

    ResponseEntity<ResponseDto> modifyToDo(UserToken userToken, Long listId, ModifyToDoRequestDto dto);

    ResponseEntity<ResponseDto> completeToDo(UserToken userToken, CompleteToDoRequestDto dto);

    ResponseEntity<ResponseDto> cancelCompleteToDo(UserToken userToken, CancelCompleteToDoRequestDto dto);

    ResponseEntity<ResponseDto> removeToDo(UserToken userToken, Long listId);

    ResponseEntity<ResponseDto> getToDoList(UserToken userToken, String selectedDate);

    ResponseEntity<ResponseDto> getOneToDoList(UserToken userToken, Long listId);

    ResponseEntity<ResponseDto> getUser(UserToken userToken);

    ResponseEntity<ResponseDto> getOtherToDoList(UserToken userToken, String selectedDate, String email);
}
