package todo.service.implement;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import todo.common.constant.ResponseMessage;
import todo.dto.request.user.*;
import todo.dto.response.ResponseDto;
import todo.dto.response.user.GetUserImgResponseDto;
import todo.dto.response.user.GetUserProfileResponseDto;
import todo.dto.response.user.SignInResponseDto;
import todo.entity.ToDoList;
import todo.entity.User;
import todo.entity.VerificationToken;
import todo.repository.ToDoListRepository;
import todo.repository.TokenRepository;
import todo.repository.UserRepository;
import todo.service.AuthService;
import todo.service.EmailService;
import todo.util.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.*;
import static todo.common.constant.ErrorMessage.*;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    private final JwtTokenUtil jwtTokenUtil;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final ToDoListRepository toDoListRepository;
    private final SaveFileUtil saveFileUtil;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordUtil passwordUtil, JwtTokenUtil jwtTokenUtil, TokenRepository tokenRepository, EmailService emailService, ToDoListRepository toDoListRepository, SaveFileUtil saveFileUtil) {
        this.userRepository = userRepository;
        this.passwordUtil = passwordUtil;
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.toDoListRepository = toDoListRepository;
        this.saveFileUtil = saveFileUtil;
    }

    @Override
    public ResponseEntity<ResponseDto> userSignUpAndSendEmail(SignUpRequestDto dto) {

        try {
            boolean existedUserEmail = userRepository.existsByEmail(dto.getEmail());
            if (existedUserEmail) return ResponseMessage.EXIST_USER_EMAIL;

            boolean existedPhoneNumber = userRepository.existsByPhoneNumber(dto.getPhoneNumber());
            if (existedPhoneNumber) return ResponseMessage.EXIST_PHONE_NUMBER;

            String encodedPassword = passwordUtil.encodePassword(dto.getPassword());

            String profileImageUrl = saveProfileImage(dto.getProfileImg());


            User user = new User(dto.getEmail(), encodedPassword, profileImageUrl, dto.getPhoneNumber());

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
        } catch (IOException exception) {
            log.error(IMAGE_UPLOAD_ERROR_LOG, exception);
            return ResponseMessage.IMAGE_UPLOAD_ERROR;
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

                VerificationToken verificationToken = tokenRepository.findByEmail(dto.getEmail())
                        .orElse(null);

                if (verificationToken == null || verificationToken.getExpireDate().isBefore(now())) {
                    String token = jwtTokenUtil.generateToken(user.getEmail(), user.getRole());
                    verificationToken = new VerificationToken(token, dto.getEmail());
                    log.info("generate Token : {}", token);
                    tokenRepository.save(verificationToken);
                }
                return ResponseEntity.status(HttpStatus.OK).body(new SignInResponseDto(user, verificationToken.getToken()));
            }
        } catch (DataAccessException exception) {
            log.error(DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        }
        return ResponseMessage.LOGIN_FAILED;
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

            Optional<VerificationToken> verificationToken = tokenRepository.findByEmail(userToken.getEmail());
            tokenRepository.delete(verificationToken.get());

            userRepository.delete(user);

            return ResponseMessage.SUCCESS;
        } catch (DataAccessException exception) {
            log.error(DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        }
    }

    @Override
    public ResponseEntity<ResponseDto> getUserImg(UserToken userToken) {
        try {
            if (userToken == null) {
                return ResponseMessage.TOKEN_NOT_FOUND;
            }

            User user = userRepository.findUserByEmail(userToken.getEmail());

            if (user == null) {
                return ResponseMessage.NOT_EXIST_USER;
            }

            int lastSlashIndex = user.getProfileImg().lastIndexOf("/");
            String url = user.getProfileImg().substring(lastSlashIndex + 1);
            Path profileImg = Paths.get("src/main/resources/uploads/", url);
            byte[] fileContent = Files.readAllBytes(profileImg);
            String encodedString = Base64.getEncoder().encodeToString(fileContent);

            return ResponseEntity.status(HttpStatus.OK).body(new GetUserImgResponseDto(encodedString));
        } catch (IOException exception) {
            log.error(GET_IMAGE_ERROR_LOG, exception);
            return ResponseMessage.GET_IMAGE_ERROR;
        }

    }

    @Override
    public ResponseEntity<ResponseDto> getUserProfile(UserToken userToken) {
        try {
            if (userToken == null) {
                return ResponseMessage.TOKEN_NOT_FOUND;
            }

            User user = userRepository.findUserByEmail(userToken.getEmail());

            if (user == null) {
                return ResponseMessage.NOT_EXIST_USER;
            }

            int lastSlashIndex = user.getProfileImg().lastIndexOf("/");
            String url = user.getProfileImg().substring(lastSlashIndex + 1);
            Path profileImg = Paths.get("src/main/resources/uploads/", url);
            byte[] fileContent = Files.readAllBytes(profileImg);
            String encodedString = Base64.getEncoder().encodeToString(fileContent);

            return ResponseEntity.status(HttpStatus.OK).body(new GetUserProfileResponseDto(user.getEmail(), user.getPhoneNumber(), encodedString));
        } catch (DataAccessException exception) {
            log.error(DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        } catch (IOException exception) {
            log.error(GET_IMAGE_ERROR_LOG, exception);
            return ResponseMessage.GET_IMAGE_ERROR;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> modifyProfile(UserToken userToken, UserProfileModifyRequestDto dto) {
        try {
            if (userToken == null) {
                return ResponseMessage.TOKEN_NOT_FOUND;
            }

            User user = userRepository.findUserByEmail(userToken.getEmail());

            if (user == null) {
                return ResponseMessage.NOT_EXIST_USER;
            }

            boolean isModified = false; // 변경 사항이 있는지 확인하기 위한 플래그

            if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isEmpty()) {
                log.info(dto.getPhoneNumber());
                user.setPhoneNumber(dto.getPhoneNumber());
                isModified = true;
            }

            String profileImageUrl = saveProfileImage(dto.getProfileImg());
            if (profileImageUrl != null) {
                user.setProfileImg(profileImageUrl);
                isModified = true;
            }

            if (dto.getCurrentPassword() != null && dto.getNewPassword() != null) {
                if (!passwordUtil.matches(dto.getCurrentPassword(), user.getPassword())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ResponseDto("현재 비밀번호가 일치하지 않습니다.", null));
                }

                user.setPassword(passwordUtil.encodePassword(dto.getNewPassword()));
                isModified = true;
            }

            if (isModified) {
                userRepository.save(user); // 변경된 사용자 정보를 저장
            }

            return ResponseMessage.SUCCESS;

        } catch (DataAccessException exception) {
            log.error(DATABASE_ERROR_LOG, exception);
            return ResponseMessage.DATABASE_ERROR;
        } catch (IOException exception) {
            log.error(GET_IMAGE_ERROR_LOG, exception);
            return ResponseMessage.GET_IMAGE_ERROR;
        }
    }


    private void sendVerificationEmail(String email) throws MessagingException {

        String token = UUIDUtil.generateUUID();

        String verificationUrl = "http://127.0.0.1:3000/email/verify?token=" + token;
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

    private String saveProfileImage(MultipartFile image) throws IOException {
        MultipartFile profileImg = image;
        String profileImageUrl;
        if (profileImg != null && !profileImg.isEmpty()) {
            String filename = image.getOriginalFilename();
            byte[] bytes = image.getBytes();
            profileImageUrl = saveFileUtil.saveFile(bytes, filename);
        } else {
            profileImageUrl = "http://127.0.0.1:8080/default.png";
        }
        return profileImageUrl;
    }
}
