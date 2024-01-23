package safe.bank.app.bankservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import safe.bank.app.bankservice.entities.BankUser;

import java.util.UUID;

@Repository
public interface BankUserRepository extends JpaRepository<BankUser, UUID> {
}
