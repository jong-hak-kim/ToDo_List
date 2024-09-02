package todo.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import todo.repository.UserRepository;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void addUser() throws Exception {
        //given
        User user1 = new User("user1@naver.com", "user1pass", "user1img", now(), "user", true, null);
        User user2 = new User("user2@naver.com", "user2pass", "user2img", now().minusHours(2), "user", false, now().plusDays(2));

        //when
        User saveUser1 = userRepository.save(user1);
        User saveUser2 = userRepository.save(user2);

        //then
        assertThat(saveUser1.getEmail()).isEqualTo(user1.getEmail());
        assertThat(saveUser2.getEmail()).isEqualTo(user2.getEmail());
    }

}