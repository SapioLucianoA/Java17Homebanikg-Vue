package MIndHub.HomeBanking.repositories;

import MIndHub.HomeBanking.models.ClientLoan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientLoanRepository extends JpaRepository<ClientLoan, String> {
}
