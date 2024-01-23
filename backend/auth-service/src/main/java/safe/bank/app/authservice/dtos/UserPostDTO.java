package safe.bank.app.authservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import safe.bank.app.authservice.validation.EntropyValidation;
import safe.bank.app.authservice.validation.PasswordConstraint;
import safe.bank.app.authservice.validation.PasswordMatches;

@Value
@Builder
@Jacksonized
@PasswordMatches()
public class UserPostDTO {

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
            message = "The first name can only contain letters"
    )
    String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    String email;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(
            regexp = "^\\d{9}$",
            message = "Phone number must be 9 digits"
    )
    String phoneNumber;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @PasswordConstraint
    @EntropyValidation
    String password;

    @NotBlank(message = "Password cannot be blank")
    String confirmPassword;
}
