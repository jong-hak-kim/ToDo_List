package todo.service;

import org.springframework.http.ResponseEntity;
import todo.dto.request.admin.*;
import todo.dto.response.ResponseDto;
import todo.util.UserToken;

public interface AdminService {

    ResponseEntity<ResponseDto> resetUserPassword(UserToken userToken, AdminPwdResetRequestDto dto);

    ResponseEntity<ResponseDto> signIn(AdminSignInRequestDto dto);

    ResponseEntity<ResponseDto> removeUser(UserToken userToken, AdminRemoveUserRequestDto dto);

    ResponseEntity<ResponseDto> deactivateUser(UserToken userToken, AdminDeactivateRequestDto dto);

    ResponseEntity<ResponseDto> removeToDo(UserToken userToken, RemoveUserToDoRequestDto dto);

}
