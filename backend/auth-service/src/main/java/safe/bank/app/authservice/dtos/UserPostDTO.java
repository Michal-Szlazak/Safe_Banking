package safe.bank.app.authservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import safe.bank.app.authservice.validation.PasswordMatches;

@Value
@Builder
@Jacksonized
@PasswordMatches()
public class UserPostDTO {

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 20, message = "First name cannot be longer than 20 characters")
    String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 20, message = "Last name cannot be longer than 20 characters")
    String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    String email;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(
            regexp = "^[0-9]{9}$",
            message = "Phone number must be 10 digits"
    )
    String phoneNumber;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    String password;

    @NotBlank(message = "Password cannot be blank")
    String confirmPassword;
}
