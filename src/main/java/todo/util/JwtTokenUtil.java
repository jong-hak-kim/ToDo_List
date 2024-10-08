package todo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtil {

    private SecretKey secretKey;

    @Value("${jwt.secret-key}")
    private String secretKeyStr;

    @PostConstruct
    public void init() {
        byte[] keyBytes = secretKeyStr.getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email, String role) {
        Instant now = Instant.now();
        long expirationTime = 3600L;

        return Jwts.builder()
                .claim("iat", Date.from(now))
                .claim("exp", Date.from(now.plusSeconds(expirationTime)))
                .claim("email", email)
                .claim("role", role)
                .signWith(secretKey)
                .compact();
    }

    public UserToken validate(String token) {

        String email = "";
        String role = "";
        log.info(token);

        try {
            Claims claims = Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            email = "" + claims.get("email");
            role = "" + claims.get("role");
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        return new UserToken(email, role);
    }

}
