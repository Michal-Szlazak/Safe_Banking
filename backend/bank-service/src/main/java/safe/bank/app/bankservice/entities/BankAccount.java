package safe.bank.app.bankservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {

    @Id
    @GeneratedValue
    private UUID accountId;
    private String accountName;
    private String accountNumber;
    private String cvv;
    private Instant expiresAt;
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "userId")
    private BankUser bankUser;
}
