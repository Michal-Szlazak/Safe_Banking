package safe.bank.app.authservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import safe.bank.app.authservice.entities.PartialPasswordPart;

import java.util.UUID;

@Repository
public interface PartialPasswordPartRepository
        extends JpaRepository<PartialPasswordPart, UUID> {
}
