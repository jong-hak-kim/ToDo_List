package todo.service;

import org.springframework.http.ResponseEntity;
import todo.dto.request.*;
import todo.dto.response.ResponseDto;
import todo.util.UserToken;

public interface TodoService {

    public ResponseEntity<ResponseDto> addToDo(UserToken userToken, AddToDoRequestDto dto);

    public ResponseEntity<ResponseDto> modifyToDo(UserToken userToken, ModifyToDoRequestDto dto);

    public ResponseEntity<ResponseDto> completeToDo(UserToken userToken, CompleteToDoRequestDto dto);

    public ResponseEntity<ResponseDto> cancelCompleteToDo(UserToken userToken, CancelCompleteToDoRequestDto dto);

    public ResponseEntity<ResponseDto> removeToDo(UserToken userToken, RemoveToDoRequestDto dto);
}
