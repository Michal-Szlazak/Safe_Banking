package safe.bank.app.bankservice.dtos;

import java.time.Instant;

public record GeneratedAccountDTO (
    String accountNumber,
    Instant expiresAt,
    String balance
) {}
