package safe.bank.app.bankservice.configurations;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Component
public class Beans {

    @Bean
    public Faker faker() {
        return new Faker();
    }

    @Bean
    public SecretKey secretKey() {
        byte[] keyBytes = hexStringToByteArray("7d3480a02ebe5b24d46dc14c1805677d5f9615b6f57e48a39d7e8f8e6f4cc1a3");
        return new SecretKeySpec(keyBytes, "AES");
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
