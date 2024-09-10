package todo.service;

import org.springframework.http.ResponseEntity;
import todo.dto.request.SignInRequestDto;
import todo.dto.request.SignUpRequestDto;
import todo.dto.request.UserImgRequestDto;
import todo.dto.response.ResponseDto;
import todo.util.UserToken;

public interface AuthService {

    public ResponseEntity<ResponseDto> userSignUpAndSendEmail(SignUpRequestDto dto);

    public ResponseEntity<ResponseDto> verifyEmail(String token);

    public ResponseEntity<ResponseDto> userSignIn(SignInRequestDto dto);

    public ResponseEntity<ResponseDto> updateUserImg(UserToken token, UserImgRequestDto dto);
}
