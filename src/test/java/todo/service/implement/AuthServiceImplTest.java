package todo.service.implement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import todo.common.constant.ResponseMessage;
import todo.dto.request.SignUpRequestDto;
import todo.dto.response.ResponseDto;
import todo.util.PasswordUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceImplTest {

    @Autowired
    private AuthServiceImpl authServiceImpl;

    @Autowired
    private PasswordUtil passwordUtil;


    @Test
    void addDuplicateEmail() throws Exception {
        //given
        SignUpRequestDto dto = new SignUpRequestDto();
        dto.setEmail("user1@naver.com");
        dto.setPassword("userPass2");
        dto.setProfileImg("profile.jpg");
        dto.setPhoneNumber("010-1234-5678");

        //when
        ResponseEntity<ResponseDto> responseEntity = authServiceImpl.userSignUpAndSendEmail(dto);
        ResponseEntity<ResponseDto> existUserEmail = ResponseMessage.EXIST_USER_EMAIL;

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(existUserEmail.getStatusCode());
        assertThat(responseEntity.getBody().getCode()).isEqualTo(existUserEmail.getBody().getCode());
        assertThat(responseEntity.getBody().getMessage()).isEqualTo(existUserEmail.getBody().getMessage());

    }

    @Test
    void addDuplicatePhoneNumber() throws Exception {
        //given
        SignUpRequestDto dto = new SignUpRequestDto();
        dto.setEmail("newUser@naver.com");
        dto.setPassword("userPass2");
        dto.setProfileImg("profile.jpg");
        dto.setPhoneNumber("010-1111-1111");
        //when
        ResponseEntity<ResponseDto> responseEntity = authServiceImpl.userSignUpAndSendEmail(dto);
        ResponseEntity<ResponseDto> existPhoneNumber = ResponseMessage.EXIST_PHONE_NUMBER;

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(existPhoneNumber.getStatusCode());
        assertThat(responseEntity.getBody().getCode()).isEqualTo(existPhoneNumber.getBody().getCode());
        assertThat(responseEntity.getBody().getMessage()).isEqualTo(existPhoneNumber.getBody().getMessage());
    }


    @Test
    void passwordEncode() throws Exception {
        //given
        String rawPassword = "user1111";
        //when
        String encodedPassword = passwordUtil.encodePassword(rawPassword);

        //then
        assertAll(
                () -> assertNotEquals(rawPassword, encodedPassword),
                () -> assertTrue(passwordUtil.matches(rawPassword, encodedPassword))
        );
    }

}