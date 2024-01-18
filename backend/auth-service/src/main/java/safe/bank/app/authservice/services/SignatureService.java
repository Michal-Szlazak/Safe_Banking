package safe.bank.app.authservice.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
public class SignatureService {

//    @Value("${signature-service.secret}")
    private static String secret = "secret";
    private static final String ALGORITHM = "HmacSHA256";

    public String createDigitalSignature(List<Integer> dataList) {

        try {

            byte[] dataBytes = convertListToByteArray(dataList);

            SecretKeySpec secretKeySpec = new SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8),
                    ALGORITHM);

            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(secretKeySpec);


            return Base64.getEncoder().encodeToString(mac.doFinal(dataBytes));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to create signature for partial password index list.");
        }

    }

    private byte[] convertListToByteArray(List<Integer> dataList) {
        byte[] result = new byte[dataList.size() * Integer.BYTES];
        for (int i = 0; i < dataList.size(); i++) {
            int value = dataList.get(i);
            result[i * Integer.BYTES] = (byte) (value >> 24);
            result[i * Integer.BYTES + 1] = (byte) (value >> 16);
            result[i * Integer.BYTES + 2] = (byte) (value >> 8);
            result[i * Integer.BYTES + 3] = (byte) value;
        }
        return result;
    }

    public boolean verifyDigitalSignature(List<Integer> dataList, String receivedSignature) {

        try {
            String computedSignatureString = createDigitalSignature(dataList);
            return computedSignatureString.equals(receivedSignature);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to verify the signature for partial password.");
        }
    }
}
