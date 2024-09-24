package todo.service.implement;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.common.constant.ResponseMessage;
import todo.dto.request.user.SignInRequestDto;
import todo.dto.request.user.SignUpRequestDto;
import todo.dto.request.user.UserImgRequestDto;
import todo.dto.request.user.UserPwdRequestDto;
import todo.dto.response.ResponseDto;
import todo.dto.response.SignInResponseDto;
import todo.entity.ToDoList;
import todo.entity.User;
import todo.entity.VerificationToken;
import todo.repository.ToDoListRepository;
import todo.repository.TokenRepository;
import todo.repository.UserRepository;
import todo.service.AuthService;
import todo.service.EmailService;
import todo.util.JwtTokenUtil;
import todo.util.PasswordUtil;
import todo.util.UUIDUtil;
import todo.util.UserToken;

import java.util.List;

import static todo.common.constant.ErrorMessage.DATABASE_ERROR_LOG;
import static todo.common.constant.ErrorMessage.MESSAGING_ERROR;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    private final JwtTokenUtil jwtTokenUtil;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final ToDoListRepository toDoListRepository;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordUtil passwordUtil, JwtTokenUtil jwtTokenUtil, TokenRepository tokenRepository, EmailService emailService, ToDoListRepository toDoListRepository1) {
        this.userRepository = userRepository;
        this.passwordUtil = passwordUtil;
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.toDoListRepository = toDoListRepository1;
    }

    @Override
    public ResponseEntity<ResponseDto> userSignUpAndSendEmail(SignUpRequestDto dto) {

        try {
            boolean existedUserEmail = userRepository.existsByEmail(dto.getEmail());
            if (existedUserEmail) return ResponseMessage.EXIST_USER_EMAIL;

            boolean existedPhoneNumber = userRepository.existsByPhoneNumber(dto.getPhoneNumber());
            if (existedPhoneNumber) return ResponseMessage.EXIST_PHONE_NUMBER;

            String encodedPassword = passwordUtil.encodePassword(dto.getPassword());

            User user = !dto.getProfileImg().isEmpty() ? new User(dto.getEmail(), encodedPassword, dto.getProfileImg(), dto.getPhoneNumber()) : new User(dto.getEmail(), encodedPassword, dto.getPhoneNumber());

            user.setActive(false);
            sendVerificationEmail(dto.getEmail());

            userRepository.save(user);
            return ResponseMessage.SUCCESS;

        } catch (DataAccessException exception) {
            log.error(DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        } catch (MessagingException exception) {
            log.error(MESSAGING_ERROR, exception);
            return ResponseMessage.EMAIL_SEND_ERROR;
        }
    }

    @Override
    public ResponseEntity<ResponseDto> verifyEmail(String token) {
        try {


            VerificationToken verificationToken = tokenRepository.findByToken(token);
            if (verificationToken == null) {
                return ResponseMessage.TOKEN_NOT_FOUND;
            }

            if (verificationToken.isExpired()) {
                return ResponseMessage.TOKEN_HAS_EXPIRED;
            }

            activateUser(verificationToken.getEmail());
            return ResponseMessage.SUCCESS;
        } catch (DataAccessException exception) {
            log.error(DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        }

    }

    @Override
    public ResponseEntity<ResponseDto> userSignIn(SignInRequestDto dto) {

        try {

            User user = userRepository.findUserByEmail(dto.getEmail());

            if (user == null) {
                return ResponseMessage.NOT_EXIST_USER;
            }

            if (!user.isActive()) {
                return ResponseMessage.IS_NOT_ACTIVATE;
            }

            if (passwordUtil.matches(dto.getPassword(), user.getPassword())) {
                String token = jwtTokenUtil.generateToken(user.getEmail(), user.getRole());
                log.info("generate Token : " + token);
                return ResponseEntity.status(HttpStatus.OK).body(new SignInResponseDto(user, token));
            }
        } catch (DataAccessException exception) {
            log.error(DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        }
        return ResponseMessage.LOGIN_FAILED;
    }

    @Override
    public ResponseEntity<ResponseDto> updateUserImg(UserToken userToken, UserImgRequestDto dto) {
        try {


            if (userToken == null) {
                return ResponseMessage.TOKEN_NOT_FOUND;
            }

            User user = userRepository.findUserByEmail(dto.getEmail());
            boolean existedUserEmail = userRepository.existsByEmail(userToken.getEmail());
            if (!existedUserEmail || user == null) {
                return ResponseMessage.NOT_EXIST_USER;
            }

            if (!user.isActive()) {
                return ResponseMessage.IS_NOT_ACTIVATE;
            }

            user.setProfileImg(dto.getImage());
            userRepository.save(user);
            return ResponseMessage.SUCCESS;
        } catch (DataAccessException exception) {
            log.error(DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        }
    }

    @Override
    public ResponseEntity<ResponseDto> updatePwd(UserToken userToken, UserPwdRequestDto dto) {
        try {

            log.info("Received token: {}", userToken);
            if (userToken == null) {
                return ResponseMessage.TOKEN_NOT_FOUND;
            }

            User user = userRepository.findUserByEmail(userToken.getEmail());
            if (!passwordUtil.matches(dto.getOriginalPwd(), user.getPassword())) {
                return ResponseMessage.PASSWORD_CURRENT_INVALID;
            }

            String newPassword = passwordUtil.encodePassword(dto.getNewPwd());
            user.setPassword(newPassword);

            userRepository.save(user);

            return ResponseMessage.SUCCESS;
        } catch (DataAccessException exception) {
            log.error(DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        }

    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> removeUser(UserToken userToken) {
        try {

            if (userToken == null) {
                return ResponseMessage.TOKEN_NOT_FOUND;
            }

            User user = userRepository.findUserByEmail(userToken.getEmail());

            if (user == null) {
                return ResponseMessage.NOT_EXIST_USER;
            }

            List<ToDoList> toDoLists = toDoListRepository.findToDoListsByUser(user);

            for (ToDoList toDoList : toDoLists) {
                toDoList.getComments().clear();
                user.removeToDoList(toDoList);
            }

            user.getComments().clear();

            VerificationToken verificationToken = tokenRepository.findByEmail(userToken.getEmail());
            tokenRepository.delete(verificationToken);

            userRepository.delete(user);

            return ResponseMessage.SUCCESS;
        } catch (DataAccessException exception) {
            log.error(DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        }
    }


    private void sendVerificationEmail(String email) throws MessagingException {

        String token = UUIDUtil.generateUUID();

        String verificationUrl = "http://127.0.0.1:8080/email/verify?token=" + token;
        saveVerificationToken(token, email);
        emailService.sendSignUpEmail(email, verificationUrl);

    }

    private void saveVerificationToken(String token, String email) {
        tokenRepository.save(new VerificationToken(token, email));
    }

    private void activateUser(String email) {
        User user = userRepository.findUserByEmail(email);
        user.setActive(true);
        userRepository.save(user);
    }
}
