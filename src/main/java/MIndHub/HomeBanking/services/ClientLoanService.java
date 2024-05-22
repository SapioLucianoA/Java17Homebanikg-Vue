package MIndHub.HomeBanking.services;

import MIndHub.HomeBanking.models.Client;
import MIndHub.HomeBanking.models.ClientLoan;
import MIndHub.HomeBanking.models.Loan;

import java.util.List;

public interface ClientLoanService {
    ClientLoan findClientLoanByClientAndLoan (Client client, Loan loan);

    void saveClientLoan (ClientLoan clientLoan);

    List<ClientLoan> findAllClientLoans ();

    ClientLoan findClientLoanByClientIdAndLoanId(String clientId, String loanId);

    boolean existClientLoanByClientLoanAndIsActive(Client client, Loan loan, boolean isActive);
}
