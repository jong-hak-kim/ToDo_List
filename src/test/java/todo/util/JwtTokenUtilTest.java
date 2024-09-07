package todo.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenUtilTest {

    @Test
    void generateTokenTest() throws Exception {

        //given
        String token = JwtTokenUtil.generateToken("whdgkr9070@naver.com", "user");
        Claims claimsFromToken = JwtTokenUtil.getClaimsFromToken(token);
        //when

        //then
        assertThat(token).isNotNull();
        assertThat(claimsFromToken)
                .containsEntry("email", "whdgkr9070@naver.com")
                .containsEntry("role", "user");
    }

}