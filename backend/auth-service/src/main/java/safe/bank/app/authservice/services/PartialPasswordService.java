package safe.bank.app.authservice.services;

import com.codahale.shamir.Scheme;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import safe.bank.app.authservice.dtos.PartialPasswordGetDTO;
import safe.bank.app.authservice.dtos.PartialPasswordPostDTO;
import safe.bank.app.authservice.entities.PartialPasswordPart;
import safe.bank.app.authservice.entities.PartialPasswordSet;
import safe.bank.app.authservice.repositories.PartialPasswordPartRepository;
import safe.bank.app.authservice.repositories.PartialPasswordSetRepository;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PartialPasswordService {


    private final PartialPasswordPartRepository partialPasswordPartRepository;
    private final PartialPasswordSetRepository partialPasswordSetRepository;
    private final SignatureService signatureService;
    private static final int GCM_NONCE_LENGTH = 12; // GCM nonce length in bytes
    private static final int GCM_TAG_LENGTH = 128;
    private static final Logger logger = LoggerFactory.getLogger(LoggerFactory.PROVIDER_PROPERTY_KEY);
    private static final int PARTS_NUMBER = 4;
    public void createPartialPasswordSet(String password, UUID userId) {

        SecureRandom secureRandom = new SecureRandom();

        Scheme scheme = new Scheme(new SecureRandom(), password.length(), PARTS_NUMBER);
        byte[] secret = new byte[32];
        secureRandom.nextBytes(secret);
        Map<Integer, byte[]> parts = scheme.split(secret);

        List<PartialPasswordPart> partsList = new ArrayList<>();

        parts.forEach((key, value) -> {
            PartialPasswordPart part = new PartialPasswordPart();

            byte[] salt = generateSalt();
            byte[] derivedKey = deriveKey(password.charAt(key - 1), salt);
            byte[] encryptedShare = encrypt(value, derivedKey);

            part.setShare(encryptedShare);
            part.setSalt(salt);
            partsList.add(part);
            partialPasswordPartRepository.save(part);
        });

        PartialPasswordSet partialPasswordSet = new PartialPasswordSet();
        partialPasswordSet.setUserId(userId);
        partialPasswordSet.setSecret(secret);
        partialPasswordSet.setN(password.length());
        partialPasswordSet.setK(PARTS_NUMBER);
        partialPasswordSet.setParts(partsList);
        partialPasswordSetRepository.save(partialPasswordSet);
    }

    public void checkPartialPassword(PartialPasswordPostDTO partialPasswordPostDTO,
                                        UUID userId) {

        if(!signatureService.verifyDigitalSignature(getIntegersFromMap(partialPasswordPostDTO.parts()),
                partialPasswordPostDTO.signature())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Wrong signature.");
        }

        PartialPasswordSet partialPasswordSet = partialPasswordSetRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Partial password not found"));

        Map<Integer, byte[]> parts = new HashMap<>();

        partialPasswordPostDTO.parts().forEach((key, value) -> {

            PartialPasswordPart part = partialPasswordSet.getParts().get(key - 1);
            byte[] salt = part.getSalt();
            byte[] derivedKey = deriveKey(value.charAt(0), salt);
            byte[] encryptedShare = part.getShare();
            byte[] decryptedShare = decrypt(encryptedShare, derivedKey);

            parts.put(key, decryptedShare);
        });

        Scheme scheme = new Scheme(new SecureRandom(),
                partialPasswordSet.getN(), partialPasswordSet.getK());

        byte[] recovered = scheme.join(parts);
        if(!Arrays.equals(recovered, partialPasswordSet.getSecret())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Failed to authorize.");
        }
    }

    public PartialPasswordGetDTO getPartialPassword(UUID userId) {

        PartialPasswordSet partialPasswordSet = partialPasswordSetRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Partial password not found"));

        List<Integer> partsIndexes = new ArrayList<>();
        SecureRandom secureRandom = new SecureRandom();

        while(partsIndexes.size() < partialPasswordSet.getK()) {
            int i = secureRandom.nextInt(1, partialPasswordSet.getN());

            if(!partsIndexes.contains(i)) {
                partsIndexes.add(i);
            }
        }

        Collections.sort(partsIndexes);
        String signature = signatureService.createDigitalSignature(partsIndexes);
        return new PartialPasswordGetDTO(signature, partsIndexes);
    }

    public static List<Integer> getIntegersFromMap(Map<Integer, String> map) {
        List<Integer> integerList = new ArrayList<>();

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            try {
                int intValue = entry.getKey();
                integerList.add(intValue);
            } catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Failed to authenticate using partial password. Invalid index.");
            }
        }

        return integerList;
    }

    private static byte[] deriveKey(char password, byte[] salt) {

        try {
            int iterationCount = 10000; // Adjust as needed
            int keyLength = 256; // Key length in bits

            KeySpec keySpec = new PBEKeySpec(new char[]{password}, salt, iterationCount, keyLength);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            return keyFactory.generateSecret(keySpec).getEncoded();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to create a derived key in Partial Password Service");
        }
    }

    private static byte[] generateSalt() {
        // Generating a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        return salt;
    }

    private static byte[] encrypt(byte[] data, byte[] key) {
        try {

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

            byte[] nonce = generateNonce();

            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, nonce);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);

            byte[] encryptedData = cipher.doFinal(data);

            byte[] result = new byte[nonce.length + encryptedData.length];
            System.arraycopy(nonce, 0, result, 0, nonce.length);
            System.arraycopy(encryptedData, 0, result, nonce.length, encryptedData.length);

            return result;
        } catch (Exception e) {
            throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to encrypt in Partial Password Service.");
        }
    }

    private static byte[] generateNonce() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] nonce = new byte[GCM_NONCE_LENGTH];
        secureRandom.nextBytes(nonce);
        return nonce;
    }

    private static byte[] decrypt(byte[] data, byte[] key) {

        try {

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

            byte[] nonce = new byte[GCM_NONCE_LENGTH];
            System.arraycopy(data, 0, nonce, 0, GCM_NONCE_LENGTH);

            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, nonce);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);

            return cipher.doFinal(data, GCM_NONCE_LENGTH, data.length - GCM_NONCE_LENGTH);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return generateSalt();
        }
    }
}
