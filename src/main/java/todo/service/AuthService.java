package todo.service;

import org.springframework.http.ResponseEntity;
import todo.dto.request.admin.RemoveUserToDoRequestDto;
import todo.dto.request.user.*;
import todo.dto.response.ResponseDto;
import todo.util.UserToken;

public interface AuthService {

    ResponseEntity<ResponseDto> userSignUpAndSendEmail(SignUpRequestDto dto);

    ResponseEntity<ResponseDto> verifyEmail(String token);

    ResponseEntity<ResponseDto> userSignIn(SignInRequestDto dto);

    ResponseEntity<ResponseDto> removeUser(UserToken userToken);

    ResponseEntity<ResponseDto> getUserImg(UserToken userToken);

    ResponseEntity<ResponseDto> getUserProfile(UserToken userToken);

    ResponseEntity<ResponseDto> modifyProfile(UserToken userToken, UserProfileModifyRequestDto dto);
}
