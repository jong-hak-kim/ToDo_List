package todo.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import todo.common.constant.ResponseMessage;
import todo.dto.request.AddToDoRequestDto;
import todo.dto.request.CompleteToDoRequestDto;
import todo.dto.request.UpdateToDoRequestDto;
import todo.dto.response.ResponseDto;
import todo.entity.ToDoList;
import todo.entity.User;
import todo.repository.ToDoListRepository;
import todo.repository.UserRepository;
import todo.service.TodoService;
import todo.util.UserToken;

import java.util.Optional;

@Service
@Slf4j
public class TodoServiceImpl implements TodoService {

    private final ToDoListRepository toDoListRepository;
    private final UserRepository userRepository;

    public TodoServiceImpl(ToDoListRepository toDoListRepository, UserRepository userRepository) {
        this.toDoListRepository = toDoListRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<ResponseDto> addToDo(UserToken userToken, AddToDoRequestDto dto) {
        try {
            if (userToken == null) {
                return ResponseMessage.TOKEN_NOT_FOUND;
            }

            User user = userRepository.findUserByEmail(userToken.getEmail());

            if(user == null){
                return ResponseMessage.NOT_EXIST_USER;
            }

            if (!user.isActive()) {
                return ResponseMessage.IS_NOT_ACTIVATE;
            }

            ToDoList todoList = new ToDoList(user, dto.getTitle(), dto.getContent(), dto.getDueDate(), dto.getPriority(), dto.getRepeatInterval());

            toDoListRepository.save(todoList);

        } catch (DataAccessException exception) {
            log.error("Database error occurred while checking user details", exception);
            return ResponseMessage.DATABASE_ERROR;
        }

        return ResponseMessage.SUCCESS;
    }

    @Override
    public ResponseEntity<ResponseDto> updateToDo(UserToken userToken, UpdateToDoRequestDto dto) {
        try {
            if (userToken == null) {
                return ResponseMessage.TOKEN_NOT_FOUND;
            }

            User user = userRepository.findUserByEmail(userToken.getEmail());

            if(user == null){
                return ResponseMessage.NOT_EXIST_USER;
            }

            if (!user.isActive()) {
                return ResponseMessage.IS_NOT_ACTIVATE;
            }

            ToDoList toDoList = toDoListRepository.findToDoListByListId(dto.getListId());
            updateNonNullField(dto, toDoList);

            toDoListRepository.save(toDoList);

        } catch (DataAccessException exception) {
            log.error("Database error occurred while checking user details", exception);
            return ResponseMessage.DATABASE_ERROR;
        }


        return ResponseMessage.SUCCESS;
    }

    @Override
    public ResponseEntity<ResponseDto> completeToDo(UserToken userToken, CompleteToDoRequestDto dto) {
        try {
            if (userToken == null) {
                return ResponseMessage.TOKEN_NOT_FOUND;
            }

            User user = userRepository.findUserByEmail(userToken.getEmail());

            if(user == null){
                return ResponseMessage.NOT_EXIST_USER;
            }

            if (!user.isActive()) {
                return ResponseMessage.IS_NOT_ACTIVATE;
            }

            ToDoList toDoList = toDoListRepository.findToDoListByListId(dto.getListId());
            toDoList.setCompletionStatus(true);

            toDoListRepository.save(toDoList);

        }catch (DataAccessException exception) {
            log.error("Database error occurred while checking user details", exception);
            return ResponseMessage.DATABASE_ERROR;
        }

        return ResponseMessage.SUCCESS;
    }


    private void updateNonNullField(UpdateToDoRequestDto dto, ToDoList toDoList) {
        Optional.ofNullable(dto.getTitle()).ifPresent(toDoList::setTitle);
        Optional.ofNullable(dto.getContent()).ifPresent(toDoList::setContent);
        Optional.ofNullable(dto.getPriority()).ifPresent(toDoList::setPriority);
        Optional.ofNullable(dto.getDueDate()).ifPresent(toDoList::setDueDate);
        Optional.ofNullable(dto.getRepeatInterval()).ifPresent(toDoList::setRepeatInterval);
    }
}
