package safe.bank.app.bankservice.dtos;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
public class PostBankUserDTO {

    UUID userId;
    String phoneNumber;
    String address;
}
