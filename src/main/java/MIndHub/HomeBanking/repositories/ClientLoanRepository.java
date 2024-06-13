package MIndHub.HomeBanking.repositories;

import MIndHub.HomeBanking.models.Client;
import MIndHub.HomeBanking.models.ClientLoan;
import MIndHub.HomeBanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientLoanRepository extends JpaRepository<ClientLoan, String> {
    boolean existsByClientAndLoanAndIsActive(Client client, Loan loan,boolean isActive);

    ClientLoan findClientLoanByClientAndLoanNameAndIsActive(Client cLient, String name, boolean isActive);
}
