package todo.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.common.constant.ResponseMessage;
import todo.dto.request.todo.*;
import todo.dto.response.ResponseDto;
import todo.dto.response.todo.GetToDoListFilterDto;
import todo.dto.response.todo.GetToDoListResponseDto;
import todo.entity.ToDoList;
import todo.entity.User;
import todo.repository.ToDoListRepository;
import todo.repository.UserRepository;
import todo.service.TodoService;
import todo.util.UserToken;

import java.util.List;
import java.util.Optional;

import static todo.common.constant.ErrorMessage.DATABASE_ERROR_LOG;

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

            if (user == null) {
                return ResponseMessage.NOT_EXIST_USER;
            }

            if (!user.isActive()) {
                return ResponseMessage.IS_NOT_ACTIVATE;
            }

            ToDoList todoList = new ToDoList(user, dto.getTitle(), dto.getContent(), dto.getStartDate().atTime(23, 59, 59), dto.getPriority(), dto.getRepeatEndDate().atTime(23, 59, 59));

            user.addToDoList(todoList);

            toDoListRepository.save(todoList);
            return ResponseMessage.SUCCESS;

        } catch (DataAccessException exception) {
            log.error(DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        }

    }

    @Override
    public ResponseEntity<ResponseDto> modifyToDo(UserToken userToken, ModifyToDoRequestDto dto) {
        try {
            if (userToken == null) {
                return ResponseMessage.TOKEN_NOT_FOUND;
            }

            User user = userRepository.findUserByEmail(userToken.getEmail());

            if (user == null) {
                return ResponseMessage.NOT_EXIST_USER;
            }

            if (!user.isActive()) {
                return ResponseMessage.IS_NOT_ACTIVATE;
            }

            ToDoList toDoList = toDoListRepository.findToDoListByListId(dto.getListId());
            updateNonNullField(dto, toDoList);

            toDoListRepository.save(toDoList);
            return ResponseMessage.SUCCESS;

        } catch (DataAccessException exception) {
            log.error(DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        }


    }

    @Override
    public ResponseEntity<ResponseDto> completeToDo(UserToken userToken, CompleteToDoRequestDto dto) {
        try {
            if (userToken == null) {
                return ResponseMessage.TOKEN_NOT_FOUND;
            }

            User user = userRepository.findUserByEmail(userToken.getEmail());

            if (user == null) {
                return ResponseMessage.NOT_EXIST_USER;
            }

            if (!user.isActive()) {
                return ResponseMessage.IS_NOT_ACTIVATE;
            }

            ToDoList toDoList = toDoListRepository.findToDoListByListId(dto.getListId());

            if (toDoList == null) {
                return ResponseMessage.NOT_EXIST_TODO;
            }

            toDoList.setCompletionStatus(true);

            toDoListRepository.save(toDoList);

            return ResponseMessage.SUCCESS;
        } catch (DataAccessException exception) {
            log.error(DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        }

    }

    @Override
    public ResponseEntity<ResponseDto> cancelCompleteToDo(UserToken userToken, CancelCompleteToDoRequestDto dto) {
        try {
            if (userToken == null) {
                return ResponseMessage.TOKEN_NOT_FOUND;
            }

            User user = userRepository.findUserByEmail(userToken.getEmail());

            if (user == null) {
                return ResponseMessage.NOT_EXIST_USER;
            }

            if (!user.isActive()) {
                return ResponseMessage.IS_NOT_ACTIVATE;
            }

            ToDoList toDoList = toDoListRepository.findToDoListByListId(dto.getListId());

            if (toDoList == null) {
                return ResponseMessage.NOT_EXIST_TODO;
            }

            toDoList.setCompletionStatus(false);

            toDoListRepository.save(toDoList);

            return ResponseMessage.SUCCESS;
        } catch (DataAccessException exception) {
            log.error(DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        }

    }

    @Transactional
    @Override
    public ResponseEntity<ResponseDto> removeToDo(UserToken userToken, RemoveToDoRequestDto dto) {
        try {
            if (userToken == null) {
                return ResponseMessage.TOKEN_NOT_FOUND;
            }

            User user = userRepository.findUserByEmail(userToken.getEmail());

            if (user == null) {
                return ResponseMessage.NOT_EXIST_USER;
            }

            if (!user.isActive()) {
                return ResponseMessage.IS_NOT_ACTIVATE;
            }

            ToDoList toDoList = toDoListRepository.findToDoListByListId(dto.getListId());

            if (toDoList == null) {
                return ResponseMessage.NOT_EXIST_TODO;
            }

            user.removeToDoList(toDoList);

            toDoListRepository.delete(toDoList);
            return ResponseMessage.SUCCESS;
        } catch (DataAccessException exception) {
            log.error(DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        }

    }

    @Override
    public ResponseEntity<ResponseDto> getToDoList(UserToken userToken) {
        try {
            if (userToken == null) {
                return ResponseMessage.TOKEN_NOT_FOUND;
            }

            User user = userRepository.findUserByEmail(userToken.getEmail());

            if (user == null) {
                return ResponseMessage.NOT_EXIST_USER;
            }

            if (!user.isActive()) {
                return ResponseMessage.IS_NOT_ACTIVATE;
            }

            List<ToDoList> toDoLists = toDoListRepository.findToDoListsByUser(user);

            List<GetToDoListFilterDto> todoResponseList = toDoLists.stream()
                    .map(toDoList -> new GetToDoListFilterDto(toDoList.getListId(), toDoList.getTitle(), toDoList.isCompletionStatus()))
                    .toList();

            return ResponseEntity.status(HttpStatus.OK).body(new GetToDoListResponseDto(todoResponseList));

        } catch (DataAccessException exception) {
            log.error(DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        }
    }

    private void updateNonNullField(ModifyToDoRequestDto dto, ToDoList toDoList) {
        Optional.ofNullable(dto.getTitle()).ifPresent(toDoList::setTitle);
        Optional.ofNullable(dto.getContent()).ifPresent(toDoList::setContent);
        Optional.ofNullable(dto.getPriority()).ifPresent(toDoList::setPriority);
        Optional.ofNullable(dto.getStartDate()).ifPresent(startDate -> toDoList.setStartDate(startDate.atTime(23, 59, 59)));
        Optional.ofNullable(dto.getRepeatEndDate()).ifPresent(repeatEndDate -> toDoList.setRepeatEndDate(repeatEndDate.atTime(23, 59, 59)));
    }
}
