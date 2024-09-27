package todo.service;

import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import todo.dto.request.user.ResetPwdRequestDto;
import todo.dto.response.ResponseDto;

import java.time.LocalDate;

public interface EmailService {
    ResponseEntity<ResponseDto> sendSignUpEmail(String email, String verificationUrl) throws MessagingException;

    ResponseEntity<ResponseDto> sendTemporalPwdEmail(ResetPwdRequestDto dto) throws MessagingException;

    ResponseEntity<ResponseDto> sendRemoveEmail(String email, String content) throws MessagingException;

    ResponseEntity<ResponseDto> sendDeactivationEmail(String email, String reason, LocalDate deactivationDate);
}
