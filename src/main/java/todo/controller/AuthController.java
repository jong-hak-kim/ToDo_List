package todo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todo.dto.request.SignInRequestDto;
import todo.dto.request.SignUpRequestDto;
import todo.dto.response.ResponseDto;
import todo.dto.response.SignInResponseDto;
import todo.service.AuthService;

@RestController
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "유저 회원가입", description = "유저 회원가입 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 완료", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> userSignUpAndSendEmail(
            @Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        return authService.userSignUpAndSendEmail(signUpRequestDto);
    }

    @GetMapping("/email/verify")
    public ResponseEntity<ResponseDto> verifyEmail(@RequestParam("token") String token) {
        return authService.verifyEmail(token);
    }

    @PostMapping("/login")
    @Operation(summary = "유저 로그인", description = "유저 로그인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "로그인 실패", content = @Content)
    })
    public ResponseEntity<? super SignInResponseDto> userSignIn(
            @Valid @RequestBody SignInRequestDto signInRequestDto) {
        return authService.userSignIn(signInRequestDto);
    }
}
