package todo.service.implement;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import todo.common.constant.ErrorMessage;
import todo.common.constant.ResponseMessage;
import todo.dto.request.admin.AdminPwdResetRequestDto;
import todo.dto.request.admin.AdminSignInRequestDto;
import todo.dto.request.user.ResetPwdRequestDto;
import todo.dto.response.ResponseDto;
import todo.dto.response.admin.AdminSignInResponseDto;
import todo.entity.User;
import todo.repository.UserRepository;
import todo.service.AdminService;
import todo.service.EmailService;
import todo.util.JwtTokenUtil;
import todo.util.PasswordUtil;
import todo.util.UserToken;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordUtil passwordUtil;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, EmailService emailService, PasswordUtil passwordUtil, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordUtil = passwordUtil;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public ResponseEntity<ResponseDto> resetUserPassword(UserToken userToken, AdminPwdResetRequestDto dto) {

        try {

            if (userToken == null) {
                return ResponseMessage.TOKEN_NOT_FOUND;
            }

            if (!userToken.getRole().equals("admin")) {
                return ResponseMessage.UNAUTHORIZED_USER;
            }

            return emailService.sendTemporalPwdEmail(new ResetPwdRequestDto(dto.getEmail()));

        } catch (DataAccessException exception) {
            log.error(ErrorMessage.DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        } catch (MessagingException exception) {
            log.error(ErrorMessage.MESSAGING_ERROR, exception);
            return ResponseMessage.EMAIL_SEND_ERROR;
        }
    }

    @Override
    public ResponseEntity<ResponseDto> signIn(AdminSignInRequestDto dto) {

        try {

            User user = userRepository.findUserByEmail(dto.getEmail());

            if (user == null) {
                return ResponseMessage.NOT_EXIST_USER;
            }

            if (passwordUtil.matches(dto.getPassword(), user.getPassword())) {
                String token = jwtTokenUtil.generateToken(user.getEmail(), user.getRole());
                log.info("generate Token : " + token);
                return ResponseEntity.status(HttpStatus.OK).body(new AdminSignInResponseDto(user.getEmail(), token));
            }
        } catch (DataAccessException exception) {
            log.error(ErrorMessage.DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        }

        return ResponseMessage.LOGIN_FAILED;
    }
}
