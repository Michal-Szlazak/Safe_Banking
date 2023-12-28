package safe.bank.app.authservice.entities;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ErrorResponseEntity {

    String errorMessage;
}
