package safe.bank.app.bankservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import safe.bank.app.bankservice.entities.Transfer;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, UUID> {

    List<Transfer> findAllBySenderBankUserUserId(UUID userId);
}
