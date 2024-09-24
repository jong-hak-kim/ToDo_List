package todo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import todo.dto.request.user.SignInRequestDto;
import todo.dto.request.user.SignUpRequestDto;
import todo.dto.request.user.UserImgRequestDto;
import todo.dto.request.user.UserPwdRequestDto;
import todo.dto.response.ResponseDto;
import todo.service.AuthService;
import todo.util.UserToken;

@Slf4j
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
    @Operation(summary = "이메일 인증", description = "유저 이메일 링크 인증 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "토큰 기간 만료"),
            @ApiResponse(responseCode = "404", description = "인증 토큰 없음")
    })
    public ResponseEntity<ResponseDto> verifyEmail(@RequestParam("token") String token) {
        return authService.verifyEmail(token);
    }

    @PostMapping("/login")
    @Operation(summary = "유저 로그인", description = "유저 로그인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "로그인 실패", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "이메일 전송 실패", content = @Content)
    })
    public ResponseEntity<ResponseDto> userSignIn(
            @Valid @RequestBody SignInRequestDto signInRequestDto) {
        return authService.userSignIn(signInRequestDto);
    }

    @PostMapping("/profile_img")
    @Operation(summary = "프로필 사진 변경", description = "프로필 사진 변경 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 사진 변경 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한이 없는 유저", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content)
    })
    public ResponseEntity<ResponseDto> updateUserImg(
            @AuthenticationPrincipal UserToken userToken,
            @Valid @RequestBody UserImgRequestDto userImgRequestDto) {
        return authService.updateUserImg(userToken, userImgRequestDto);
    }

    @PostMapping("/password")
    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 완료", content = @Content),
            @ApiResponse(responseCode = "400", description = "기존 비밀번호 불일치", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한이 없는 유저", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> updatePwd(
            @AuthenticationPrincipal UserToken userToken,
            @Valid @RequestBody UserPwdRequestDto userPwdRequestDto) {
        log.info("Received token: {}", userToken);
        return authService.updatePwd(userToken, userPwdRequestDto);
    }

    @PostMapping("/remove")
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한이 없는 유저", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> removeUser(
            @AuthenticationPrincipal UserToken userToken) {
        return authService.removeUser(userToken);
    }
}
