package safe.bank.app.bankservice.dtos;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.Instant;

@Value
@Builder
@Jacksonized
public class BankAccountGetDTO {

    String accountName;
    String accountNumber;
    String cvv;
    Instant expiresAt;
    BigDecimal balance;
}
