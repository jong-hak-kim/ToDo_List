package todo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

public class JwtTokenUtil {

    private static final SecretKey secretKey = SecretKeyGenerator.generateSecretKey();

    private JwtTokenUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String generateToken(String email, String role) {
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

    public static Claims getClaimsFromToken(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
