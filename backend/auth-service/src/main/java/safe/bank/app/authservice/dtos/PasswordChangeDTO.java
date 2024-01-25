package safe.bank.app.authservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import safe.bank.app.authservice.validation.ChangePasswordMatches;
import safe.bank.app.authservice.validation.EntropyValidation;
import safe.bank.app.authservice.validation.PasswordConstraint;

@Data
@ChangePasswordMatches
public class PasswordChangeDTO {

    @NotBlank(message = "Password cannot be blank")
    private String oldPassword;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @PasswordConstraint
    @EntropyValidation
    private String newPassword;

    private String confirmNewPassword;
}
