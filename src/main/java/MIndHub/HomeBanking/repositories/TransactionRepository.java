package MIndHub.HomeBanking.repositories;

import MIndHub.HomeBanking.models.Account;
import MIndHub.HomeBanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByAccount (Account account);
}
