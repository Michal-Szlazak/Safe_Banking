package safe.bank.app.authservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Jacksonized
public record PartialPasswordGetDTO(String signature, List<Integer> parts) {

}
