package safe.bank.app.bankservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankUser {

    @Id
    private UUID userId;

    private String phoneNumber;

    @OneToMany
    @JoinColumn(name = "userId")
    private List<BankAccount> bankAccounts;

    @OneToMany(mappedBy = "sender")
    private List<Transfer> sentTransfers;

    @OneToMany(mappedBy = "receiver")
    private List<Transfer> receivedTransfers;
}
