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
import todo.dto.request.UpdateToDoRequestDto;
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
            @ApiResponse(responseCode = "200", description = "투두 리스트 작성 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한이 없는 유저", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> addTodo(
            @AuthenticationPrincipal UserToken userToken,
            @Valid @RequestBody AddToDoRequestDto addToDoRequestDto) {
        return todoService.AddTodo(userToken, addToDoRequestDto);
    }

    @PostMapping("/todo/update")
    @Operation(summary = "투두리스트 수정", description = "투두 리스트 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "투두 리스트 작성 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한이 없는 유저", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> updateTodo(
            @AuthenticationPrincipal UserToken userToken,
            @Valid @RequestBody UpdateToDoRequestDto updateToDoRequestDto) {
        return todoService.UpdateTodo(userToken, updateToDoRequestDto);
    }
}
