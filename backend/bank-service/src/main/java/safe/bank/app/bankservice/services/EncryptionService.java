package safe.bank.app.bankservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import safe.bank.app.bankservice.dtos.GeneratedAccountDTO;
import safe.bank.app.bankservice.entities.BankAccount;
import safe.bank.app.bankservice.entities.BankUser;
import safe.bank.app.bankservice.entities.Transfer;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EncryptionService {

    private final SecretKey secretKey;
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    public IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public String encrypt(String input, SecretKey key, IvParameterSpec iv) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] cipherText = cipher.doFinal(input.getBytes());
            return Base64.getEncoder()
                    .encodeToString(cipherText);
        } catch (Exception e) {
            System.out.println(input);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to encrypt data " + input + " " + e.getMessage());
        }
    }

    public String decrypt(String cipherText, SecretKey key, IvParameterSpec iv) {

        try {

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] plainText = cipher.doFinal(Base64.getDecoder()
                    .decode(cipherText));
            return new String(plainText);
        } catch (Exception e) {
            System.out.println(cipherText);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to decrypt data " + cipherText + "\n " + e.getMessage());
        }
    }

    public GeneratedAccountDTO encryptBankAccountCreateDTO(GeneratedAccountDTO generatedAccountDTO) {

        IvParameterSpec iv = generateIv();

        String accountNumber = generatedAccountDTO.getAccountNumber();
        String cvv = generatedAccountDTO.getCvv();
        String balance = generatedAccountDTO.getBalance();

        generatedAccountDTO.setCvv(encrypt(cvv, secretKey, iv));
        generatedAccountDTO.setAccountNumber(encrypt(accountNumber, secretKey, iv));
        generatedAccountDTO.setBalance(encrypt(String.valueOf(balance), secretKey, iv));
        generatedAccountDTO.setIv(iv.getIV());
        return generatedAccountDTO;
    }


    public BankAccount encryptBankAccount(BankAccount bankAccount) {

        IvParameterSpec iv = generateIv();
        String cvv = bankAccount.getCvv();
        String balance = bankAccount.getBalance();

        bankAccount.setCvv(encrypt(cvv, secretKey, iv));
        bankAccount.setBalance(encrypt(balance, secretKey, iv));
        bankAccount.setIv(iv.getIV());
        return bankAccount;
    }

    public BankAccount decryptBankAccount(BankAccount bankAccount) {

        byte[] ivBytes = bankAccount.getIv();
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        String cvv = bankAccount.getCvv();
        String balance = bankAccount.getBalance();

        bankAccount.setCvv(decrypt(cvv, secretKey, iv));
        bankAccount.setBalance(decrypt(balance, secretKey, iv));
        return bankAccount;
    }

    public Transfer decryptTransfer(Transfer transfer) {

        byte[] ivTransferBytes = transfer.getIv();
        IvParameterSpec ivTransfer = new IvParameterSpec(ivTransferBytes);
        String title = transfer.getTitle();
        String receiversName = transfer.getReceiverName();
        String amount = transfer.getAmount();

        transfer.setTitle(decrypt(title, secretKey, ivTransfer));
        transfer.setReceiverName(decrypt(receiversName, secretKey, ivTransfer));
        transfer.setAmount(decrypt(amount, secretKey, ivTransfer));
        return transfer;
    }

    public BankUser encryptBankUser(BankUser bankUser) {

        IvParameterSpec iv = generateIv();

        String firstName = bankUser.getFirstName();
        String lastName = bankUser.getLastName();
        String phoneNumber = bankUser.getPhoneNumber();

        bankUser.setFirstName(encrypt(firstName, secretKey, iv));
        bankUser.setLastName(encrypt(lastName, secretKey, iv));
        bankUser.setPhoneNumber(encrypt(phoneNumber, secretKey, iv));
        bankUser.setIv(iv.getIV());
        System.out.println(bankUser);
        return bankUser;
    }

    public BankUser decryptBankUser (BankUser bankUser) {

        byte[] ivBytes = bankUser.getIv();
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        String firstName = bankUser.getFirstName();
        String lastName = bankUser.getLastName();
        String phoneNumber = bankUser.getPhoneNumber();

        bankUser.setFirstName(decrypt(firstName, secretKey, iv));
        bankUser.setLastName(decrypt(lastName, secretKey, iv));
        bankUser.setPhoneNumber(decrypt(phoneNumber, secretKey, iv));
        bankUser.setIv(iv.getIV());
        return bankUser;
    }

    public Transfer encryptTransfer(Transfer transfer) {

        IvParameterSpec iv = generateIv();

        String title = transfer.getTitle();
        String receiversName = transfer.getReceiverName();
        String amount = transfer.getAmount();

        transfer.setTitle(encrypt(title, secretKey, iv));
        transfer.setReceiverName(encrypt(receiversName, secretKey, iv));
        transfer.setAmount(encrypt(amount, secretKey, iv));
        transfer.setIv(iv.getIV());
        return transfer;
    }

}
