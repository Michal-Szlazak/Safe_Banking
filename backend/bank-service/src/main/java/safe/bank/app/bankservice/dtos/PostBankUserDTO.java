package safe.bank.app.bankservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
public class PostBankUserDTO {

    UUID userId;

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 20, message = "First name cannot be longer than 20 characters")
    @Pattern(
            regexp = "^[a-zA-Z]+$",
            message = "The first name can only contain letters"
    )
    String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 20, message = "Last name cannot be longer than 20 characters")
    @Pattern(
            regexp = "^[a-zA-Z]+$",
            message = "The last name can only contain letters"
    )
    String lastName;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(
            regexp = "^\\d{9}$",
            message = "Phone number must be 9 digits"
    )
    String phoneNumber;
}
