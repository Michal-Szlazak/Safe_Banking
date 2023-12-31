package safe.bank.app.authservice.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class BankUserDTO {

    private UUID userId;
    private String phoneNumber;
}
