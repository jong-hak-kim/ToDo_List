package todo.service;

import org.springframework.http.ResponseEntity;
import todo.dto.request.admin.AdminPwdResetRequestDto;
import todo.dto.request.admin.AdminRemoveUserRequestDto;
import todo.dto.request.admin.AdminSignInRequestDto;
import todo.dto.response.ResponseDto;
import todo.util.UserToken;

public interface AdminService {

    ResponseEntity<ResponseDto> resetUserPassword(UserToken userToken, AdminPwdResetRequestDto dto);

    ResponseEntity<ResponseDto> signIn(AdminSignInRequestDto dto);

    ResponseEntity<ResponseDto> removeUser(UserToken userToken, AdminRemoveUserRequestDto dto);
}
