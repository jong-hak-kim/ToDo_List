package todo.service.implement;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import todo.common.constant.ResponseMessage;
import todo.dto.request.SignInRequestDto;
import todo.dto.request.SignUpRequestDto;
import todo.dto.response.ResponseDto;
import todo.dto.response.SignInResponseDto;
import todo.entity.User;
import todo.entity.VerificationToken;
import todo.repository.TokenRepository;
import todo.repository.UserRepository;
import todo.service.AuthService;
import todo.service.EmailService;
import todo.util.JwtTokenUtil;
import todo.util.PasswordUtil;
import todo.util.UUIDUtil;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordUtil passwordUtil, TokenRepository tokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordUtil = passwordUtil;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    @Override
    public ResponseEntity<ResponseDto> userSignUpAndSendEmail(SignUpRequestDto dto) {

        try {
            boolean existedUserEmail = userRepository.existsByEmail(dto.getEmail());
            if (existedUserEmail) return ResponseMessage.EXIST_USER_EMAIL;

            boolean existedPhoneNumber = userRepository.existsByPhoneNumber(dto.getPhoneNumber());
            if (existedPhoneNumber) return ResponseMessage.EXIST_PHONE_NUMBER;

            String encodedPassword = passwordUtil.encodePassword(dto.getPassword());

            User user = !dto.getProfileImg().isEmpty()
                    ? new User(dto.getEmail(), encodedPassword, dto.getProfileImg(), dto.getPhoneNumber())
                    : new User(dto.getEmail(), encodedPassword, dto.getPhoneNumber());

            user.setActive(false);
            sendVerificationEmail(dto.getEmail());

            userRepository.save(user);
            return ResponseMessage.SUCCESS;

        } catch (DataAccessException exception) {
            log.error("Database error occurred while checking user details", exception);
            return ResponseMessage.DATABASE_ERROR;
        } catch (MessagingException exception) {
            log.error("error occurred while sending the email", exception);
            return ResponseMessage.EMAIL_SEND_ERROR;
        }
    }

    @Override
    public ResponseEntity<ResponseDto> verifyEmail(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return ResponseMessage.TOKEN_NOT_FOUND;
        }

        if (verificationToken.isExpired()) {
            return ResponseMessage.TOKEN_HAS_EXPIRED;
        }

        activateUser(verificationToken.getEmail());

        return ResponseMessage.SUCCESS;

    }

    @Override
    public ResponseEntity<? super SignInResponseDto> userSignIn(SignInRequestDto dto) {

        User user = userRepository.findUserByEmail(dto.getEmail());


        if (passwordUtil.matches(dto.getPassword(), user.getPassword())) {
            String token = JwtTokenUtil.generateToken(user.getEmail(), user.getRole());
            return ResponseEntity.status(HttpStatus.OK).body(new SignInResponseDto(user, token));
        }


        return ResponseMessage.LOGIN_FAILED;
    }

    private void sendVerificationEmail(String email) throws MessagingException {

        String token = UUIDUtil.generateUUID();
        String verificationUrl = "http://127.0.0.1:8080/email/verify?token=" + token;

        saveVerificationToken(token, email);

        emailService.sendTokenEmail(email, verificationUrl);
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
