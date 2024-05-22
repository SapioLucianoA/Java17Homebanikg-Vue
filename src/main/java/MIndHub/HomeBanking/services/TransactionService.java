package MIndHub.HomeBanking.services;

import MIndHub.HomeBanking.models.Account;
import MIndHub.HomeBanking.models.Transaction;

import java.util.List;

public interface TransactionService {
    void saveTransaction (Transaction transaction);

    List<Transaction> findAllTransactions ();

    List<Transaction> findAllTransactionsByAccount(Account account);
}
