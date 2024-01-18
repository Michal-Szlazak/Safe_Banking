package safe.bank.app.bankservice.services;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import safe.bank.app.bankservice.dtos.GeneratedAccountDTO;
import safe.bank.app.bankservice.repositories.BankAccountRepository;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FakerService {

    private final Faker faker;
    private final BankAccountRepository bankAccountRepository;

    public GeneratedAccountDTO generatedAccountDTO() {

        String accountNumber = generateBankAccountNumber();
        String cvv = generateCcvNumber();
        Instant expiresAt = getExpirationDate();
        BigDecimal balance = BigDecimal.valueOf(1000);
        return new GeneratedAccountDTO(accountNumber, cvv, expiresAt, balance.toString());
    }
    private String generateFakeBankAccountNumber() {

        return "PL" + faker.number().digits(26);
    }

    private String generateBankAccountNumber() {
        List<String> accounts = bankAccountRepository.findAllAccountNumbers();

        String accountNumber;
        do {
            accountNumber = this.generateFakeBankAccountNumber();
        } while (accounts.contains(accountNumber));

        return accountNumber;
    }

    private String generateCcvNumber() {
        return faker.number().digits(3);
    }

    private Instant getExpirationDate() {
        return Instant.now().plus(Duration.ofDays(365).multipliedBy(3));
    }
}
