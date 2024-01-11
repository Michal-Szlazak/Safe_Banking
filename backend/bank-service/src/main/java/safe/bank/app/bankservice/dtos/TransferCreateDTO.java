package safe.bank.app.bankservice.dtos;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Value
@Builder
@Jacksonized
public class TransferCreateDTO {

    String receiverName;
    String title;
    String senderAccount;
    String receiverAccount;
    BigDecimal amount;
}
