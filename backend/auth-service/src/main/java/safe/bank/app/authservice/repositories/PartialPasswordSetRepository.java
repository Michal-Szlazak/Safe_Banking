package safe.bank.app.authservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import safe.bank.app.authservice.entities.PartialPasswordSet;

import java.util.UUID;

public interface PartialPasswordSetRepository extends JpaRepository<PartialPasswordSet, UUID> {
}
