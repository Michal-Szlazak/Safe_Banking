package safe.bank.app.bankservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import java.time.Instant;

@Data
@Builder
@Jacksonized
@AllArgsConstructor
public class GeneratedAccountDTO {

    public GeneratedAccountDTO(String accountNumber, String cvv, Instant expiresAt, String balance) {
        this.accountNumber = accountNumber;
        this.cvv = cvv;
        this.expiresAt = expiresAt;
        this.balance = balance;
    }

    String accountNumber;
    String cvv;
    Instant expiresAt;
    String balance;
    byte[] iv;
}
