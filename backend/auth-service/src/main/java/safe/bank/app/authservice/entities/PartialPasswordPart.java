package safe.bank.app.authservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PartialPasswordPart {

    @Id
    @GeneratedValue
    private UUID id;
    private byte[] salt;
    private byte[] share;
}
