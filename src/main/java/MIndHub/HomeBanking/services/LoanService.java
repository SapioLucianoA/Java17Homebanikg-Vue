package MIndHub.HomeBanking.services;

import MIndHub.HomeBanking.models.Client;
import MIndHub.HomeBanking.models.ClientLoan;
import MIndHub.HomeBanking.models.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService {
    //Loan

    Optional<Loan> findLoanById (String id);

    List<Loan> findAllLoans ();

    void saveLoan (Loan loan);

    Loan findLoanByName(String name);

}
