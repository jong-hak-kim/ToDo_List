package todo.service;

import org.springframework.http.ResponseEntity;
import todo.dto.request.SignUpRequestDto;
import todo.dto.response.ResponseDto;

public interface AuthService {

    public ResponseEntity<ResponseDto> userSignUpAndSendEmail(SignUpRequestDto dto);
    public ResponseEntity<ResponseDto> verifyEmail(String token);
}
