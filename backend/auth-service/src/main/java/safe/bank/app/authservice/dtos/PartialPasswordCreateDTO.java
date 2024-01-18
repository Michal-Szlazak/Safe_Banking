package safe.bank.app.authservice.dtos;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Builder
@Jacksonized
public record PartialPasswordCreateDTO(UUID userId, String password) {

}
