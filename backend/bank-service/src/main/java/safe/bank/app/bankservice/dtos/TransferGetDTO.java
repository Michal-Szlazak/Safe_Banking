package safe.bank.app.bankservice.dtos;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class TransferGetDTO {

    String title;
    String receiverName;
    String senderNumber;
    String receiverNumber;
    BigDecimal amount;
    LocalDateTime timestamp;
}
