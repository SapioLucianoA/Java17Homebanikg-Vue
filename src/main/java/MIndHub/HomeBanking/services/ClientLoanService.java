package MIndHub.HomeBanking.services;

import MIndHub.HomeBanking.models.Client;
import MIndHub.HomeBanking.models.ClientLoan;
import MIndHub.HomeBanking.models.Loan;

import java.util.List;
import java.util.Optional;

public interface ClientLoanService {
    ClientLoan findClientLoanByClientAndLoanNameAndIsActive (Client client, String name, boolean isActive);

    void saveClientLoan (ClientLoan clientLoan);

    List<ClientLoan> findAllClientLoans ();

    Optional<ClientLoan> findCLienLoanById(String id);

    boolean existClientLoanByClientLoanAndIsActive(Client client, Loan loan, boolean isActive);
}
