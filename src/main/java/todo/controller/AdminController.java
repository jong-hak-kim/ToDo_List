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
import todo.dto.request.admin.AdminPwdResetRequestDto;
import todo.dto.request.admin.AdminSignInRequestDto;
import todo.dto.response.ResponseDto;
import todo.service.AdminService;
import todo.util.UserToken;

@RestController
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/admin/login")
    @Operation(summary = "관리자 로그인", description = "관리자 로그인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전송 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한 없음", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> adminSignIn(
            @Valid @RequestBody AdminSignInRequestDto adminSignInRequestDto){
        return adminService.signIn(adminSignInRequestDto);
    }

    @PostMapping("/admin/password")
    @Operation(summary = "관리자가 유저 임시 비밀번호 발급", description = "관리자 유저의 임시 비밀번호 발급 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전송 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한 없음", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "이메일 전송 실패, DB 에러", content = @Content)
    })
    public ResponseEntity<ResponseDto> resetUserPassword(
            @AuthenticationPrincipal UserToken userToken,
            @Valid @RequestBody AdminPwdResetRequestDto adminPwdResetRequestDto) {
        return adminService.resetUserPassword(userToken, adminPwdResetRequestDto);
    }

}
