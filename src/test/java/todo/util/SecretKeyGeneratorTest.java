package todo.util;

import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import static org.assertj.core.api.Assertions.assertThat;

class SecretKeyGeneratorTest {

    @Test
    void secretKeyTest() throws Exception {
        //given
        SecretKey secretKey = SecretKeyGenerator.generateSecretKey();
        System.out.println("secretKey = " + secretKey);

        //when

        //then
        assertThat(secretKey).isNotNull();
        assertThat(secretKey.getEncoded()).hasSize(32);
    }

}