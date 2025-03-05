package safe.bank.app.bankservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Component
public class CvvGenerator {

    @Value("${secrets.encryption-key}")
    private String key;

    public String generateCVV(String accountNumber, Instant expiresAt) {
        try {
            // Format expiration date as MMYY (e.g., 0327 for March 2027)
            String expiryDate = DateTimeFormatter.ofPattern("MMyy")
                    .withZone(ZoneId.of("UTC"))
                    .format(expiresAt);

            // Combine account number and expiry date
            String data = accountNumber + expiryDate;

            // Create HMAC-SHA256 hash
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // Convert hash to Base64 and take first 3 digits for CVV
            return Base64.getEncoder().encodeToString(hash).replaceAll("\\D", "").substring(0, 3);
        } catch (Exception e) {
            throw new RuntimeException("Error generating CVV", e);
        }
    }

    public boolean validateCVV(String inputCVV, String accountNumber, Instant expiresAt) {
        String generatedCVV = generateCVV(accountNumber, expiresAt);
        return generatedCVV.equals(inputCVV);
    }
}
