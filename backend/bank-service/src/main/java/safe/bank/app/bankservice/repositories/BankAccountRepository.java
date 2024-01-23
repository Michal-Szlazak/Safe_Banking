package safe.bank.app.bankservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import safe.bank.app.bankservice.entities.BankAccount;
import safe.bank.app.bankservice.entities.BankUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {

    @Query("SELECT b.accountNumber FROM BankAccount b")
    List<String> findAllAccountNumbers();

    List<BankAccount> findAllByBankUser(BankUser bankUser);

    Optional<BankAccount> findBankAccountByAccountNumber(String accountNumber);
}
