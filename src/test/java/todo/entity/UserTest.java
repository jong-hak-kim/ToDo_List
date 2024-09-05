package todo.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import todo.repository.UserRepository;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void addUser() throws Exception {
        //given
        User user1 = new User("user1@naver.com", "user1pass", "user1img", "010-1111-1111");
        User user2 = new User("user2@naver.com", "user2pass", "user2img", "010-2222-2222");

        //when
        User saveUser1 = userRepository.save(user1);
        User saveUser2 = userRepository.save(user2);

        //then
        assertThat(saveUser1.getEmail()).isEqualTo(user1.getEmail());
        assertThat(saveUser2.getEmail()).isEqualTo(user2.getEmail());
    }

    @Test
    void findUserByEmail() throws Exception {
        //given
        User user1 = new User("user1@naver.com", "user1pass", "user1img", "010-1111-1111");
        User user2 = new User("user2@naver.com", "user2pass", "user2img", "010-2222-2222");
        userRepository.save(user1);
        userRepository.save(user2);

        //when
        User findUser1 = userRepository.findUserByEmail("user1@naver.com");

        //then
        assertThat(findUser1.getEmail()).isEqualTo(user1.getEmail());
    }



}