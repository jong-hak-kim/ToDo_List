package todo.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import todo.common.constant.ResponseMessage;
import todo.dto.request.SignUpRequestDto;
import todo.dto.response.ResponseDto;
import todo.entity.User;
import todo.repository.UserRepository;
import todo.service.AuthService;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<ResponseDto> userSignUp(SignUpRequestDto dto) {

        try {
            boolean existedUserEmail = userRepository.existsByEmail(dto.getEmail());
            if (existedUserEmail) return ResponseMessage.EXIST_USER_EMAIL;

            boolean existedPhoneNumber = userRepository.existsByPhoneNumber(dto.getPhoneNumber());
            if (existedPhoneNumber) return ResponseMessage.EXIST_PHONE_NUMBER;

            User user = !dto.getProfileImg().isEmpty()
                    ? new User(dto.getEmail(), dto.getPassword(), dto.getProfileImg(), dto.getPhoneNumber())
                    : new User(dto.getEmail(), dto.getPassword(), dto.getPhoneNumber());

            userRepository.save(user);
            return ResponseMessage.SUCCESS;

        } catch (DataAccessException exception) {
            log.error("Database error occurred while checking user details", exception);
            return ResponseMessage.DATABASE_ERROR;
        }
    }
}
