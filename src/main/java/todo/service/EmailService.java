package todo.service;

import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import todo.dto.response.ResponseDto;

public interface EmailService {
    public ResponseEntity<ResponseDto> sendTokenEmail(String email, String verificationUrl) throws MessagingException;
}
