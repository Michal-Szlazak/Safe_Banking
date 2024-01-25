package safe.bank.app.authservice.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
public class PartialPasswordSet {

    @Id
    private UUID userId;
    private int n;
    private int k;
    private byte[] secret;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PartialPasswordPart> parts;
}
