package safe.bank.app.bankservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class User {

    @Id
    private UUID userId;

    private String phone;
    private String accountNumber;
    private double accountBalance;
}
