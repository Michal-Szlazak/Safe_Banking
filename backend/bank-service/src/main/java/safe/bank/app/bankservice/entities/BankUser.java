package safe.bank.app.bankservice.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BankUser {

    @Id
    private UUID userId;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private byte[] iv;
}
