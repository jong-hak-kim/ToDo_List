package todo.service;

import org.springframework.http.ResponseEntity;
import todo.dto.request.user.SignInRequestDto;
import todo.dto.request.user.SignUpRequestDto;
import todo.dto.request.user.UserImgRequestDto;
import todo.dto.request.user.UserPwdRequestDto;
import todo.dto.response.ResponseDto;
import todo.util.UserToken;

public interface AuthService {

    ResponseEntity<ResponseDto> userSignUpAndSendEmail(SignUpRequestDto dto);

    ResponseEntity<ResponseDto> verifyEmail(String token);

    ResponseEntity<ResponseDto> userSignIn(SignInRequestDto dto);

    ResponseEntity<ResponseDto> updateUserImg(UserToken token, UserImgRequestDto dto);

    ResponseEntity<ResponseDto> updatePwd(UserToken token, UserPwdRequestDto dto);

    ResponseEntity<ResponseDto> removeUser(UserToken userToken);
}
