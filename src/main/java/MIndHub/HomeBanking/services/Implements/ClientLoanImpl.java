package MIndHub.HomeBanking.services.Implements;

import MIndHub.HomeBanking.models.Client;
import MIndHub.HomeBanking.models.ClientLoan;
import MIndHub.HomeBanking.models.Loan;
import MIndHub.HomeBanking.repositories.ClientLoanRepository;
import MIndHub.HomeBanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientLoanImpl implements ClientLoanService {
    @Autowired
    private ClientLoanRepository clientLoanRepository;


    @Override
    public ClientLoan findClientLoanByClientAndLoanNameAndIsActive(Client client, String name, boolean isActive) {
        return clientLoanRepository.findClientLoanByClientAndLoanNameAndIsActive(client, name, isActive);
    }

    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }

    @Override
    public List<ClientLoan> findAllClientLoans() {
        return clientLoanRepository.findAll();
    }

    @Override
    public Optional<ClientLoan> findCLienLoanById(String id) {
        return clientLoanRepository.findById(id);
    }


    @Override
    public boolean existClientLoanByClientLoanAndIsActive(Client client, Loan loan, boolean isActive) {
        return clientLoanRepository.existsByClientAndLoanAndIsActive(client, loan,isActive);
    }


}
