package todo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import todo.dto.request.todo.*;
import todo.dto.response.ResponseDto;
import todo.service.TodoService;
import todo.util.UserToken;

@RestController
public class ToDoController {

    private final TodoService todoService;

    public ToDoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/todo")
    @Operation(summary = "할 일 목록 조회", description = "할 일 목록 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할 일 목록 조회 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한이 없는 유저", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> getToDoList(
            @AuthenticationPrincipal UserToken userToken,
            @RequestParam("date") String selectedDate
    ) {
        return todoService.getToDoList(userToken, selectedDate);
    }

    @GetMapping("/todo/other")
    @Operation(summary = "다른 유저 할 일 목록 조회", description = "할 일 목록 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할 일 목록 조회 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한이 없는 유저", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> getOtherToDoList(
            @AuthenticationPrincipal UserToken userToken,
            @RequestParam("date") String selectedDate,
            @RequestParam("email") String email
    ) {
        return todoService.getOtherToDoList(userToken, selectedDate, email);
    }

    @GetMapping("/todo/{listId}")
    @Operation(summary = "할 일 단일 조회", description = "할 일 단일 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할 일 조회 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한이 없는 유저", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> getOneToDoList(
            @AuthenticationPrincipal UserToken userToken,
            @PathVariable("listId") Long listId
    ) {
        return todoService.getOneToDoList(userToken, listId);
    }

    @PostMapping("/todo")
    @Operation(summary = "할 일 작성", description = "할 일 작성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할 일 작성 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한이 없는 유저", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> addToDo(
            @AuthenticationPrincipal UserToken userToken,
            @Valid @RequestBody AddToDoRequestDto addToDoRequestDto) {
        return todoService.addToDo(userToken, addToDoRequestDto);
    }

    @PostMapping("/todo/{listId}/update")
    @Operation(summary = "할 일 수정", description = "할 일 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할 일 작성 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한이 없는 유저", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> modifyToDo(
            @AuthenticationPrincipal UserToken userToken,
            @PathVariable("listId") Long listId,
            @Valid @RequestBody ModifyToDoRequestDto modifyToDoRequestDto) {
        return todoService.modifyToDo(userToken, listId, modifyToDoRequestDto);
    }

    @PostMapping("/todo/complete")
    @Operation(summary = "할 일 상태 완료 처리", description = "할 일 상태 완료 처리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할 일 상태 완료 처리", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한이 없는 유저", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> completeToDo(
            @AuthenticationPrincipal UserToken userToken,
            @Valid @RequestBody CompleteToDoRequestDto completeToDoDto) {
        return todoService.completeToDo(userToken, completeToDoDto);
    }

    @PostMapping("/todo/cancel")
    @Operation(summary = "할 일 상태 완료 해제", description = "할 일 상태 완료 해제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할 일 취소 처리", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한이 없는 유저", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> cancelCompleteToDo(
            @AuthenticationPrincipal UserToken userToken,
            @Valid @RequestBody CancelCompleteToDoRequestDto cancelCompleteToDoRequestDto) {
        return todoService.cancelCompleteToDo(userToken, cancelCompleteToDoRequestDto);
    }

    @PostMapping("/todo/{listId}/remove")
    @Operation(summary = "할 일 삭제", description = "할 일 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한이 없는 유저", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> removeToDo(
            @AuthenticationPrincipal UserToken userToken,
            @PathVariable("listId") Long listId
    ) {
        return todoService.removeToDo(userToken, listId);
    }

    @GetMapping("/todo/search/users")
    @Operation(summary = "유저 조회", description = "유저 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 조회 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한 없음", content = @Content),
            @ApiResponse(responseCode = "500", description = "DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> getUser(
            @AuthenticationPrincipal UserToken userToken) {
        return todoService.getUser(userToken);
    }

}
