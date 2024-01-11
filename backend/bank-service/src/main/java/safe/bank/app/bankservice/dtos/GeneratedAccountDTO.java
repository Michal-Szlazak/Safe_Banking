package safe.bank.app.bankservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;

@Value
@Builder
@Jacksonized
@AllArgsConstructor
public class GeneratedAccountDTO {

    String accountNumber;
    String cvv;
    Instant expiresAt;
}
