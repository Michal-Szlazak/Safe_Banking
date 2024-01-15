package safe.bank.app.bankservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String receiverName;
    private String amount;
    private byte[] iv;

    @ManyToOne
    private BankAccount sender;

    @ManyToOne
    private BankAccount receiver;

    @CreationTimestamp
    private LocalDateTime timestamp;
}
