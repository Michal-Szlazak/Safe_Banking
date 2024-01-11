package safe.bank.app.bankservice.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankUser {

    @Id
    private UUID userId;

    private String firstName;
    private String lastName;

    private String phoneNumber;
}
