package todo.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class SecretKeyGenerator {

    private SecretKeyGenerator() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static SecretKey generateSecretKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            keyGenerator.init(256);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException exception) {
            log.error("HmacSHA256 algorithm not available", exception);
            throw new IllegalStateException("Failed to generate secret key: Algorithm not available", exception);
        }
    }

}
