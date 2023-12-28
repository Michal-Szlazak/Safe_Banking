package safe.bank.app.authservice.dtos;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class UserLoginDTO {

    String email;
    String password;
}
