package MIndHub.HomeBanking.services.Implements;

import MIndHub.HomeBanking.models.Account;
import MIndHub.HomeBanking.models.Transaction;
import MIndHub.HomeBanking.repositories.TransactionRepository;
import MIndHub.HomeBanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> findAllTransactionsByAccount(Account account) {
        return transactionRepository.findAllByAccount(account);
    }

}
