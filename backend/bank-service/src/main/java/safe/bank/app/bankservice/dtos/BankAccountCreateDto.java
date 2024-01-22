package safe.bank.app.bankservice.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class BankAccountCreateDto {

    @NotBlank(message = "Account name cannot be blank")
    @Pattern(
            regexp = "^[A-Za-z]$",
            message = "Account name can only contain letters."
    )
    @Max(value = 20, message = "Max account name size is 20.")
    @Min(value = 2, message = "Min account name size is 2.")
    String accountName;
}
