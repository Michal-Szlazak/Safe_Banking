package safe.bank.app.bankservice.dtos;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class BankAccountCreateDto {
    String accountName;
}
