package todo.service.implement;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.common.constant.ErrorMessage;
import todo.common.constant.ResponseMessage;
import todo.dto.request.admin.*;
import todo.dto.request.user.ResetPwdRequestDto;
import todo.dto.response.ResponseDto;
import todo.dto.response.admin.AdminSignInResponseDto;
import todo.entity.ToDoList;
import todo.entity.User;
import todo.repository.ToDoListRepository;
import todo.repository.UserRepository;
import todo.service.AdminService;
import todo.service.EmailService;
import todo.util.JwtTokenUtil;
import todo.util.PasswordUtil;
import todo.util.UserToken;

import java.time.LocalDateTime;
import java.util.List;

import static todo.common.enums.Role.ADMIN;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ToDoListRepository toDoListRepository;
    private final EmailService emailService;
    private final PasswordUtil passwordUtil;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, ToDoListRepository toDoListRepository, EmailService emailService, PasswordUtil passwordUtil, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.toDoListRepository = toDoListRepository;
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

            if (!userToken.getRole().equals(ADMIN.getDescription())) {
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

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> removeUser(UserToken userToken, AdminRemoveUserRequestDto dto) {
        try {

            if (userToken == null) {
                return ResponseMessage.TOKEN_NOT_FOUND;
            }

            if (!userToken.getRole().equals(ADMIN.getDescription())) {
                return ResponseMessage.UNAUTHORIZED_USER;
            }

            User user = userRepository.findUserWithToDoLists(dto.getEmail());

            List<ToDoList> toDoLists = user.getToDoLists();

            for (ToDoList toDoList : toDoLists) {
                toDoList.getComments().clear();
                user.removeToDoList(toDoList);
            }

            user.getComments().clear();

            emailService.sendRemoveEmail(dto.getEmail(), "비속어 사용");

            userRepository.delete(user);


            return ResponseMessage.SUCCESS;

        } catch (MessagingException exception) {
            log.error(ErrorMessage.MESSAGING_ERROR, exception);
            return ResponseMessage.EMAIL_SEND_ERROR;
        } catch (DataAccessException exception) {
            log.error(ErrorMessage.DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        }
    }

    @Override
    public ResponseEntity<ResponseDto> deactivateUser(UserToken userToken, AdminDeactivateRequestDto dto) {
        try {
            if (userToken == null) {
                return ResponseMessage.TOKEN_NOT_FOUND;
            }

            if (!userToken.getRole().equals(ADMIN.getDescription())) {
                return ResponseMessage.UNAUTHORIZED_TOKEN;
            }

            User user = userRepository.findUserByEmail(dto.getEmail());

            user.setActive(false);
            user.setDeactivationDate(dto.getDeactivateDate());

            userRepository.save(user);

            emailService.sendDeactivationEmail(dto.getEmail(), "비정상적인 활동", dto.getDeactivateDate().toLocalDate());

            return ResponseMessage.SUCCESS;
        } catch (DataAccessException exception) {
            log.error(ErrorMessage.DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        }
    }

    @Override
    public ResponseEntity<ResponseDto> removeToDo(UserToken userToken, RemoveUserToDoRequestDto dto) {

        try {
            if (userToken == null) {
                return ResponseMessage.TOKEN_NOT_FOUND;
            }

            if (!userToken.getRole().equals(ADMIN.getDescription())) {
                return ResponseMessage.UNAUTHORIZED_TOKEN;
            }

            ToDoList toDoList = toDoListRepository.findToDoListByListId(dto.getListId());

            emailService.sendRemoveUserToDo(toDoList.getUser().getEmail(), toDoList.getTitle(), dto.getReason());

            return ResponseMessage.SUCCESS;

        } catch (DataAccessException exception) {
            log.error(ErrorMessage.DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        }

    }

    @Scheduled(fixedRate = 86400000)
    public void reactivateUser() {
        LocalDateTime now = LocalDateTime.now();
        List<User> deactivateUsers = userRepository.findAllByIsActiveIsFalseAndDeactivationDateBefore(now);

        for (User deactivateUser : deactivateUsers) {
            deactivateUser.setActive(true);
            deactivateUser.setDeactivationDate(null);
            userRepository.save(deactivateUser);
        }
    }
}
