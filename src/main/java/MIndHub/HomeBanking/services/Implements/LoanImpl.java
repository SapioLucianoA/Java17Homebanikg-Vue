package MIndHub.HomeBanking.services.Implements;

import MIndHub.HomeBanking.models.Loan;
import MIndHub.HomeBanking.repositories.LoanRepository;
import MIndHub.HomeBanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class LoanImpl implements LoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Override
    public Optional<Loan> findLoanById(String id) {
        return loanRepository.findById(id);
    }

    @Override
    public List<Loan> findAllLoans() {
        return loanRepository.findAll();
    }

    @Override
    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }

    @Override
    public Loan findLoanByName(String name) {
        return null;
    }

}
