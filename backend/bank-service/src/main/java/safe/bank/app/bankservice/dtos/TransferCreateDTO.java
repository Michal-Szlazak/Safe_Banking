package safe.bank.app.bankservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Value
@Builder
@Jacksonized
public class TransferCreateDTO {


    @Pattern(
            regexp = "^[a-zA-Z\\s]+$",
            message = "Wrong receivers name format."
    )
    String receiverName;

    @Pattern(
            regexp = "^[a-zA-Z\\s]+$",
            message = "Wrong transfer title format."
    )
    String title;

    @NotBlank
    @Pattern(
            regexp = "^[A-Z]{2}\\d{26}$",
            message = "Wrong account number format."
    )
    String senderAccount;

    @Pattern(
            regexp = "^[A-Z]{2}\\d{26}$",
            message = "Wrong account number format."
    )
    String receiverAccount;
    BigDecimal amount;
}
