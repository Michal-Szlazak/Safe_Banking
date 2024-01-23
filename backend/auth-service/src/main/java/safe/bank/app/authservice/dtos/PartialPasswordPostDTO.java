package safe.bank.app.authservice.dtos;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Builder
@Jacksonized
public record PartialPasswordPostDTO(String signature, Map<Integer, String> parts) {

}
