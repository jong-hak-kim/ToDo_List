package todo.service;

import org.springframework.http.ResponseEntity;
import todo.dto.request.SignUpRequestDto;
import todo.dto.response.ResponseDto;

public interface AuthService {

    public ResponseEntity<ResponseDto> userSignUp(SignUpRequestDto dto);
}
