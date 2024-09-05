package todo.service.implement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import todo.util.PasswordUtil;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceImplTest {

    @Autowired
    private AuthServiceImpl authServiceImpl;

    @Autowired
    private PasswordUtil passwordUtil;

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