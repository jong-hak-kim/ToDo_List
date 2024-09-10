package todo.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenUtilTest {

    @Test
    void generateTokenTest() throws Exception {

        final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

        //given
        String token = jwtTokenUtil.generateToken("whdgkr9070@naver.com", "user");
        Claims claimsFromToken = jwtTokenUtil.getClaimsFromToken(token);
        //when

        //then
        assertThat(token).isNotNull();
        assertThat(claimsFromToken)
                .containsEntry("email", "whdgkr9070@naver.com")
                .containsEntry("role", "user");
    }

}