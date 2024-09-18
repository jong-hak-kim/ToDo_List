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
import todo.dto.request.CancelCompleteToDoRequestDto;
import todo.dto.request.CompleteToDoRequestDto;
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
    public ResponseEntity<ResponseDto> addToDo(
            @AuthenticationPrincipal UserToken userToken,
            @Valid @RequestBody AddToDoRequestDto addToDoRequestDto) {
        return todoService.addToDo(userToken, addToDoRequestDto);
    }

    @PostMapping("/todo/update")
    @Operation(summary = "투두리스트 수정", description = "투두 리스트 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "투두 리스트 작성 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한이 없는 유저", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> updateToDo(
            @AuthenticationPrincipal UserToken userToken,
            @Valid @RequestBody UpdateToDoRequestDto updateToDoRequestDto) {
        return todoService.updateToDo(userToken, updateToDoRequestDto);
    }

    @PostMapping("/todo/complete")
    @Operation(summary = "목표 완료", description = "목표 완료 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "투두 리스트 작성 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한이 없는 유저", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> completeToDo(
            @AuthenticationPrincipal UserToken userToken,
            @Valid @RequestBody CompleteToDoRequestDto completeToDoDto){
        return todoService.completeToDo(userToken, completeToDoDto);
    }

    @PostMapping("/todo/cancel")
    @Operation(summary = "목표 완료", description = "목표 완료 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "투두 리스트 작성 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한이 없는 유저", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> cancelCompleteToDo(
            @AuthenticationPrincipal UserToken userToken,
            @Valid @RequestBody CancelCompleteToDoRequestDto cancelCompleteToDoRequestDto){
        return todoService.cancelCompleteToDo(userToken, cancelCompleteToDoRequestDto);
    }
}
