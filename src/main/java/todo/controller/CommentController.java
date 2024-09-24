package todo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import todo.dto.request.comment.AddCommentRequestDto;
import todo.dto.request.comment.ModifyCommentRequestDto;
import todo.dto.request.comment.RemoveCommentRequestDto;
import todo.dto.response.ResponseDto;
import todo.service.CommentService;
import todo.util.UserToken;

@RestController
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/todo/comment")
    @Operation(summary = "댓글 작성", description = "댓글 작성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 작성 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한이 없는 유저", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> addComment(
            @AuthenticationPrincipal UserToken userToken,
            @Valid @RequestBody AddCommentRequestDto addCommentRequestDto) {
        return commentService.addComment(userToken, addCommentRequestDto);
    }

    @PostMapping("/todo/comment/modify")
    @Operation(summary = "댓글 수정", description = "댓글 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 수정 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한이 없는 유저", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> modifyComment(
            @AuthenticationPrincipal UserToken userToken,
            @Valid @RequestBody ModifyCommentRequestDto modifyCommentRequestDto) {
        return commentService.modifyComment(userToken, modifyCommentRequestDto);
    }

    @PostMapping("/todo/comment/remove")
    @Operation(summary = "댓글 삭제", description = "댓글 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 삭제 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한이 없는 유저", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> removeComment(
            @AuthenticationPrincipal UserToken userToken,
            @Valid @RequestBody RemoveCommentRequestDto removeCommentRequestDto) {
        return commentService.removeComment(userToken, removeCommentRequestDto);
    }
}
