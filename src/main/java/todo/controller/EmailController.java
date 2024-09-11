package todo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import todo.dto.request.ResetPwdRequestDto;
import todo.dto.response.ResponseDto;
import todo.service.EmailService;

@RestController
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/reset/password")
    @Operation(summary = "임시 비밀번호 메일 전송", description = "임시 비밀번호 메일 전송 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전송 완료", content = @Content),
            @ApiResponse(responseCode = "401", description = "권한 없음", content = @Content),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "500", description = "이메일 전송 실패", content = @Content)
    })
    public ResponseEntity<ResponseDto> resetPassword(
            @Valid @RequestBody ResetPwdRequestDto resetPwdRequestDto) throws MessagingException {
        return emailService.sendTemporalPwdEmail(resetPwdRequestDto);
    }
}
