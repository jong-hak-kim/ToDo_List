package todo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import todo.dto.request.AddToDoRequestDto;
import todo.dto.response.ResponseDto;
import todo.service.TodoService;
import todo.util.UserToken;

@RestController
public class ToDoController {

    private final TodoService todoService;

    public ToDoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/todo")
    @Operation(summary = "투두리스트 작성", description = "투두 리스트 작성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "투두 리스트 작성 완료", content = @Content)
    })
    public ResponseEntity<ResponseDto> addTodo(
            @AuthenticationPrincipal UserToken userToken,
            @Valid @RequestBody AddToDoRequestDto addToDoRequestDtod) {
        return todoService.AddTodo(userToken, addToDoRequestDtod);
    }
}
