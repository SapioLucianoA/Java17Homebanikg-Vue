package MIndHub.HomeBanking.repositories;

import MIndHub.HomeBanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanRepository extends JpaRepository <Loan, String> {
    @Override
    Optional<Loan> findById(String id);

    Loan findLoanByName(String name);

    @Override
    boolean existsById(String id);
}
